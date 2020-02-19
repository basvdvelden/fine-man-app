package nl.management.finance.app.data.contact;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import nl.management.finance.app.data.bank.Bank;

@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Contact> contacts);

    @Query("select * from contact where user_id like :userId order by name asc")
    LiveData<List<Contact>> getContacts(String userId);

    @Query("select * from contact where iban like :iban")
    Contact getByIban(String iban);

    @Delete
    void delete(Contact contact);
}
