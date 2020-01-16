package nl.management.finance.app.data.transaction;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TransactionDao {
    @Insert
    void insertTransactions(List<Transaction> transactions);

    @Query("select * from `transaction` where bank_account_resource_id like :bankAccountId")
    List<Transaction> getByBankAccountId(String bankAccountId);

    @Query("delete from `transaction` where 1=1")
    void deleteAll();
}
