package ro.sebastianrapa.atmapp.service;

import org.springframework.stereotype.Service;
import ro.sebastianrapa.atmapp.dao.BankAccountDao;
import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.exception.runtime.BankAccountNotFoundException;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private transient final BankAccountDao bankAccountDao;

    public BankAccountServiceImpl(BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
    }

    @Override
    public void saveBankAccount(BankAccount bankAccount) {
        bankAccountDao.saveBankAccount(bankAccount);


    }

    @Override
    public BankAccount getBankAccountByIban(String bankAccountIban) throws BankAccountNotFoundException {
        return bankAccountDao.getBankAccountByIban(bankAccountIban)
                             .orElseThrow(() -> new BankAccountNotFoundException("Bank account was not found"));
    }

    @Override
    public List<BankAccount> fetchAllBankAccounts() {
        return bankAccountDao.fetchAllBankAccounts();
    }

    @Override
    public Set<Currency> fetchCurrencies() {
        return bankAccountDao.fetchCurrencies();
    }

    @Override
    public boolean hasBankAccount(String holderName) {
        return bankAccountDao.hasBankAccount(holderName);
    }
}
