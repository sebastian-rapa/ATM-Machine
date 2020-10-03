package ro.sebastianrapa.atmapp.service;

import org.springframework.stereotype.Service;
import ro.sebastianrapa.atmapp.dao.CardDao;
import ro.sebastianrapa.atmapp.model.Card;
import ro.sebastianrapa.atmapp.model.exception.runtime.CardNotFoundException;

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

    @Override
    public Card getCardByCardNumber(String cardNumber) throws CardNotFoundException {
        return cardDao.getCardByCardNumber(cardNumber)
                      .orElseThrow(() -> new CardNotFoundException("The card with the number: " + cardNumber + " was not found"));
    }

    @Override
    public void blockCard(Card cardToBlock) {
        cardToBlock.setBlockedCard(true);
    }
}
