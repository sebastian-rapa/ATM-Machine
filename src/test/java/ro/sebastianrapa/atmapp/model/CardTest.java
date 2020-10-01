package ro.sebastianrapa.atmapp.model;

import org.junit.jupiter.api.Test;
import ro.sebastianrapa.atmapp.form.CardCreateForm;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Card
 * */
class CardTest {

    /**
     * Lower bound for a Digit Group
     * */
    private static final int LOW_DIGIT_GROUP_BOUND = 999;
    /**
     * Higher bound for a Digit Group
     * */
    private static final int HIGH_DIGIT_GROUP_BOUND = 10_000;
    /**
     * This is the number of Digit Groups on a Card
     * */
    private static final int NUMBER_OF_DIGIT_GROUPS = 4;
    /**
     * Lower bound for a CVV
     * */
    private static final int LOW_CVV_BOUND = 99;
    /**
     * Higher bound for a CVV
     * */
    private static final int HIGH_CVV_BOUND = 1000;
    /**
     * Lower bound for a Month
     * */
    private static final int MONTH_LOW_BOUND = 1;
    /**
     * Higher bound for a Month
     * */
    private static final int MONTH_HIGH_BOUND = 13;
    /**
     * This is a Default pin code
     * */
    private static final String DEFAULT_PIN_CODE = "0000";

    /**
     * Default constructor
     * */
    public CardTest() {
    }

    /**
     * Test for the card number
     * */
    @Test
    public void cardNumberTest() {
        // Get a demo card
        final Card testCard = getDemoCard();

        // Get the card number
        final String cardNumber = testCard.getCardNumber();

        // Split the card number in 4 groups
        final String[] digitGroups = cardNumber.split("\\s");

        // Make sure there are 4 digit groups
        assertEquals( NUMBER_OF_DIGIT_GROUPS, digitGroups.length, "The number of digit groups should be 4");

        int integerDigitGroup;

        // Go through each digit group and check to in bounds
        for (final String digitGroup : digitGroups) {
            // Convert the string into an integer
            integerDigitGroup = Integer.parseInt(digitGroup);
            // The group has to be bigger than 999
            assertTrue(integerDigitGroup > LOW_DIGIT_GROUP_BOUND, "The digit group should be grater than 999");
            // The group has to be smaller than 10.000
            assertTrue(integerDigitGroup < HIGH_DIGIT_GROUP_BOUND, "The digit group should be less than 10000");
        }
    }

    /**
     * Test for the CVV
     * */
    @Test
    public void cvvTest() {
        // Get a demo card
        final Card testCard = getDemoCard();
        // Get the card CVV
        final int cardCvv = testCard.getSecureCode();
        // Make sure the CVV has is bigger than 99
        assertTrue(cardCvv > LOW_CVV_BOUND, "The CVV should be greater than 99");
        // Make sure the CVV has is smaller than 1000
        assertTrue(cardCvv < HIGH_CVV_BOUND, "The CVV should be less than 1000");
    }

    /**
     * Test for the card expiry date
     * */
    @Test
    public void expiryDateTest() {
        // Get a demo card
        final Card testCard = getDemoCard();
        // Get the expiry date
        final String expiryDate = testCard.getExpiryDate();
        // Split de expiry date by slash
        final String[] monthAndYear = expiryDate.split("/");
        // Expiry date should be composed out of month and year
        assertEquals(2, monthAndYear.length, "The expiry date should contain only 2 fields: Month and Year. Divided by a slash.");
        // Convert the string month to an integer
        final int integerMonth = Integer.parseInt(monthAndYear[0]);
        // Make sure the month is not smaller than 1
        assertTrue(integerMonth > MONTH_LOW_BOUND, "The number of the month should be grater than 1");
        // Make sure the month is bigger than 12
        assertTrue(integerMonth < MONTH_HIGH_BOUND, "The number of the month should be less than 13");
        // Convert the string year to an integer
        final int integerYear = Integer.parseInt(monthAndYear[1]);
        // Get the last 2 digit of the current year
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
        // Make sure the year is bigger than the current year
        assertTrue(integerYear >= currentYear, "The number of the year should be grater than the current year");
        // Make sure the year is not grater than the current year plus 5
        assertTrue(integerYear <=  currentYear + 5, "The number of the year should be less than the current year plus 5");
    }

    /**
     * Test for the card pin code
     * */
    @Test
    public void pinCodeTest() {
        final CardCreateForm form = new CardCreateForm();
        form.setBankAccountIban("Demo iban");
        form.setCardHolderName("Demo User");
        form.setPinCode(DEFAULT_PIN_CODE);

        final Card testCard = new Card(form);

        assertTrue(testCard.checkPinCode(DEFAULT_PIN_CODE), "The instantiated card should have: " + DEFAULT_PIN_CODE + " as pin code");

        assertFalse(testCard.checkPinCode("asdasd"), "The pin code should differ from the random string inputted");

        assertFalse(testCard.checkPinCode("1234"), "The pin code should differ from the random string inputted");

        assertFalse(testCard.checkPinCode("4321"), "The pin code should differ from the random string inputted");
    }

    private Card getDemoCard() {
        // Return a demo card
        return new Card(getDemoForm());
    }

    private CardCreateForm getDemoForm() {
        final CardCreateForm form = new CardCreateForm();
        form.setPinCode("1234");
        form.setCardHolderName("Demo user");
        form.setBankAccountIban("DemoIban");
        return form;
    }

}