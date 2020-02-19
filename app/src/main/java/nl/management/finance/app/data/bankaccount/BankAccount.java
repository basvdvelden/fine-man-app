package nl.management.finance.app.data.bankaccount;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import nl.management.finance.app.data.user.User;

@Entity(tableName = "bank_account", indices = @Index(value = {"iban", "resource_id"}, unique = true))
public class BankAccount {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "bank_id")
    private int bankId;

    // TODO: Use foreign key annotation.
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
    @ColumnInfo(name = "resource_id")
    private String resourceId;

    public BankAccount(@NonNull String id, int bankId, @NonNull String userId, @NonNull String name,
                       @NonNull String currency, @NonNull String iban,
                       @NonNull Double balance, @NonNull String resourceId) {
        this.id = id;
        this.bankId = bankId;
        this.userId = userId;
        this.name = name;
        this.currency = currency;
        this.iban = iban;
        this.balance = balance;
        this.resourceId = resourceId;
    }

    @Ignore
    public BankAccount(int bankId, @NonNull String userId, @NonNull String name,
                       @NonNull String currency, @NonNull String iban,
                       @NonNull Double balance, @NonNull String resourceId) {
        this.id = UUID.randomUUID().toString();
        this.bankId = bankId;
        this.userId = userId;
        this.name = name;
        this.currency = currency;
        this.iban = iban;
        this.balance = balance;
        this.resourceId = resourceId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    @NonNull
    public Double getBalance() {
        return balance;
    }
}
