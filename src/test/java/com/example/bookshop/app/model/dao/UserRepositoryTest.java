package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests are written according to the course materials,
 * in the future it should be replaced with @DataJpaTest
 */
@SpringBootTest
@TestPropertySource("/application-test.properties")
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void testAddNewUser(){
        User user = new User();
        user.setPassword("1234567890");
        user.setPhone("+79030000000");
        user.setName("Tester");
        user.setEmail("testing@test.org");

        assertNotNull(userRepository.save(user));
    }
}