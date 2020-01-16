package nl.management.finance.app.ui.pin;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import nl.authentication.management.app.data.login.PinVerificationFailedException;
import nl.management.finance.app.R;

import javax.inject.Inject;

import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.user.UserRepository;
import nl.management.finance.app.data.user.WrongPinCodeException;
import nl.management.finance.app.data.PinCode;
import nl.management.finance.app.ui.UIResult;

public class VerifyPinViewModel extends PinViewModel {
    private static final String TAG = "VerifyPinViewModel";

    private final UserRepository userRepository;

    private MutableLiveData<UIResult<Void>> pinVerificationResult = new MutableLiveData<>();

    @Inject
    VerifyPinViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentPin = new PinCode();
    }

    void initial() {
        this.pinVerificationResult = new MutableLiveData<>();
        this.currentPin.reset();
    }

    LiveData<UIResult<Void>> getPinVerificationResult() { return pinVerificationResult; }

    void verifyPin() {
        PinVerificationTask task = new PinVerificationTask();
        task.execute();
    }

    void resetPin() {
        currentPin.reset();
    }

    private void handlePinVerificationResult(Result<Void> result) {
        Log.i(TAG, "handling pin verification result...");
        if (result instanceof Result.Success) {
            userRepository.cachePin(currentPin.getValue());
            pinVerificationResult.setValue(new UIResult<>(null));
        } else {
            Log.d(TAG, "pin verification failed");
            PinVerificationFailedException error = (PinVerificationFailedException)
                    ((Result.Error) result).getError();
            Throwable cause = error.getCause();

            if (cause instanceof WrongPinCodeException) {
                pinVerificationResult.setValue(new UIResult<>(R.string.wrong_pin_code));
            } else {
                pinVerificationResult.setValue(new UIResult<>(R.string.server_error));
            }
        }
    }

    private final class PinVerificationTask extends AsyncTask<Void, Void, Result<Void>> {
        @Override
        protected Result<Void> doInBackground(Void... voids) {
            Log.i(TAG, "pin verification started...");
            return userRepository.verifyPin(currentPin.getValue());
        }


        @Override
        protected void onPostExecute(Result<Void> result) {
            Log.i(TAG, "pin verification ended...");
            handlePinVerificationResult(result);
        }
    }

}
