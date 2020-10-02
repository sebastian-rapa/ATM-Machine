package ro.sebastianrapa.atmapp.form;

import ro.sebastianrapa.atmapp.model.BankAccount;

public class LinkCardToAccountForm {

    private String bankAccountIban;
    private String cardHolderName;
    private String pinCode;
    private String retypedPinCode;


    public String getBankAccountIban() {
        return bankAccountIban;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public void setBankAccountIban(String bankAccountIban) {
        this.bankAccountIban = bankAccountIban;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getRetypedPinCode() {
        return retypedPinCode;
    }

    public void setRetypedPinCode(String retypedPinCode) {
        this.retypedPinCode = retypedPinCode;
    }

    @Override
    public String toString() {
        return "LinkCardToAccountForm{" +
                "bankAccountIban='" + bankAccountIban + '\'' +
                ", holderName='" + cardHolderName + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", retypedPinCode='" + retypedPinCode + '\'' +
                '}';
    }

    public void fillInAccountData(BankAccount account) {
        this.cardHolderName = account.getHolderName();
        this.bankAccountIban = account.getIban();
    }
}
