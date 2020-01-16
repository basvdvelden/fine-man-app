package nl.management.finance.app.data.bankaccount;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BankAccountDao {
    @Insert
    void insertBankAccounts(List<BankAccount> bankAccounts);

    @Query("select * from bank_account where user_id like :userId")
    List<BankAccount> getBankAccounts(String userId);

    @Query("select * from bank_account where iban like :iban")
    BankAccount getByIban(String iban);
}
