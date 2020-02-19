package nl.management.finance.app.data.transaction;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "bank_account_id")
    private String bankAccountId;

    @ColumnInfo(name = "check_id")
    private String checkId;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    @NonNull
    @ColumnInfo(name = "bookingDate")
    private String bookingDate;

    @ColumnInfo(name = "debtorName")
    private String debtorName;

    @NonNull
    @ColumnInfo(name = "ultimateDebtor")
    private String ultimateDebtor;

    @ColumnInfo(name = "creditorName")
    private String creditorName;

    @NonNull
    @ColumnInfo(name = "ultimateCreditor")
    private String ultimateCreditor;

    @NonNull
    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "initiatingParty")
    private String initiatingParty;

    public Transaction(String bankAccountId, String checkId, @NonNull String type,
                       @NonNull String bookingDate, @NonNull String debtorName, @NonNull String ultimateDebtor,
                       String creditorName, @NonNull String ultimateCreditor, @NonNull String amount,
                       @NonNull String description, @NonNull String initiatingParty) {
        this.bankAccountId = bankAccountId;
        this.checkId = checkId;
        this.type = type;
        this.bookingDate = bookingDate;
        this.debtorName = debtorName;
        this.ultimateDebtor = ultimateDebtor;
        this.creditorName = creditorName;
        this.ultimateCreditor = ultimateCreditor;
        this.amount = amount;
        this.description = description;
        this.initiatingParty = initiatingParty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getBookingDate() {
        return bookingDate;
    }

    public String getDebtorName() {
        return debtorName;
    }

    @NonNull
    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    public String getInitiatingParty() {
        return initiatingParty;
    }

    @NonNull
    public String getUltimateDebtor() {
        return ultimateDebtor;
    }

    @NonNull
    public String getUltimateCreditor() {
        return ultimateCreditor;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public String getCheckId() {
        return checkId;
    }
}
