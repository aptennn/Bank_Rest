package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        User found = userRepository.findByUsername(user.getUsername()).orElse(null);

        // Assert
        assertNotNull(found);
        assertEquals(user.getUsername(), found.getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenNotExists() {
        // Act
        User found = userRepository.findByUsername("nonexistent").orElse(null);

        // Assert
        assertNull(found);
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenExists() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        boolean exists = userRepository.existsByUsername(user.getUsername());

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenNotExists() {
        // Act
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Assert
        assertFalse(exists);
    }
}