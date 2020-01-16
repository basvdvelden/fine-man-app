package nl.management.finance.app.data.user;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import androidx.annotation.NonNull;

public class Authentication {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private Long expiresAt;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("scope")
    private String scope;
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
