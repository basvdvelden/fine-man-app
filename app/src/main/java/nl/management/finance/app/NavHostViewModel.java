package nl.management.finance.app;

import android.util.Log;

import java.util.Objects;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import nl.management.finance.app.data.storage.AppSharedPreferences;

public class NavHostViewModel extends ViewModel {
    private static final String TAG = "NavHostViewModel";
    private final AppSharedPreferences sharedPrefs;

    private MutableLiveData<Boolean> verifyPin = new MutableLiveData<>();

    @Inject
    public NavHostViewModel(AppSharedPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
        verifyPin.setValue(sharedPrefs.getVerifyPin());
    }

    public LiveData<Boolean> getVerifyPin() {
        return verifyPin;
    }

    public void setVerifyPin(Boolean verifyPin) {
        sharedPrefs.setVerifyPin(verifyPin);
        this.verifyPin.setValue(verifyPin);
    }

    @Override
    protected void onCleared() {
        sharedPrefs.setVerifyPin(Objects.requireNonNull(verifyPin.getValue()));
        Log.d(TAG, "onCleared " + sharedPrefs.getVerifyPin().toString());
    }
}
