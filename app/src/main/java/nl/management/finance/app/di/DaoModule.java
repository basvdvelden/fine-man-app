package nl.management.finance.app.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import nl.management.finance.app.data.bankaccount.BankAccountDao;
import nl.management.finance.app.data.bank.BankDao;
import nl.management.finance.app.data.FinemanDatabase;
import nl.management.finance.app.data.transaction.TransactionDao;
import nl.management.finance.app.data.userbank.UserBankDao;
import nl.management.finance.app.data.user.UserDao;

@Module
public class DaoModule {
    @Provides
    public UserDao provideUserDao(Application context) {
        return FinemanDatabase.getInstance(context).userDao();
    }

    @Provides
    public UserBankDao provideUserBankDao(Application context) {
        return FinemanDatabase.getInstance(context).userBankDao();
    }

    @Provides
    public BankDao provideBankDao(Application context) {
        return FinemanDatabase.getInstance(context).bankDao();
    }

    @Provides
    public BankAccountDao provideBankAccountDao(Application context) {
        return FinemanDatabase.getInstance(context).bankAccountDao();
    }

    @Provides
    public TransactionDao provideTransactionDao(Application context) {
        return FinemanDatabase.getInstance(context).transactionDao();
    }
}
