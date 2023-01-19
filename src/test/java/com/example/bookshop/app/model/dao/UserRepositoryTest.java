package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void testAddNewUser(){
        User user = new User();
        user.setPassword("1234567890");
        user.setPhone("+79030000000");
        user.setName("Tester");
        user.setEmail("testing@test.org");

        assertNotNull(userRepository.save(user));
    }
}