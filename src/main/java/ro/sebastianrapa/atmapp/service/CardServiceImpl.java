package ro.sebastianrapa.atmapp.service;

import org.springframework.stereotype.Service;
import ro.sebastianrapa.atmapp.dao.CardDao;
import ro.sebastianrapa.atmapp.model.Card;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private transient final CardDao cardDao;

    public CardServiceImpl(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    @Override
    public List<Card> fetchAllCards() {
        return cardDao.fetchAllCards();
    }

    @Override
    public void save(Card newCard) {
        cardDao.save(newCard);
    }
}
