package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser.setRole("USER");
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    void findAll_ShouldReturnAllCards() {
        // Arrange
        Card card1 = createTestCard("1111222233334444", 1000.0, "John Doe", testUser);
        entityManager.persist(card1);

        Card card2 = createTestCard("5555666677778888", 2000.0, "Jane Smith", testUser);
        entityManager.persist(card2);

        entityManager.flush();

        // Act
        List<Card> cards = cardRepository.findAll();

        // Assert
        assertEquals(2, cards.size());
    }

    @Test
    void findById_ShouldReturnCard_WhenExists() {
        // Arrange
        Card card = createTestCard("1111222233334444", 1000.0, "John Doe", testUser);
        Card savedCard = entityManager.persist(card);
        entityManager.flush();

        // Act
        Card found = cardRepository.findById(savedCard.getId()).orElse(null);

        // Assert
        assertNotNull(found);
        assertEquals(savedCard.getNumber(), found.getNumber());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        // Act
        Card found = cardRepository.findById(999L).orElse(null);

        // Assert
        assertNull(found);
    }

    private Card createTestCard(String number, double balance, String owner, User user) {
        Card card = new Card();
        card.setNumber(number);
        card.setBalance(balance);
        card.setOwner(owner);
        card.setExpiryDate(LocalDate.now().plusYears(2));
        card.setStatus(Card.CardStatus.ACTIVE);
        card.setUser(user);
        return card;
    }
}