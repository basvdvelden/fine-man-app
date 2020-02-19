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
    @NonNull
    private int bankId;

    @NonNull
    @ColumnInfo(name = "token_type")
    private String tokenType;

    @NonNull
    @ColumnInfo(name = "access_token")
    private String accessToken;

    @NonNull
    @ColumnInfo(name = "expires_at")
    private Long expiresAt;

    @NonNull
    @ColumnInfo(name = "refresh_token")
    private String refreshToken;

    @NonNull
    @ColumnInfo(name = "consent_code")
    private String consentCode;

    @NonNull
    @ColumnInfo(name = "scopes")
    private String scopes;

    public UserBank() {}

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(@NonNull String tokenType) {
        this.tokenType = tokenType;
    }

    @NonNull
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(@NonNull String accessToken) {
        this.accessToken = accessToken;
    }

    @NonNull
    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(@NonNull Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    @NonNull
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(@NonNull String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @NonNull
    public String getConsentCode() {
        return consentCode;
    }

    public void setConsentCode(@NonNull String consentCode) {
        this.consentCode = consentCode;
    }

    @NonNull
    public String getScopes() {
        return scopes;
    }

    public void setScopes(@NonNull String scopes) {
        this.scopes = scopes;
    }
}
