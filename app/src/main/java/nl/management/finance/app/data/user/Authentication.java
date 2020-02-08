package nl.management.finance.app.data.user;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class Authentication {
    @ColumnInfo(name = "access_token")
    @SerializedName("access_token")
    private String accessToken;

    @Ignore
    @SerializedName("expires_in")
    private Long expiresIn;

    @ColumnInfo(name = "expires_at")
    @SerializedName("expires_at")
    private Long expiresAt;

    @ColumnInfo(name = "refresh_token")
    @SerializedName("refresh_token")
    private String refreshToken;

    @Ignore
    private String scope;

    @ColumnInfo(name = "token_type")
    @SerializedName("token_type")
    private String tokenType;

    public Authentication() {}

    public Authentication(String accessToken, Long expiresAt, String refreshToken, String tokenType) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return this.expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format(Locale.getDefault(),
                "[accessToken=%s, expiresAt=%d, refreshToken=%s, scope=%s, tokenType=%s]",
                accessToken, expiresAt, refreshToken, scope, tokenType);
    }
}
