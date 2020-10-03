package ro.sebastianrapa.atmapp.service;

import ro.sebastianrapa.atmapp.model.Card;

import java.util.List;

public interface CardService {

    List<Card> fetchAllCards();

    void save(Card newCard);
}
