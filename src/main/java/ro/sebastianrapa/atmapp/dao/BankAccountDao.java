package ro.sebastianrapa.atmapp.dao;

import ro.sebastianrapa.atmapp.model.BankAccount;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BankAccountDao {

    List<BankAccount> fetchAllBankAccounts();

    Optional<BankAccount> getBankAccountByIban(String bankAccountIban);

    Set<Currency> fetchCurrencies();

    void saveBankAccount(BankAccount bankAccount);

    boolean hasBankAccount(String holderName);

}
