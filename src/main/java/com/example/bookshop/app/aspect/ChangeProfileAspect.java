package com.example.bookshop.app.aspect;

import com.example.bookshop.app.model.dao.ProfileChangesRepository;
import com.example.bookshop.app.model.dao.VerificationTokenRepository;
import com.example.bookshop.app.model.entity.ProfileChanges;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.app.model.entity.VerificationToken;
import com.example.bookshop.web.dto.ProfileDto;
import com.example.bookshop.web.services.SimpleMailSender;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Aspect monitors service methods related to changes in the User Profile
 */
@Component
@Aspect
@RequiredArgsConstructor
public class ChangeProfileAspect {

    private final MessageSource messages;
    private final SimpleMailSender mailSender;
    private final ProfileChangesRepository profileChangesRepo;
    private final VerificationTokenRepository tokenRepo;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${mail.username}")
    private String bookstoreEmail;

    @Pointcut(value = "execution(public * com.example.bookshop.app.services.UserProfileService.changeProfileInfo(..))"
            + "&& args(profile, user, ..)", argNames = "profile, user")
    public void changeProfile(ProfileDto profile, User user) {
    }

    /**
     * The method creates a token, then sends the change confirmation information to the user's email
     * after the temporary Profiles data has been saved in the DB
     *
     * @param profile - ProfileDto contains the information from the form filled in by the User
     * @param user    - Current session user
     */
    @AfterReturning(value = "changeProfile(profile, user)", argNames = "profile, user")
    public void confirmChanges(ProfileDto profile, User user) {

        Optional<ProfileChanges> profileChanges = profileChangesRepo.findByUserAndEnabled(user, false);

        if (profileChanges.isPresent()) {
            VerificationToken token = new VerificationToken(profileChanges.get(), UUID.randomUUID().toString());
            tokenRepo.save(token);

            String recipientAddress = user.getEmail();
            String subject = messages.getMessage("message.changeProfile.subject", null, Locale.ENGLISH);
            String confirmationUrl = "profile/confirmChanges?token=" + token.getToken();
            String message = messages.getMessage("message.changeProfile.info", null, Locale.ENGLISH)
                    + "\r\n" + baseUrl + confirmationUrl;

            mailSender.send(subject, message, bookstoreEmail, recipientAddress);
        }
    }
}
