package ro.sebastianrapa.atmapp.service;

import org.springframework.stereotype.Service;
import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.Transaction;

@Service
public class ATMServiceImpl implements ATMService {

    @Override
    public synchronized void deposit(BankAccount account, long depositAmount) {
        account.deposit(depositAmount);
        Transaction transaction = new Transaction("Deposited: " + depositAmount);
        account.addTransaction(transaction);
    }

    @Override
    public synchronized void withdraw(BankAccount account, long withdrawAmount) {
        account.withdraw(withdrawAmount);
        Transaction transaction = new Transaction("Withdrawn: " + withdrawAmount);
        account.addTransaction(transaction);
    }

    @Override
    public long queryBalance(BankAccount account) {
        return 0;
    }
}
