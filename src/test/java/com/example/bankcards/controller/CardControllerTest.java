package com.example.bankcards.controller;

import com.example.bankcards.entity.Card;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCards_ShouldReturnListOfCards() {
        // Arrange
        Card card1 = new Card();
        card1.setId(1L);
        Card card2 = new Card();
        card2.setId(2L);
        List<Card> expectedCards = Arrays.asList(card1, card2);
        when(cardService.getAllCards()).thenReturn(expectedCards);

        // Act
        ResponseEntity<List<Card>> response = cardController.getAllCards();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedCards, response.getBody());
        verify(cardService, times(1)).getAllCards();
    }

    @Test
    void getCardById_ShouldReturnCard() {
        // Arrange
        Long cardId = 1L;
        Card expectedCard = new Card();
        expectedCard.setId(cardId);
        when(cardService.getCardById(cardId)).thenReturn(expectedCard);

        // Act
        ResponseEntity<Card> response = cardController.getCardById(cardId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedCard, response.getBody());
        verify(cardService, times(1)).getCardById(cardId);
    }

    @Test
    void createCard_ShouldReturnCreatedCard() {
        // Arrange
        Card newCard = new Card();
        newCard.setNumber("1234567890123456");
        when(cardService.createCard(newCard)).thenReturn(newCard);

        // Act
        ResponseEntity<Card> response = cardController.createCard(newCard);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(newCard, response.getBody());
        verify(cardService, times(1)).createCard(newCard);
    }

    @Test
    void updateCard_ShouldReturnUpdatedCard() {
        // Arrange
        Long cardId = 1L;
        Card updatedCard = new Card();
        updatedCard.setId(cardId);
        updatedCard.setNumber("6543210987654321");
        when(cardService.updateCard(cardId, updatedCard)).thenReturn(updatedCard);

        // Act
        ResponseEntity<Card> response = cardController.updateCard(cardId, updatedCard);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedCard, response.getBody());
        verify(cardService, times(1)).updateCard(cardId, updatedCard);
    }

    @Test
    void deleteCard_ShouldReturnNoContent() {
        // Arrange
        Long cardId = 1L;
        doNothing().when(cardService).deleteCard(cardId);

        // Act
        ResponseEntity<Void> response = cardController.deleteCard(cardId);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(cardService, times(1)).deleteCard(cardId);
    }

    @Test
    void transferBetweenCards_ShouldReturnOk() {
        // Arrange
        Long fromCardId = 1L;
        Long toCardId = 2L;
        Double amount = 100.0;
        doNothing().when(cardService).transferBetweenCards(fromCardId, toCardId, amount);

        // Act
        ResponseEntity<Void> response = cardController.transferBetweenCards(fromCardId, toCardId, amount);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(cardService, times(1)).transferBetweenCards(fromCardId, toCardId, amount);
    }
}