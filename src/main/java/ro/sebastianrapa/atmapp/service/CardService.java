package ro.sebastianrapa.atmapp.service;

import ro.sebastianrapa.atmapp.model.Card;
import ro.sebastianrapa.atmapp.model.exception.runtime.CardNotFoundException;

import java.util.List;

public interface CardService {

    List<Card> fetchAllCards();

    void save(Card newCard);

    Card getCardByCardNumber(String cardNumber) throws CardNotFoundException;

    void blockCard(Card cardToBlock);
}
