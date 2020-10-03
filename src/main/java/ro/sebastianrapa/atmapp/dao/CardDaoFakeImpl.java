package ro.sebastianrapa.atmapp.dao;

import org.springframework.stereotype.Repository;
import ro.sebastianrapa.atmapp.model.Card;
import ro.sebastianrapa.atmapp.model.exception.runtime.CardNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CardDaoFakeImpl implements CardDao {

    private List<Card> FAKE_CARD_DB =  new ArrayList<>();

    @Override
    public List<Card> fetchAllCards() {
        return FAKE_CARD_DB.stream()
                           .filter(Card::unblockedCard)
                           .collect(Collectors.toList());
    }

    @Override
    public void save(Card newCard) {
        FAKE_CARD_DB.add(newCard);
    }

    @Override
    public Optional<Card> getCardByCardNumber(String cardNumber) {
        return FAKE_CARD_DB.stream()
                           .filter(card -> card.getCardNumber().equals(cardNumber))
                           .findFirst();
    }
}
