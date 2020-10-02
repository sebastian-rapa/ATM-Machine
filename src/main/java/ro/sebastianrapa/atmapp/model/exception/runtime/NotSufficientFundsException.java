package ro.sebastianrapa.atmapp.model.exception.runtime;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Exception that occurs when a Bank Account has Insufficient Funds
 * */
public class NotSufficientFundsException extends RuntimeException {
    /**
     * Generate Random serial version uid so it's not calculated at runtime
     * */
    private static final long serialVersionUID = ThreadLocalRandom.current().nextLong();
    /**
     * Calling the Super Class Constructor
     * */
    public NotSufficientFundsException(final String message) {
        super(message);
    }

    /**
     * Calling the Super Class Constructor
     * */
    public NotSufficientFundsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
