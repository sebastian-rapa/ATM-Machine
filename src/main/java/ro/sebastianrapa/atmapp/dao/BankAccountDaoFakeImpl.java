package ro.sebastianrapa.atmapp.dao;

import org.springframework.stereotype.Repository;
import ro.sebastianrapa.atmapp.model.BankAccount;

import java.util.*;

@Repository
public class BankAccountDaoFakeImpl implements BankAccountDao {

    private final List<BankAccount> FAKE_BANK_ACCOUNT_DB = new ArrayList<>();

    @Override
    public void saveBankAccount(BankAccount bankAccount) {
        // Add the bank account to the Fake DB
        FAKE_BANK_ACCOUNT_DB.add(bankAccount);
    }

    @Override
    public Optional<BankAccount> getBankAccountByIban(String bankAccountIban) {
        return FAKE_BANK_ACCOUNT_DB.stream()
                                   .filter(account -> account.getIban().equals(bankAccountIban))
                                   .findFirst();
    }

    @Override
    public List<BankAccount> fetchAllBankAccounts() {
        return FAKE_BANK_ACCOUNT_DB;
    }

    @Override
    public Set<Currency> fetchCurrencies() {
        return Currency.getAvailableCurrencies();
    }

    @Override
    public boolean hasBankAccount(String holderName) {
        return FAKE_BANK_ACCOUNT_DB.stream()
                                   .anyMatch(account -> account.getHolderName().equals(holderName));
    }
}
