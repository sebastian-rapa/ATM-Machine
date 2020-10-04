package ro.sebastianrapa.atmapp.service;

import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.Card;

public interface ATMService {

    void deposit(BankAccount account, long depositAmount);

    void withdraw(BankAccount account, long withdrawAmount);

    // Return the current balance in the bank account associated with the card
    long queryBalance(BankAccount account);
}
