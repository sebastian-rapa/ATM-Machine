package ro.sebastianrapa.atmapp.dao;

import org.springframework.stereotype.Repository;
import ro.sebastianrapa.atmapp.model.Card;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CardDaoFakeImpl implements CardDao {

    private List<Card> FAKE_CARD_DB =  new ArrayList<>();

    @Override
    public List<Card> fetchAllCards() {
        return FAKE_CARD_DB;
    }

    @Override
    public void save(Card newCard) {
        FAKE_CARD_DB.add(newCard);
    }
}
