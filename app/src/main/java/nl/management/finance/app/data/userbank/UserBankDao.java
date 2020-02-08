package nl.management.finance.app.data.userbank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import nl.management.finance.app.data.user.Authentication;
import nl.management.finance.app.data.user.User;

@Dao
public abstract class UserBankDao {
    @Insert
    abstract void insertUserBank(UserBank userBank);

    @Query("select * from user_bank where user_id like :userId and bank_id = :bankId")
    public abstract UserBank getByUserIdAndBankId(String userId, int bankId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract List<Long> insertUserBanks(List<UserBank> userBanks);

    @Update
    abstract void updateUserBanks(List<UserBank> userBanks);

    @Update
    public abstract void updateUserBank(UserBank userBank);

    @Transaction
    public void upsertUserBanks(List<UserBank> userBanks) {
        List<Long> insertResult = insertUserBanks(userBanks);
        List<UserBank> updateList = new ArrayList<>();

        for (int i = 0; i < insertResult.size(); i++) {
            if (insertResult.get(i) == -1) {
                updateList.add(userBanks.get(i));
            }
        }
        if (!updateList.isEmpty()) {
            updateUserBanks(updateList);
        }
    }

    @Query("select access_token, expires_at, refresh_token, token_type from user_bank " +
            "where user_id like :userId and bank_id = :bankId")
    public abstract Authentication getAuthByUserIdAndBankId(String userId, int bankId);
}
