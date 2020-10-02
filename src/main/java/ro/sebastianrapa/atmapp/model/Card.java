package ro.sebastianrapa.atmapp.model;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ro.sebastianrapa.atmapp.form.CardCreateForm;
import ro.sebastianrapa.atmapp.form.LinkCardToAccountForm;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that models a Credit or a Debit Card
 * */
public class Card {

    /**
     * Object used to compare an introduced pin code with the hashed pin code
     * */
    private static final BCryptPasswordEncoder PIN_CODE_ENCODER = new BCryptPasswordEncoder();

    /**
     * Lower bound for a CVV
     * */
    private static final int CVV_LOW_BOUND = 100;
    /**
     * Higher bound for a CVV
     * */
    private static final int CVV_HIGH_BOUND = 999;
    /**
     * Lower bound for a Digit
     * */
    private static final int DIGIT_LOW_BOUND = 1;
    /**
     * Higher bound for a Digit
     * */
    private static final int DIGIT_HIGH_BOUND = 10;
    /**
     * Lower bound for a Month
     * */
    private static final int MONTH_LOW_BOUND = 1;
    /**
     * Higher bound for a Month
     * */
    private static final int MONTH_HIGH_BOUND = 13;
    /**
     * Integer representing the length of a card number
     * */
    private static final int CARD_NUM_LENGTH = 16;

    /**
     * Space character needed to be added in card numbers
     * */
    private static final String SPACE = " ";
    /**
     * Slash character needed to be added on the expiry date
     * */
    private static final String SLASH = "/";
    /**
     * Zero character needed to be added on the month field
     * */
    private static final String ZERO = "0";

    /**
     * This is the name of the card holder
     * */
    private final String cardHolderName;
    /**
     * This is the 16 digit card number
     * */
    private final String cardNumber;
    /**
     * This is the expiry date
     * */
    private final String expiryDate;
    /**
     * Bank account iban that each instance of a card is associated to
     * */
    private final String bankAccountIban;
    /**
     * This is the CVV, the last 3 digit on the back of the card
     * */
    private final int secureCode;
    /**
     * This is the pin code of the card wich is hased
     * */
    private final String pinCode;

    /**
     * Constructor for a card using a form Object
     * */
    public Card(final CardCreateForm form) {
        this.bankAccountIban = form.getBankAccountIban();
        this.cardHolderName = form.getCardHolderName();
        this.pinCode = hashPinCode(form.getPinCode());
        this.cardNumber = generateCardNumber();
        this.expiryDate = generateExpiryDate();
        this.secureCode = generateSecureCode();
    }

    /**
     * Constructor for a card using a form Object
     * */
    public Card(final LinkCardToAccountForm form) {
        this.bankAccountIban = form.getBankAccountIban();
        this.cardHolderName = form.getCardHolderName();
        this.pinCode = hashPinCode(form.getPinCode());
        this.cardNumber = generateCardNumber();
        this.expiryDate = generateExpiryDate();
        this.secureCode = generateSecureCode();
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public int getSecureCode() {
        return secureCode;
    }

    public String getBankAccountIban() {
        return bankAccountIban;
    }

    private String hashPinCode(final String clearPinCode) {
        final String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(clearPinCode, salt);
    }

    /**
     * Method that compares an introduced pin code with the hashed pin code
     * */
    public boolean checkPinCode(final String pinCode) {
        return PIN_CODE_ENCODER.matches(pinCode, this.pinCode);
    }

    private String generateCardNumber() {
        // Create a card number
        final StringBuilder cardNumber = new StringBuilder();
        // This will be a digit one at a time
        String digit;
        // Logic for generating a card number
        for (int i = 0; i < CARD_NUM_LENGTH; i++) {
            // Get a random digit
            digit = getRandomNumberAsString(DIGIT_HIGH_BOUND, DIGIT_LOW_BOUND);
            // Append the string to the card number
            cardNumber.append(digit);
            // Add a space after each 4 digit group
            if ((i + 1) % 4 == 0) {
                cardNumber.append(SPACE);
            }
        }
        return cardNumber.toString();
    }

    private static String getRandomNumberAsString(final int maximum, final int minimum){
        // Generate a random digit
        return Integer.toString(ThreadLocalRandom.current().nextInt(minimum, maximum));
    }

    private static String generateExpiryDate() {
        // Logic for generating a card's expiry date
        final StringBuilder expiryDate = new StringBuilder();
        // Generate a random month
        final String randomMonth = getRandomNumberAsString(MONTH_HIGH_BOUND, MONTH_LOW_BOUND);
        // Get the integer value of the random month
        final int intValueOfMonth = Integer.parseInt(randomMonth);
        // If the random value is a one digit month append a 0 to the expiry date
        if (intValueOfMonth / 10 == 0) {
            expiryDate.append(ZERO);
        }
        // Get current year
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
        // Generate a random year
        final String randomYear = getRandomNumberAsString(currentYear + 5, currentYear);
        // Append the month a slash and the year
        expiryDate.append(randomMonth).append(SLASH).append(randomYear);
        // Return the expiry date as a string
        return expiryDate.toString();
    }

    private static int generateSecureCode() {
        return Integer.parseInt(getRandomNumberAsString(CVV_HIGH_BOUND, CVV_LOW_BOUND));
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardHolderName='" + cardHolderName + '\'' +
                "cardHolderName='" + cardHolderName + '\'' +
                " bankAccountIban='" + bankAccountIban + '\'' +
                " cardNumber='" + cardNumber + '\'' +
                " expiryDate='" + expiryDate + '\'' +
                " secureCode=" + secureCode +
                " pinCode='" + pinCode + '\'' +
                '}';
    }
}
