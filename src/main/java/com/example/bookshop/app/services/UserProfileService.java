package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.ProfileChangesRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.dao.VerificationTokenRepository;
import com.example.bookshop.app.model.entity.ProfileChanges;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.VerificationToken;
import com.example.bookshop.web.dto.ProfileDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepo;
    private final ProfileChangesRepository profileChangesRepo;
    private final VerificationTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;

    // TODO: At the moment, changing profile information only supports changing the password
    //  - need to implement validation and changes for each field
    public ValidatedResponseDto changeProfileInfo(ProfileDto profile, User user) {
        return saveTempProfileInfo(profile, user);
    }

    /**
     * The method saves temporary changes to the User table if the changes are confirmed by the user.
     *
     * @param token - uniq token that contains a unique token and an expiry date
     *              has relation one to one with ProfileChanges
     */
    @Transactional
    public void confirmProfileChanges(String token) {
        VerificationToken verificationToken = tokenRepo.findByToken(token);
        User user = verificationToken.getProfileChanges().getUser();

        boolean isTokenExpired = verificationToken.getExpiryDate().before(new Timestamp(System.currentTimeMillis()));

        if (!isTokenExpired) {
            ProfileChanges profileChanges = profileChangesRepo.findByUserAndEnabled(user, false);
            profileChangesRepo.updateEnabled(true, profileChanges.getId());

            UserProfileMapper.INSTANCE.update(user, profileChanges);
            userRepo.save(user);
        }
    }

    @Transactional
    public void topUpAccount(Integer sum, User user) {
        int id = user.getId();
        userRepo.updateBalance(sum, id);
    }

    /**
     * Method saves profile change information until it is confirmed by the user by mail
     *
     * @param profile - ProfileDto contains the information from the form filled in by the User
     * @param user    - Current session user
     * @return - Pair<Boolean, Map<String, String>>
     * 1. Boolean returns true if the data was successfully saved
     * 2. Map<String, String> contains error information (if any)
     */
    @Transient
    private ValidatedResponseDto saveTempProfileInfo(ProfileDto profile, User user) {

        Map<String, String> errors = new HashMap<>();

        if (passwordEncoder.matches(profile.getPassword(), user.getPassword())) {
            errors.put("Password", "The password must not be the same as the previous one.");
            return new ValidatedResponseDto(false, errors);
        }

        if (profile.getPassword().equals(profile.getPasswordReply())) {
            String encryptedPassword = passwordEncoder.encode(profile.getPassword());
            ProfileChanges profileChanges = UserProfileMapper.INSTANCE.map(profile, user, encryptedPassword);
            profileChangesRepo.save(profileChanges);
            return new ValidatedResponseDto(true, errors);
        } else {
            errors.put("Password", "Unfortunately, the password was not saved.");
            return new ValidatedResponseDto(false, errors);
        }
    }

}
