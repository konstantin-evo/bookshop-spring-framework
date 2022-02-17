package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.UserRepository;
import com.example.bookshop.app.model.entity.User;
import com.example.bookshop.web.dto.RegistrationFormDto;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRegisterServiceTest {

    private RegistrationFormDto registrationForm;

    @Mock
    UserRepository userRepo;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserRegisterService service;

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationFormDto();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPassword("111");
        registrationForm.setPhone("9031232323");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUserTest() {
        service.registerNewUser(registrationForm);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(captor.capture());
        User user = captor.getValue();

        when(passwordEncoder.encode(registrationForm.getPassword()))
                .thenReturn("111");

        assertNotNull(user);
        assertTrue(CoreMatchers.is(user.getName()).matches(registrationForm.getName()));
        assertTrue(CoreMatchers.is(user.getEmail()).matches(registrationForm.getEmail()));

        verify(userRepo, Mockito.times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    void registerNewUserFailTest(){
        when(userRepo.findUserByEmail(registrationForm.getEmail())).thenReturn(new User());
        service.registerNewUser(registrationForm);
        verify(userRepo, never()).save(Mockito.any(User.class));
    }
}
