package nl.management.finance.app.data.user;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import nl.management.finance.app.data.user.User;
import nl.management.finance.app.data.bankaccount.BankAccount;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("select * from bank_account where user_id like :userId")
    List<BankAccount> getBankAccounts(String userId);

    @Query("select * from user where user_id like :userId")
    User getUser(String userId);
}
