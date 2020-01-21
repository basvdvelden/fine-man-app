package nl.management.finance.app.data.storage;

import android.content.SharedPreferences;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppSharedPreferences {
    private final SharedPreferences sharedPrefs;

    private static class Keys {
        private static final String VERIFY_PIN = "verifyPin";
    }

    @Inject
    public AppSharedPreferences(@Named("fine") SharedPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

    public void setVerifyPin(@NonNull Boolean verifyPin) {
        this.sharedPrefs.edit().putBoolean(Keys.VERIFY_PIN, verifyPin).apply();
    }

    @NonNull
    public Boolean getVerifyPin() {
        return this.sharedPrefs.getBoolean(Keys.VERIFY_PIN, true);
    }
}
