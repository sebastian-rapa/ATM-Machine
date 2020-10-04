package ro.sebastianrapa.atmapp.model;

import ro.sebastianrapa.atmapp.form.BankAccountCreateForm;
import ro.sebastianrapa.atmapp.model.exception.runtime.NotSufficientFundsException;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BankAccount {

    private static final long INITIAL_BALANCE = 0L;
    private static final int IBAN_LENGTH = 24;
    private static final String IBAN_PREFIX = "IBAN";
    private static final String SPACE = " ";

    private final String iban;
    private final String holderName;
    private final Currency currency;
    private long balance;
    private List<Transaction> transactions;

    public BankAccount(final BankAccountCreateForm form) {
        holderName = form.getBankAccountHolderName();
        currency = form.getCurrency();
        iban = generateIban();
        balance = INITIAL_BALANCE;
    }

    public String getIban() {
        return iban;
    }

    public String getHolderName() {
        return holderName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        if (null == transactions) {
            transactions = new ArrayList<>();
        }

        transactions.add(transaction);
    }

    public void deposit(final long amountToDeposit) {
        balance += amountToDeposit;
    }

    public long withdraw(final long amountToWithdraw) throws NotSufficientFundsException {
        // Make sure the balance is sufficient to serve the required amount to withdraw
        if (balance < amountToWithdraw) {
            throw new NotSufficientFundsException("Not sufficient funds");
        }
        // Subtract from the balance the withdrawn amount
        balance -= amountToWithdraw;
        // Return the required amount to withdraw
        return amountToWithdraw;
    }

    private String generateIban() {
        final StringBuilder ibanBuilder = new StringBuilder(IBAN_PREFIX);

        int randomDigit;

        for (int i = 0; i < IBAN_LENGTH; i++) {
            randomDigit = ThreadLocalRandom.current().nextInt(0, 9);

            ibanBuilder.append(randomDigit);

            if ((i + 1) % 4 == 0) {
                ibanBuilder.append(SPACE);
            }
        }

        return ibanBuilder.toString();
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "iban='" + iban + '\'' +
                ", holderName='" + holderName + '\'' +
                ", currency=" + currency +
                ", balance=" + balance +
                '}';
    }
}
