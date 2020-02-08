package nl.management.finance.app.data.transaction;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import nl.management.finance.app.ui.transactions.TransactionView;

@Dao
public abstract class TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract List<Long> insertTransactions(List<Transaction> transactions);

    @Update
    abstract void updateTransactions(List<Transaction> transactions);

    @androidx.room.Transaction
    public void upsertTransactions(List<Transaction> transactions) {
        List<Long> insertResult = insertTransactions(transactions);
        List<Transaction> updateList = new ArrayList<>();

        for (int i = 0; i < insertResult.size(); i++) {
            if (insertResult.get(i) == -1) {
                updateList.add(transactions.get(i));
            }
        }
        if (!updateList.isEmpty()) {
            updateTransactions(updateList);
        }
    }

    @Query("select bookingDate, ultimateDebtor as debtorName, amount, description, initiatingParty, " +
            "ultimateCreditor as creditorName from `transaction` " +
            "where bankAccountId like :bankAccountId")
    abstract LiveData<List<TransactionView>> getByBankAccountId(String bankAccountId);

    @Query("delete from `transaction` where 1=1")
    abstract void deleteAll();
}
