package com.example.bookshop.app.services;

import com.example.bookshop.app.config.security.UserDetails;
import com.example.bookshop.app.config.security.UserDetailsService;
import com.example.bookshop.app.config.security.jwt.JWTUtil;
import com.example.bookshop.app.config.security.oauth.CustomOAuth2User;
import com.example.bookshop.app.model.dao.JwtBlockListRepository;
import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.ContactConfirmationPayload;
import com.example.bookshop.web.dto.ContactConfirmationResponse;
import com.example.bookshop.web.dto.RegistrationFormDto;
import com.example.bookshop.web.exception.CustomAuthenticationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.RetryingTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserRegisterServiceTest {

    @Value("${auth.secret}")
    private String authSecret;

    private RegistrationFormDto registrationForm;
    private ContactConfirmationPayload contactConfirmation;
    private CustomOAuth2User oAuth2User;

    private final static String REGISTER_USER_NAME = "Tester";
    private final static String REGISTER_USER_EMAIL = "test@mail.org";
    private final static String REGISTER_USER_PASSWORD = "111";
    private final static String REGISTER_USER_PHONE = "+79030000000";
    private final static String EXISTING_USER_NAME = "Admin Admin";
    private final static String EXISTING_USER_EMAIL = "admin@gmail.com";
    private final static String ONE_TIME_CODE = "111 111";
    private final static String EXCEPTION_MESSAGE = "user not found doh!";

    @Mock
    UserRepository userRepo;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserDetailsService userDetailsService;
    @Mock
    JwtBlockListRepository jwtBlockListRepository;
    @Mock
    OneTimeCodeService oneTimeCodeService;
    @Spy
    JWTUtil jwtUtil = new JWTUtil(jwtBlockListRepository);

    @InjectMocks
    UserRegisterService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", authSecret);
        oAuth2User = generateOAuth2User();
        contactConfirmation = generateContactConfirmationPayload();
        registrationForm = generateRegistrationForm();
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
        oAuth2User = null;
        contactConfirmation = null;
    }

    @Test
    void registerNewUserByRegistrationFormTest() {
        service.registerNewUser(registrationForm);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(captor.capture());
        User user = captor.getValue();

        when(passwordEncoder.encode(registrationForm.getPassword()))
                .thenReturn(REGISTER_USER_PASSWORD);

        assertNotNull(user);
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));

        verify(userRepo, Mockito.times(1))
                .save(any(User.class));
    }

    @Test
    void registerNewUserByOAuth2Test() {
        UserDetails userDetails = service.registerNewUser(oAuth2User);

        verify(userRepo, Mockito.times(1))
                .save(any(User.class));

        assertEquals(userDetails.getUser().getName(),
                oAuth2User.getAttributes().get("name"));
        assertEquals(userDetails.getUser().getEmail(),
                oAuth2User.getAttributes().get("email"));
    }

    @Test
    void registerNewUserFailTest() {
        when(userRepo.findUserByEmail(registrationForm.getEmail())).thenReturn(new User());
        service.registerNewUser(registrationForm);
        verify(userRepo, never()).save(any(User.class));
    }

    @RetryingTest(maxAttempts = 3)
    void loginSuccessfulTest() throws CustomAuthenticationException {
        UserDetails userDetails = generateUserDetails();

        when(oneTimeCodeService.verifyCode(any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(contactConfirmation.getContact()))
                .thenReturn(userDetails);

        ContactConfirmationResponse response = service.login(contactConfirmation);

        assertNotNull(response);
        assertEquals(response.getResult(), generateExpectedToken());
    }

    @Test
    void loginFailedIfUserNotExistTest() {
        when(oneTimeCodeService.verifyCode(any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(EXISTING_USER_EMAIL))
                .thenThrow(new UsernameNotFoundException(EXCEPTION_MESSAGE));

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> service.login(contactConfirmation));

        verify(jwtUtil, never()).generateToken(any(UserDetails.class));
        assertEquals(exception.getMessage(), EXCEPTION_MESSAGE);
    }

    private RegistrationFormDto generateRegistrationForm() {
        RegistrationFormDto registrationForm = new RegistrationFormDto();
        registrationForm.setEmail(REGISTER_USER_EMAIL);
        registrationForm.setName(REGISTER_USER_NAME);
        registrationForm.setPassword(REGISTER_USER_PASSWORD);
        registrationForm.setPhone(REGISTER_USER_PHONE);
        return registrationForm;
    }

    private ContactConfirmationPayload generateContactConfirmationPayload() {
        return new ContactConfirmationPayload(EXISTING_USER_EMAIL, ONE_TIME_CODE);
    }

    private UserDetails generateUserDetails() {
        User user = new User(EXISTING_USER_NAME, EXISTING_USER_EMAIL);
        return new UserDetails(user);
    }

    private String generateExpectedToken() {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(EXISTING_USER_EMAIL)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, authSecret)
                .compact();
    }

    private CustomOAuth2User generateOAuth2User() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", REGISTER_USER_NAME);
        attributes.put("email", REGISTER_USER_EMAIL);

        GrantedAuthority authority = new OAuth2UserAuthority(attributes);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);

        DefaultOAuth2User OAuthUser = new DefaultOAuth2User(authorities, attributes, "email");
        return new CustomOAuth2User(OAuthUser);
    }

}
