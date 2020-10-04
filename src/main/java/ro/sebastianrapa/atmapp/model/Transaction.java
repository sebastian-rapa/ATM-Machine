package ro.sebastianrapa.atmapp.model;

import java.util.Date;

/**
 * Class modeling a bank Transaction
 * */
public class Transaction {

    /**
     * Field that will hold the details of the transaction
     * */
    private final String details;
    /**
     * Field that will hold the date when the transaction happened
     * */
    private final Date createdAt;

    /**
     * Constructor requiring the details of the transaction
     * */
    public Transaction(final String details) {
        this.details = details;
        this.createdAt = new Date();
    }

    public String getDetails() {
        return details;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "details='" + details + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
