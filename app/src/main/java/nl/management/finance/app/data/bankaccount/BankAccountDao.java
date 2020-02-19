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

@Dao
public abstract class BankAccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract List<Long> insertBankAccounts(List<BankAccount> bankAccounts);

    @Update
    abstract int updateBankAccounts(List<BankAccount> bankAccounts);

    @Query("select * from bank_account where user_id like :userId")
    abstract LiveData<List<BankAccount>> getBankAccounts(String userId);

    @Query("select * from bank_account where iban like :iban")
    abstract BankAccount getByIban(String iban);

    // TODO: Remove this method.
    @Query("delete from `bank_account` where 1=1")
    abstract void deleteAll();

    @Transaction
    public List<BankAccount> upsertBankAccounts(List<BankAccount> bankAccounts) {
        List<Long> insertResult = insertBankAccounts(bankAccounts);
        List<BankAccount> updateList = new ArrayList<>();
        List<Integer> insertedBankAccountsIndices = new ArrayList<>();

        for (int i = 0; i < insertResult.size(); i++) {
            if (insertResult.get(i) == -1) {
                updateList.add(bankAccounts.get(i));
            } else {
                insertedBankAccountsIndices.add(i);
            }
        }
        if (!updateList.isEmpty()) {
            int updateResult = updateBankAccounts(bankAccounts);
            if (updateResult == 0 && updateList.size() == bankAccounts.size()) {
                // Nothing was inserted or updated.
                return new ArrayList<>();
            } else if (updateResult == 0) {
                // Nothing was updated so we return only the inserted bank accounts.
                List<BankAccount> result = new ArrayList<>();
                for (Integer index: insertedBankAccountsIndices) {
                    result.add(bankAccounts.get(index));
                }
                return result;
            }
        }

        // Either all were inserted or some were updated,
        // since we can't know which bank accounts were updated we return them all.
        return bankAccounts;
    }
}
