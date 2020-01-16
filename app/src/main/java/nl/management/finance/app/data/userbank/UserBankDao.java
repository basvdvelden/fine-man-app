package nl.management.finance.app.data.userbank;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserBankDao {
    @Insert
    void insertUserBank(UserBank userBank);

    @Query("select * from user_bank where user_id like :userId")
    UserBank getByUserId(String userId);
}
