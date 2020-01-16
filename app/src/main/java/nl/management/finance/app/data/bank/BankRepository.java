package nl.management.finance.app.data.bank;

import javax.inject.Inject;

public class BankRepository {
    private static final String TAG = "BankRepository";
    private final BankDao bankDao;

    @Inject
    public BankRepository(BankDao bankDao) {
        this.bankDao = bankDao;
    }

    public Bank getBankByName(String name) {
        return bankDao.findByName(name);
    }
}
