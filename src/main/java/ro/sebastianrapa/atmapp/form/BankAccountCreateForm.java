package ro.sebastianrapa.atmapp.form;

import java.util.Currency;

public class BankAccountCreateForm {

    private String bankAccountHolderName;
    private Currency currency;

    public String getBankAccountHolderName() {
        return bankAccountHolderName;
    }

    public void setBankAccountHolderName(String bankAccountHolderName) {
        this.bankAccountHolderName = bankAccountHolderName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "BankAccountCreateForm{" +
                "cardHolderName='" + bankAccountHolderName + '\'' +
                ", currency=" + currency +
                '}';
    }
}
