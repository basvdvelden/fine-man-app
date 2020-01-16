package nl.management.finance.app.data.userbank;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "user_bank", primaryKeys = {"user_id", "bank_id"})
public class UserBank {
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "bank_id")
    private int bankId;

    @NonNull
    @ColumnInfo(name = "consent_code")
    private String consentCode;

    public UserBank(@NonNull String userId, int bankId, @NonNull String consentCode) {
        this.userId = userId;
        this.bankId = bankId;
        this.consentCode = consentCode;
    }

    @NonNull
    public String getConsentCode() {
        return consentCode;
    }

    public int getBankId() {
        return bankId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }
}
