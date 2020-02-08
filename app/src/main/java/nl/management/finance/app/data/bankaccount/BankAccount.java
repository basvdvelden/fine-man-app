package nl.management.finance.app.data.bankaccount;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "bank_account", indices = @Index(value = {"iban", "resourceId"}, unique = true))
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "bank_id")
    private int bankId;

    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "currency")
    private String currency;

    @NonNull
    @ColumnInfo(name = "iban")
    private String iban;

    @NonNull
    @ColumnInfo(name = "balance")
    private Double balance;

    @NonNull
    @ColumnInfo(name = "resourceId")
    private String resourceId;

    public BankAccount(int bankId, @NonNull String userId, @NonNull String name,
                       @NonNull String currency, @NonNull String iban,
                       @NonNull Double balance, @NonNull String resourceId) {
        this.bankId = bankId;
        this.userId = userId;
        this.name = name;
        this.currency = currency;
        this.iban = iban;
        this.balance = balance;
        this.resourceId = resourceId;
    }

    public int getId() {
        return id;
    }

    public int getBankId() {
        return bankId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getCurrency() {
        return currency;
    }

    @NonNull
    public String getIban() {
        return iban;
    }

    @NonNull
    public String getResourceId() {
        return resourceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public Double getBalance() {
        return balance;
    }
}
