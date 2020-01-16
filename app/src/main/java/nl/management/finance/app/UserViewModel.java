package nl.management.finance.app;

import android.os.AsyncTask;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import nl.authentication.management.app.data.login.FineAuthenticationFailedException;
import nl.authentication.management.app.data.login.LoggedInUser;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.user.UserRepository;
import nl.management.finance.app.data.userbank.UserBankRepository;
import nl.management.finance.app.ui.UIResult;

@Singleton
public class UserViewModel extends ViewModel {
    private static final String TAG = "UserViewModel";

    private UserRepository userRepository;
    private UserBankRepository userBankRepository;
    private MutableLiveData<UIResult<Boolean>> hasRegisteredPin = new MutableLiveData<>();

    @Inject
    UserViewModel(UserRepository userRepository, UserBankRepository userBankRepository) {
        this.userRepository = userRepository;
        this.userBankRepository = userBankRepository;
    }

    public LiveData<UIResult<Boolean>> hasRegisteredPin() {
        return hasRegisteredPin;
    }

    void hasUserRegisteredPin() {
        AuthTask task = new AuthTask();
        task.execute();
    }

    public void logout() {
        this.hasRegisteredPin = new MutableLiveData<>();
        userRepository.logout();
    }

    private void handleHasRegisteredPinResult(Result<Boolean> result) {
        if (result instanceof Result.Success) {
            Boolean hasRegisteredPin = ((Result.Success<Boolean>) result).getData();
            this.hasRegisteredPin.setValue(new UIResult<>(hasRegisteredPin));
        } else {
            FineAuthenticationFailedException error = (FineAuthenticationFailedException)
                    ((Result.Error) result).getError();
            Log.w(TAG, "hasRegisteredPin error result: ", error);

            hasRegisteredPin.setValue(new UIResult<>(R.string.server_error));
        }
    }

    void deletePinFromMemory() {
        userRepository.deletePinFromMemory();
    }

    public void setCurrentUser(LoggedInUser user) {
        this.userRepository.setCurrentUser(user);
        this.userBankRepository.setCurrentBank(user.getUserId().toString());
    }

    private final class AuthTask extends AsyncTask<Void, Void, Result<Boolean>> {
        @Override
        protected Result<Boolean> doInBackground(Void... voids) {
            Log.i(TAG, "fine authentication started...");
            return userRepository.hasUserRegisteredPin();
        }


        @Override
        protected void onPostExecute(Result<Boolean> result) {
            Log.i(TAG, "fine authentication ended...");
            handleHasRegisteredPinResult(result);
        }
    }
}
