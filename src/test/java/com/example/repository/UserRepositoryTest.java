package com.example.repository;

import com.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_ReturnsUser() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        
        entityManager.persist(user);
        entityManager.flush();

        // When
        User found = userRepository.findByEmail(user.getEmail()).orElse(null);

        // Then
        assertNotNull(found);
        assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void existsByEmail_ReturnsTrueForExistingEmail() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        
        entityManager.persist(user);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByEmail(user.getEmail());

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_ReturnsFalseForNonExistingEmail() {
        // When
        boolean exists = userRepository.existsByEmail("nonexisting@example.com");

        // Then
        assertFalse(exists);
    }
} 