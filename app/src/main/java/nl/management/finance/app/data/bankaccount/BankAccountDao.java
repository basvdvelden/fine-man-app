package nl.management.finance.app.data.bankaccount;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import nl.management.finance.app.ui.overview.BankAccountView;

@Dao
public abstract class BankAccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract List<Long> insertBankAccounts(List<BankAccount> bankAccounts);

    @Update
    abstract void updateBankAccounts(List<BankAccount> bankAccounts);

    @Query("select resourceId as id, name, iban, balance, currency from bank_account where user_id like :userId")
    abstract LiveData<List<BankAccountView>> getBankAccountsForUI(String userId);

    @Query("select * from bank_account where user_id like :userId")
    abstract List<BankAccount> getBankAccounts(String userId);

    @Query("select * from bank_account where iban like :iban")
    abstract BankAccount getByIban(String iban);

    @Transaction
    public void upsertBankAccounts(List<BankAccount> bankAccounts) {
        List<Long> insertResult = insertBankAccounts(bankAccounts);
        List<BankAccount> updateList = new ArrayList<>();

        for (int i = 0; i < insertResult.size(); i++) {
            if (insertResult.get(i) == -1) {
                updateList.add(bankAccounts.get(i));
            }
        }
        if (!updateList.isEmpty()) {
            updateBankAccounts(bankAccounts);
        }
    }
}
