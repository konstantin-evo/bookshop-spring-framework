package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.ProfileDto;
import com.example.bookshop.web.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChangeProfileService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    // TODO: At the moment, changing profile information only supports changing the password
    //  - need to implement validation and changes for each field
    public ProfileResponseDto changeProfileInfo(ProfileDto profile, User user) {
        Pair<Boolean, Map<String, String>> changeInformation = changePassword(profile, user);
        return new ProfileResponseDto(changeInformation.getFirst(), changeInformation.getSecond());
    }

    /**
     * The method changes the Password in the User Profile
     *
     * @param profile - ProfileDto contains the information from the form filled in by the User
     * @param user    - Current session user
     * @return - Pair<Boolean, Map<String, String>>
     * 1. Boolean returns true if the data was successfully saved
     * 2. Map<String, String> contains error information (if any)
     */
    @Transient
    private Pair<Boolean, Map<String, String>> changePassword(ProfileDto profile, User user) {

        Map<String, String> errors = new HashMap<>();

        if (passwordEncoder.matches(profile.getPassword(), user.getPassword())) {
            errors.put("Password", "The password must not be the same as the previous one.");
            return Pair.of(false, errors);
        }

        if (profile.getPassword().equals(profile.getPasswordReply())) {
            user.setPassword(passwordEncoder.encode(profile.getPassword()));
            userRepo.save(user);
            return Pair.of(true, errors);
        } else {
            errors.put("Password", "Unfortunately, the password was not saved.");
            return Pair.of(false, errors);
        }
    }

}
