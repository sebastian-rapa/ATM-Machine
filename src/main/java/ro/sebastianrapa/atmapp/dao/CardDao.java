package ro.sebastianrapa.atmapp.dao;

import ro.sebastianrapa.atmapp.model.Card;

import java.util.List;
import java.util.Optional;

public interface CardDao {

    List<Card> fetchAllCards();

    void save(Card newCard);

    Optional<Card> getCardByCardNumber(String cardNumber);
}
