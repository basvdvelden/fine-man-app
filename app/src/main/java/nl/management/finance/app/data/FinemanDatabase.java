package nl.management.finance.app.data;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.data.bank.Bank;
import nl.management.finance.app.data.bank.BankDao;
import nl.management.finance.app.data.bankaccount.BankAccount;
import nl.management.finance.app.data.bankaccount.BankAccountDao;
import nl.management.finance.app.data.transaction.Transaction;
import nl.management.finance.app.data.transaction.TransactionDao;
import nl.management.finance.app.data.user.User;
import nl.management.finance.app.data.user.UserDao;
import nl.management.finance.app.data.userbank.UserBank;
import nl.management.finance.app.data.userbank.UserBankDao;

@Database(entities = {User.class, UserBank.class, Bank.class, BankAccount.class, Transaction.class}, version = 28)
public abstract class FinemanDatabase extends RoomDatabase {
    private static final String DB_NAME = "fineman_db";
    private static FinemanDatabase instance;

    public static synchronized FinemanDatabase getInstance(Context context) {
        if (instance == null) {
            instance = buildDatabase(context);
        }
        return instance;
    }

    private static FinemanDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), FinemanDatabase.class, DB_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            Log.e("FinemanDatabase", "ON CREATE");

                            getInstance(context).bankDao().insertAll(Bank.populationData());
                        });
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            Log.e("FinemanDatabase", "ON OPEN " + getInstance(context).bankDao().findByName(BuildConfig.RABO_BANK_NAME));
                            if (getInstance(context).bankDao().findByName(BuildConfig.RABO_BANK_NAME) == null) {
                                getInstance(context).bankDao().insertAll(Bank.populationData());
                            }
                        });
                    }
                })
                .fallbackToDestructiveMigration().build();
    }

    public abstract BankDao bankDao();

    public abstract UserDao userDao();

    public abstract UserBankDao userBankDao();

    public abstract BankAccountDao bankAccountDao();

    public abstract TransactionDao transactionDao();
}
