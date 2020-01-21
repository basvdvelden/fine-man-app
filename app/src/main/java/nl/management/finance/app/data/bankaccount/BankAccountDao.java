package nl.management.finance.app.data.bankaccount;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import nl.management.finance.app.ui.overview.model.BankAccountView;

@Dao
public abstract class BankAccountDao {
    @Insert
    abstract void insertBankAccounts(List<BankAccount> bankAccounts);

    @Query("select name, iban, balance, currency from bank_account where user_id like :userId")
    abstract LiveData<List<BankAccountView>> getBankAccountsForUI(String userId);

    @Query("select * from bank_account where iban like :iban")
    abstract BankAccount getByIban(String iban);
}
