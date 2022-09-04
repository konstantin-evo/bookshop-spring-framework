package com.example.bookshop.app.services;

import com.example.bookshop.app.config.security.UserDetailsService;
import com.example.bookshop.app.config.security.jwt.JWTUtil;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2User;
import com.example.bookshop.app.model.dao.JwtBlockListRepository;
import com.example.bookshop.app.model.entity.JwtBlockList;
import com.example.bookshop.app.model.entity.OneTimeCode;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.app.config.security.BookshopUserDetails;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import com.example.bookshop.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepo;
    private final JwtBlockListRepository jwtBlockListRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final OneTimeCodeService oneTimeCodeService;
    private final JWTUtil jwtUtil;

    /**
     * The SMS_CODE value is temporarily hardcoded due to the fact that the TWILIO service in free mode does not work stably,
     * and it is impossible to guarantee the operation of the operation of the service during launch
     */
    @Value("${twilio.magic_code}")
    private String SMS_CODE;

    @Value("${twilio.expire_time_sec}")
    private int EXPIRE_TIME_SEC;

    private static final String EMAIL_PATTERN = "[A-Za-z\\d._%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,4}";
    private static final String ANONYMOUS_USER = "anonymousUser";

    @Transient
    public boolean registerNewUser(RegistrationFormDto registrationForm) {

        User userByEmail = userRepo.findUserByEmail(registrationForm.getEmail())
                .orElseThrow(() -> new UserNotFoundException(registrationForm.getEmail(), true));
        User userByPhone = userRepo.findUserByPhone(registrationForm.getPhone())
                .orElseThrow(() -> new UserNotFoundException(registrationForm.getPhone(), false));

        if (userByEmail == null && userByPhone == null) {
            User user = new User();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            userRepo.save(user);
            return true;
        } else {
            // TODO: Add error handling if user already exists
            return false;
        }
    }

    @Transient
    public BookshopUserDetails registerNewUser(CustomOAuth2User oAuth2User) {
        User user = new User(
                (String) oAuth2User.getAttributes().get("name"),
                (String) oAuth2User.getAttributes().get("email"));
        userRepo.save(user);
        return new BookshopUserDetails(user);
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) throws CustomAuthenticationException {

        BookshopUserDetails userDetails = (BookshopUserDetails) userDetailsService
                .loadUserByUsername(payload.getContact());

        return isEmail(payload.getContact())
                ? loginByEmail(payload.getCode(), userDetails)
                : loginByPhone(payload, userDetails);
    }

    public void logout(String token) {
        JwtBlockList jwtBlockList = new JwtBlockList(token);
        jwtBlockListRepo.save(jwtBlockList);
    }

    public void contactConfirmation(String contact) {
        String code = isEmail(contact)
                ? oneTimeCodeService.sendSecretCodeEmail(contact)
                : SMS_CODE;
        oneTimeCodeService.saveCode(new OneTimeCode(code, EXPIRE_TIME_SEC));
    }

    public Object getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != ANONYMOUS_USER) {
            BookshopUserDetails userDetails = (BookshopUserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            return userDetails.getUser();
        } else {
            return null;
        }
    }

    private ContactConfirmationResponse loginByEmail(String code, BookshopUserDetails userDetails)
            throws CustomAuthenticationException {
        if (oneTimeCodeService.verifyCode(code)) {
            String jwtToken = jwtUtil.generateToken(userDetails);
            return new ContactConfirmationResponse(jwtToken);
        } else {
            throw new CustomAuthenticationException("The code has expired or an incorrect code has been entered");
        }
    }

    private ContactConfirmationResponse loginByPhone(ContactConfirmationPayload payload,
                                                     BookshopUserDetails userDetails) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (payload.getContact(), payload.getCode()));
        String jwtToken = jwtUtil.generateToken(userDetails);
        return new ContactConfirmationResponse(jwtToken);
    }

    private boolean isEmail(String payload) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(payload);
        return matcher.matches();
    }
}
