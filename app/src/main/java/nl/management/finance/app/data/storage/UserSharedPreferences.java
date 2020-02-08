package nl.management.finance.app.data.storage;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;

/**
 * {@link SharedPreferences} wrapper class for user information
 */
public class UserSharedPreferences {
    private final SharedPreferences sharedPrefs;

    private static class Keys {
        private static final String MOST_RECENT_BANK = "mostRecentBank";
        private static final String HAS_REGISTERED_PIN = "hasRegisteredPin";
        private static final String TOKEN_TYPE = "tokenType";
        private static final String ACCESS_TOKEN = "accessToken";
        private static final String REFRESH_TOKEN = "refreshToken";
        private static final String EXPIRES_AT = "expiresAt";
    }

    @Inject
    public UserSharedPreferences(@Named("fine") SharedPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

    public void setHasRegisteredPin(@NonNull Boolean hasRegisteredPin) {
        this.sharedPrefs.edit().putBoolean(Keys.HAS_REGISTERED_PIN, hasRegisteredPin).apply();
    }

    @NonNull
    public Boolean getHasRegisteredPin() {
        if (sharedPrefs.contains(Keys.HAS_REGISTERED_PIN)) {
            return sharedPrefs.getBoolean(Keys.HAS_REGISTERED_PIN, false);
        }
        throw new NullPointerException("hasRegisteredPin is null");
    }

    public void setTokenType(@NonNull String tokenType) {
        this.sharedPrefs.edit().putString(Keys.TOKEN_TYPE, tokenType).apply();
    }

    public String getTokenType() {
        if (sharedPrefs.contains(Keys.TOKEN_TYPE)) {
            return sharedPrefs.getString(Keys.TOKEN_TYPE, null);
        }
        throw new NullPointerException("token type is null");
    }

    public void setExpiresAt(Long expiresAt) {
        sharedPrefs.edit().putLong(Keys.EXPIRES_AT, expiresAt).apply();
    }

    public Long getExpiresAt() {
        if (sharedPrefs.contains(Keys.EXPIRES_AT)) {
            return sharedPrefs.getLong(Keys.EXPIRES_AT, 0L);
        }
        throw new NullPointerException("expires at is null");
    }

    public void setAccessToken(@NonNull String accessToken) {
        this.sharedPrefs.edit().putString(Keys.ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken() {
        if (sharedPrefs.contains(Keys.ACCESS_TOKEN)) {
            return sharedPrefs.getString(Keys.ACCESS_TOKEN, null);
        }
        throw new NullPointerException("access token is null");
    }

    public void setRefreshToken(@NonNull String refreshToken) {
        this.sharedPrefs.edit().putString(Keys.REFRESH_TOKEN, refreshToken).apply();
    }

    public String getRefreshToken() {
        if (sharedPrefs.contains(Keys.REFRESH_TOKEN)) {
            return sharedPrefs.getString(Keys.REFRESH_TOKEN, null);
        }
        throw new NullPointerException("refresh token is null");
    }

    public void setMostRecentBank(String bankName) {
        this.sharedPrefs.edit().putString(Keys.MOST_RECENT_BANK, bankName).apply();
    }

    public String getMostRecentBank() {
        if (sharedPrefs.contains(Keys.MOST_RECENT_BANK)) {
            return sharedPrefs.getString(Keys.MOST_RECENT_BANK, null);
        }
        throw new NullPointerException("most recent bank is null");
    }

    public void wipeHasRegisteredPin() {
        this.sharedPrefs.edit().remove(Keys.HAS_REGISTERED_PIN).apply();
    }
}
