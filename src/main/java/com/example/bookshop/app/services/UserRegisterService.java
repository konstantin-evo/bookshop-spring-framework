package com.example.bookshop.app.services;

import com.example.bookshop.app.config.security.UserDetailsService;
import com.example.bookshop.app.config.security.jwt.JWTUtil;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2User;
import com.example.bookshop.app.model.dao.JwtBlockListRepository;
import com.example.bookshop.app.model.entity.JwtBlockList;
import com.example.bookshop.app.model.entity.OneTimeCode;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.app.config.security.UserDetails;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserRegisterService {

    private final UserRepository userRepo;
    private final JwtBlockListRepository jwtBlockListRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final OneTimeCodeService oneTimeCodeService;
    private final JWTUtil jwtUtil;

    @Value("${twilio.expire_time_sec}")
    private int EXPIRE_TIME_SEC;

    private static final String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    private static final String ANONYMOUS_USER = "anonymousUser";

    public UserRegisterService(UserRepository userRepo,
                               JwtBlockListRepository jwtBlockListRepo,
                               PasswordEncoder passwordEncoder,
                               UserDetailsService userDetailsService,
                               OneTimeCodeService oneTimeCodeService,
                               JWTUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtBlockListRepo = jwtBlockListRepo;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.oneTimeCodeService = oneTimeCodeService;
        this.jwtUtil = jwtUtil;
    }

    @Transient
    public void registerNewUser(RegistrationFormDto registrationForm) {

        User userByEmail = userRepo.findUserByEmail(registrationForm.getEmail());
        User userByPhone = userRepo.findUserByPhone(registrationForm.getPhone());

        if (userByEmail == null && userByPhone == null) {
            User user = new User();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            userRepo.save(user);
        }
    }

    @Transient
    public UserDetails registerNewUser(CustomOAuth2User oAuth2User) {
        User user = new User(
                (String) oAuth2User.getAttributes().get("name"),
                (String) oAuth2User.getAttributes().get("email"));
        userRepo.save(user);
        return new UserDetails(user);
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) throws CustomAuthenticationException {

        if (oneTimeCodeService.verifyCode(payload.getCode())) {
            UserDetails userDetails = (UserDetails) userDetailsService
                    .loadUserByUsername(payload.getContact());
            String jwtToken = jwtUtil.generateToken(userDetails);
            return new ContactConfirmationResponse(jwtToken);
        } else {
            throw new CustomAuthenticationException("The code has expired or an incorrect code has been entered");
        }
    }

    public void logout(String token) {
        JwtBlockList jwtBlockList = new JwtBlockList(token);
        jwtBlockListRepo.save(jwtBlockList);
    }

    public void contactConfirmation(String contact) {
        String code = isEmail(contact)
                ? oneTimeCodeService.sendSecretCodeEmail(contact)
                : oneTimeCodeService.sendSecretCodeSms(contact);

        oneTimeCodeService.saveCode(new OneTimeCode(code, EXPIRE_TIME_SEC));
    }

    public Object getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != ANONYMOUS_USER) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            return userDetails.getUser();
        } else {
            return null;
        }
    }

    private boolean isEmail(String payload) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(payload);
        return matcher.matches();
    }
}
