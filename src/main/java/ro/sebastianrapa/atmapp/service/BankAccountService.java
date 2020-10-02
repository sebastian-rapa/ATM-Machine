package ro.sebastianrapa.atmapp.service;

import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.exception.runtime.BankAccountNotFoundException;

import java.util.Currency;
import java.util.List;
import java.util.Set;

public interface BankAccountService {

    List<BankAccount> fetchAllBankAccounts();

    BankAccount getBankAccountByIban(String bankAccountIban) throws BankAccountNotFoundException;

    Set<Currency> fetchCurrencies();

    void saveBankAccount(BankAccount bankAccount);

    boolean hasBankAccount(String holderName);

}
