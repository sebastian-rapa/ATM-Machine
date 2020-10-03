package ro.sebastianrapa.atmapp.model.exception.runtime;

import java.util.concurrent.ThreadLocalRandom;
/**
 * Exception that occurs when a Card is not Found
 * */
public class CardNotFoundException extends RuntimeException {

    /**
     * Generate Random serial version uid so it's not calculated at runtime
     * */
    private static final long serialVersionUID = ThreadLocalRandom.current().nextLong();
    /**
     * Calling the Super Class Constructor
     * */
    public CardNotFoundException(final String message) {
        super(message);
    }
    /**
     * Calling the Super Class Constructor
     * */
    public CardNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
