package ro.sebastianrapa.atmapp.dao;

import ro.sebastianrapa.atmapp.model.Card;

import java.util.List;

public interface CardDao {

    List<Card> fetchAllCards();

    void save(Card newCard);
}
