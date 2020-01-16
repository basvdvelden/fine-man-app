package nl.management.finance.app.data.api.rabo;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import androidx.annotation.NonNull;

public class RaboOAuthResponse {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private Long expiresIn;
    @SerializedName("refresh_token")
    private String refreshToken;
    private String scope;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("refresh_token_expires_in")
    private Long refreshTokenExpiresIn;
    @SerializedName("consented_on")
    private Long consentedOn;

    public RaboOAuthResponse() {

    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setRefreshTokenExpiresIn(Long refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public Long getConsentedOn() {
        return consentedOn;
    }

    public void setConsentedOn(Long consentedOn) {
        this.consentedOn = consentedOn;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.forLanguageTag("nl"),
                "[accessToken=%s, expiresIn=%d, refreshToken=%s, scope=%s, tokenType=%s, refreshTokenExpiresIn=%d, consentedOn=%d]",
                accessToken, expiresIn, refreshToken, scope, tokenType, refreshTokenExpiresIn, consentedOn);
    }
}
