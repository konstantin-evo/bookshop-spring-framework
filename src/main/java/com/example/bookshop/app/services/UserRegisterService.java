package com.example.bookshop.app.services;

import com.example.bookshop.app.config.security.BookshopUserDetails;
import com.example.bookshop.app.config.security.BookshopUserDetailsService;
import com.example.bookshop.app.config.security.jwt.JWTUtil;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2User;
import com.example.bookshop.app.model.dao.JwtBlockListRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.JwtBlockList;
import com.example.bookshop.app.model.entity.OneTimeCode;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.example.bookshop.app.services.RegexUtil.isEmail;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final JwtBlockListRepository jwtBlockListRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookshopUserDetailsService userDetailsService;
    private final OneTimeCodeService oneTimeCodeService;
    private final JWTUtil jwtUtil;

    /**
     * The SMS_CODE value is temporarily hardcoded due to the fact that the TWILIO service in free mode does not work stably,
     * and it is impossible to guarantee the operation of the operation of the service during launch
     */
    @Value("${twilio.magic_code}")
    private String smsCode;

    @Value("${twilio.expire_time_sec}")
    private int expireTimeSec;

    private static final String ANONYMOUS_USER = "anonymousUser";

    @Transient
    public boolean registerNewUser(RegistrationFormDto registrationForm) {

        Optional<User> userByEmail = userRepo.findUserByEmail(registrationForm.getEmail());
        Optional<User> userByPhone = userRepo.findUserByPhone(registrationForm.getPhone());

        if (userByEmail.isEmpty() && userByPhone.isEmpty()) {
            User user = userMapper.map(registrationForm, passwordEncoder.encode(registrationForm.getPassword()));
            userRepo.save(user);
            return true;
        } else {
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

    public void contactConfirmation(String contact) throws NoSuchAlgorithmException {
        String code = isEmail(contact)
                ? oneTimeCodeService.sendSecretCodeEmail(contact)
                : smsCode;
        oneTimeCodeService.saveCode(new OneTimeCode(code, expireTimeSec));
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

}
