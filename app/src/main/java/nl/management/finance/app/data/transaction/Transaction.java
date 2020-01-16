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

    @ColumnInfo(name = "bank_account_resource_id")
    private String bankAccountResourceId;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "check_id", index = true)
    private String checkId;

    @NonNull
    @ColumnInfo(name = "booking_date")
    private String bookingDate;

    @ColumnInfo(name = "debtor_name")
    private String debtorName;

    @NonNull
    @ColumnInfo(name = "ultimate_debtor")
    private String ultimateDebtor;

    @ColumnInfo(name = "creditor_name")
    private String creditorName;

    @NonNull
    @ColumnInfo(name = "ultimate_creditor")
    private String ultimateCreditor;

    @NonNull
    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "initiating_party")
    private String initiatingParty;

    public Transaction(String bankAccountResourceId, @NonNull String type, @NonNull String checkId,
                       @NonNull String bookingDate, @NonNull String debtorName, @NonNull String ultimateDebtor,
                       String creditorName, @NonNull String ultimateCreditor, @NonNull String amount,
                       @NonNull String description, @NonNull String initiatingParty) {
        this.bankAccountResourceId = bankAccountResourceId;
        this.type = type;
        this.checkId = checkId;
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

    public String getBankAccountResourceId() {
        return bankAccountResourceId;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public String getCheckId() {
        return checkId;
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
}
