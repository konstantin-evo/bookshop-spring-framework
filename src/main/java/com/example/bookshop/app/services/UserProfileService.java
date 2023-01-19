package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.ProfileChangesRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.dao.VerificationTokenRepository;
import com.example.bookshop.app.model.entity.ProfileChanges;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.VerificationToken;
import com.example.bookshop.web.dto.ProfileDto;
import com.example.bookshop.web.dto.ValidatedResponseDto;
import com.example.bookshop.web.exception.BookshopEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;

import static com.example.bookshop.app.services.RegexUtil.isEmail;
import static com.example.bookshop.app.services.RegexUtil.isPhoneNumber;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepo;
    private final ProfileChangesRepository profileChangesRepo;
    private final VerificationTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;


    /**
     * Method saves profile change information until it is confirmed by the user by mail
     *
     * @param profile - ProfileDto contains the information from the form filled in by the User
     * @param user    - Current session user
     * @return ValidatedResponseDto Dto object used to interact with Front-End to display errors
     */
    @Transactional
    public ValidatedResponseDto changeProfileInfo(ProfileDto profile, User user) {

        ValidatedResponseDto response = new ValidatedResponseDto(true, new HashMap<>());
        boolean isDtoCorrect = isDtoCorrect(profile, user, response);

        // if every field is validated save result in database
        // otherwise not validated result with error messages will be sent
        if (isDtoCorrect) {
            String encryptedPassword = passwordEncoder.encode(profile.getPassword());
            ProfileChanges profileChanges = UserProfileMapper.INSTANCE.map(profile, user, encryptedPassword);
            profileChangesRepo.save(profileChanges);
        }

        return response;
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
            ProfileChanges profileChanges = profileChangesRepo.findByUserAndEnabled(user, false)
                    .orElseThrow(() -> new BookshopEntityNotFoundException(ProfileChanges.class.getSimpleName(), "User", user.toString()));
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

    private boolean isDtoCorrect(ProfileDto profile, User user, ValidatedResponseDto response) {

        if (!isPhoneNumber(profile.getPhone())) {
            response.setValidated(false);
            response.getErrorMessages().put("Phone number", "Phone number isn't match the pattern.");
        }

        if (!isEmail(profile.getMail())) {
            response.setValidated(false);
            response.getErrorMessages().put("Email", "Email isn't match the pattern.");
        }

        if (passwordEncoder.matches(profile.getPassword(), user.getPassword())) {
            response.setValidated(false);
            response.getErrorMessages().put("Password", "The password must not be the same as the previous one.");
        }

        if (!profile.getPassword().equals(profile.getPasswordReply())) {
            response.setValidated(false);
            response.getErrorMessages().put("Password", "Unfortunately, the password was not saved.");
        }

        return response.isValidated();
    }

}
