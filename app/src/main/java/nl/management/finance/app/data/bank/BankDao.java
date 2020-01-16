package nl.management.finance.app.data.bank;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BankDao {
    @Insert
    void insertAll(Bank... banks);

    @Query("select * from bank where name like :name")
    Bank findByName(String name);
}
