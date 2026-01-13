package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Card not found with id: " + id));
    }

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    public Card updateCard(Long id, Card card) {
        Card existingCard = getCardById(id);
        existingCard.setNumber(card.getNumber());
        existingCard.setOwner(card.getOwner());
        existingCard.setExpiryDate(card.getExpiryDate());
        existingCard.setStatus(card.getStatus());
        existingCard.setBalance(card.getBalance());
        return cardRepository.save(existingCard);
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    @Transactional
    public void transferBetweenCards(Long fromCardId, Long toCardId, Double amount) {
        Card fromCard = getCardById(fromCardId);
        Card toCard = getCardById(toCardId);

        if (fromCard.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }

        fromCard.setBalance(fromCard.getBalance() - amount);
        toCard.setBalance(toCard.getBalance() + amount);

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }
}