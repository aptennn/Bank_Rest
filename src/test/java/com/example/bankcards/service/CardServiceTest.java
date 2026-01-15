package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCards_ShouldReturnAllCards() {
        // Arrange
        Card card1 = new Card();
        card1.setId(1L);
        Card card2 = new Card();
        card2.setId(2L);
        List<Card> expectedCards = Arrays.asList(card1, card2);
        when(cardRepository.findAll()).thenReturn(expectedCards);

        // Act
        List<Card> result = cardService.getAllCards();

        // Assert
        assertEquals(expectedCards, result);
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void getCardById_ShouldReturnCard_WhenExists() {
        // Arrange
        Long cardId = 1L;
        Card expectedCard = new Card();
        expectedCard.setId(cardId);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(expectedCard));

        // Act
        Card result = cardService.getCardById(cardId);

        // Assert
        assertEquals(expectedCard, result);
        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void getCardById_ShouldThrowException_WhenNotExists() {
        // Arrange
        Long cardId = 1L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> cardService.getCardById(cardId));
        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void createCard_ShouldSaveAndReturnCard() {
        // Arrange
        Card newCard = new Card();
        newCard.setNumber("1234567890123456");
        when(cardRepository.save(newCard)).thenReturn(newCard);

        // Act
        Card result = cardService.createCard(newCard);

        // Assert
        assertEquals(newCard, result);
        verify(cardRepository, times(1)).save(newCard);
    }

    @Test
    void updateCard_ShouldUpdateAndReturnCard() {
        // Arrange
        Long cardId = 1L;
        Card existingCard = new Card();
        existingCard.setId(cardId);
        existingCard.setNumber("1111222233334444");
        
        Card updatedCard = new Card();
        updatedCard.setNumber("5555666677778888");
        
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));
        when(cardRepository.save(existingCard)).thenReturn(existingCard);

        // Act
        Card result = cardService.updateCard(cardId, updatedCard);

        // Assert
        assertEquals("5555666677778888", result.getNumber());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardRepository, times(1)).save(existingCard);
    }

    @Test
    void deleteCard_ShouldDeleteCard() {
        // Arrange
        Long cardId = 1L;
        doNothing().when(cardRepository).deleteById(cardId);

        // Act
        cardService.deleteCard(cardId);

        // Assert
        verify(cardRepository, times(1)).deleteById(cardId);
    }

    @Test
    void transferBetweenCards_ShouldTransferAmount() {
        // Arrange
        Long fromCardId = 1L;
        Long toCardId = 2L;
        Double amount = 100.0;
        
        Card fromCard = new Card();
        fromCard.setId(fromCardId);
        fromCard.setBalance(200.0);
        
        Card toCard = new Card();
        toCard.setId(toCardId);
        toCard.setBalance(50.0);
        
        when(cardRepository.findById(fromCardId)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(toCardId)).thenReturn(Optional.of(toCard));
        when(cardRepository.save(fromCard)).thenReturn(fromCard);
        when(cardRepository.save(toCard)).thenReturn(toCard);

        // Act
        cardService.transferBetweenCards(fromCardId, toCardId, amount);

        // Assert
        assertEquals(100.0, fromCard.getBalance());
        assertEquals(150.0, toCard.getBalance());
        verify(cardRepository, times(2)).findById(anyLong());
        verify(cardRepository, times(1)).save(fromCard);
        verify(cardRepository, times(1)).save(toCard);
    }

    @Test
    void transferBetweenCards_ShouldThrowException_WhenInsufficientBalance() {
        // Arrange
        Long fromCardId = 1L;
        Long toCardId = 2L;
        Double amount = 300.0;
        
        Card fromCard = new Card();
        fromCard.setId(fromCardId);
        fromCard.setBalance(200.0);
        
        Card toCard = new Card();
        toCard.setId(toCardId);
        toCard.setBalance(50.0);
        
        when(cardRepository.findById(fromCardId)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(toCardId)).thenReturn(Optional.of(toCard));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> cardService.transferBetweenCards(fromCardId, toCardId, amount));
        
        verify(cardRepository, times(2)).findById(anyLong());
        verify(cardRepository, never()).save(any());
    }
}