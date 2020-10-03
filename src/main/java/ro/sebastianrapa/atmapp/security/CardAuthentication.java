package ro.sebastianrapa.atmapp.security;

import ro.sebastianrapa.atmapp.model.Card;

public class CardAuthentication {

    private static final int MAX_AMOUNT_OF_WRONG_INPUT = 3;

    private Card introducedCard;
    private int wrongPinInputs;
    private boolean isAuthenticated;

    public Card getIntroducedCard() {
        return introducedCard;
    }

    public void instantiateAuthForCard(Card introducedCard) {
        // Set the introduced card to the given card
        this.introducedCard = introducedCard;
        // When introducing a new card reinitialize the number of wrong pin inputs
        this.wrongPinInputs = 0;
        // Set authenticated to false
        this.isAuthenticated = false;
    }

    public int getWrongPinInputs() {
        return wrongPinInputs;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public void wrongPinAttempt() {
        wrongPinInputs++;
    }

    public boolean tooManyWrongAttempts() {
        return wrongPinInputs == MAX_AMOUNT_OF_WRONG_INPUT;
    }

    public boolean hasIntroducedCard() {
        return null != introducedCard;
    }

    @Override
    public String toString() {
        return "CardAuthentication{" +
                "introducedCard=" + introducedCard +
                ", wrongPinInputs=" + wrongPinInputs +
                ", isAuthenticated=" + isAuthenticated +
                '}';
    }
}
