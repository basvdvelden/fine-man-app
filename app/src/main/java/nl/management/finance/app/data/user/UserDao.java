package nl.management.finance.app.data.user;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import nl.management.finance.app.data.user.User;
import nl.management.finance.app.data.bankaccount.BankAccount;

@Dao
public abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract Long insertUser(User user);

    @Update
    abstract void updateUser(User user);

    @Query("select * from bank_account where user_id like :userId")
    abstract List<BankAccount> getBankAccounts(String userId);

    @Query("select * from user where user_id like :userId")
    abstract User getUser(String userId);

    @Transaction
    public void upsertUser(User user) {
        Long insertResult = insertUser(user);
        if (insertResult == -1) {
            updateUser(user);
        }
    }
}
