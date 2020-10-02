package ro.sebastianrapa.atmapp.form;

public class CardCreateForm {

    private String bankAccountIban;
    private String cardHolderName;
    private String pinCode;
    private String retypedPinCode;

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getRetypedPinCode() {
        return retypedPinCode;
    }

    public void setRetypedPinCode(String retypedPinCode) {
        this.retypedPinCode = retypedPinCode;
    }

    public String getBankAccountIban() {
        return bankAccountIban;
    }

    public void setBankAccountIban(String bankAccountIban) {
        this.bankAccountIban = bankAccountIban;
    }

    @Override
    public String toString() {
        return "CardCreateForm{" +
                "bankAccountIban='" + bankAccountIban + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", retypedPinCode='" + retypedPinCode + '\'' +
                '}';
    }
}
