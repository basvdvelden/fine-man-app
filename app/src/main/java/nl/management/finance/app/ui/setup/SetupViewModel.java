package nl.management.finance.app.ui.setup;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.authentication.management.app.AppOptional;
import nl.management.finance.app.R;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserSubject;
import nl.management.finance.app.data.bank.BankRepository;
import nl.management.finance.app.data.user.Authentication;
import nl.management.finance.app.data.user.RegisterDto;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.userbank.UserBankDto;
import nl.management.finance.app.data.userbank.UserBankRepository;
import nl.management.finance.app.data.user.UserRepository;
import nl.management.finance.app.data.bank.Bank;
import nl.management.finance.app.data.user.User;
import nl.management.finance.app.data.userbank.UserBank;
import nl.management.finance.app.ui.UIResult;

@Singleton
public class SetupViewModel extends ViewModel {
    private static final String TAG = "SetupViewModel";
    private static final String SCOPES = "ais.balances.read ais.transactions.read-90days ais.transactions.read-history";

    private String pin;
    private String bank;
    private String consentCode;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final UserBankRepository userBankRepository;
    private final UserContext userContext;
    private final UserSubject userSubject;

    private MutableLiveData<UIResult<Void>> setupNewUserResult = new MutableLiveData<>();
    private MutableLiveData<UIResult<Void>> setupExistingUserResult = new MutableLiveData<>();
    private State state = State.NOT_CONSENTED;

    public MutableLiveData<UIResult<Void>> getSetupExistingUserResult() {
        return setupExistingUserResult;
    }

    public String getScopes() {
        return SCOPES;
    }

    private enum State {
        NOT_CONSENTED,
        CONSENTED;
    }

    @Inject
    public SetupViewModel(UserRepository userRepository, BankRepository bankRepository,
                          UserBankRepository userBankRepository, UserContext userContext, UserSubject userSubject) {
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
        this.userBankRepository = userBankRepository;
        this.userContext = userContext;
        this.userSubject = userSubject;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setupNewUser() {
        bankRepository.setCurrentBank(this.bank, true);
        Observable<AppOptional<Authentication>> authenticateAtBankComp = authenticateAtBankCompletable();

        authenticateAtBankComp.subscribe(authentication -> {
            if (authentication.get() != null) {
                registerCompletable(authentication.get()).subscribe();
            }
        });
    }

    public void setupExistingUser() {
        Completable.fromAction(() -> {
            Result<User> userResult = userRepository.getUserRemotely();
            Result<List<UserBank>> userBanksResult = userBankRepository.getUserBanksRemotely();
            if (userResult instanceof Result.Success && userBanksResult instanceof Result.Success) {
                User user = ((Result.Success<User>) userResult).getData();
                userRepository.createUser(user);

                List<UserBank> userBanks = ((Result.Success<List<UserBank>>) userBanksResult).getData();
                for (UserBank userBank: userBanks) {
                    userBankRepository.createUserBank(userBank, false);
                }
                UserBank ub = userBanks.get(0);
                userBankRepository.updateBankAuthInfo(ub.getTokenType(), ub.getExpiresAt(),
                        ub.getAccessToken(), ub.getRefreshToken(), false);
                bankRepository.setCurrentBank(ub.getBankId(), true);
                setupExistingUserResult.postValue(new UIResult<>(null));
            } else {
                setupExistingUserResult.postValue(new UIResult<>(R.string.server_error));
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    private Completable registerCompletable(Authentication auth) {
        return Completable.fromAction(() -> {
            Log.d(TAG, "registering");
            RegisterDto dto = new RegisterDto();
            dto.setPin(pin);
            Result<Void> result = userRepository.register(dto);

            if (result instanceof Result.Success) {
                User user = new User(userContext.getUserId().toString());
                userRepository.createUser(user);
                userRepository.setHasUserRegisteredPin(true);
                userSubject.setPin(pin);

                UserBank userBank = new UserBank();
                userBank.setUserId(userContext.getUserId().toString());
                userBank.setConsentCode(consentCode);
                userBank.setBankId(bankRepository.getBankId(this.bank));
                userBank.setAccessToken(auth.getAccessToken());
                userBank.setTokenType(auth.getTokenType());
                userBank.setExpiresAt(auth.getExpiresAt());
                userBank.setRefreshToken(auth.getRefreshToken());
                userBank.setScopes(SCOPES);
                userBankRepository.createUserBank(userBank, true);

                userBankRepository.updateBankAuthInfo(auth.getTokenType(), auth.getExpiresAt(),
                        auth.getAccessToken(), auth.getRefreshToken(), false);

                setupNewUserResult.postValue(new UIResult<>(null));
            } else {
                setupNewUserResult.postValue(new UIResult<>(R.string.server_error));
            }
            Log.d(TAG, "finished registering");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<AppOptional<Authentication>> authenticateAtBankCompletable() {
        return Observable.fromCallable(() -> {
            Log.d(TAG, "authenticating at bank");
            Result<Authentication> result = userRepository.authenticateAtBank(consentCode);
            Authentication data = null;
            if (result instanceof Result.Success) {
                data = ((Result.Success<Authentication>) result).getData();
                return new AppOptional<>(data);
            } else {
                setupNewUserResult.postValue(new UIResult<>(R.string.server_error));
                return new AppOptional<>(data);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public LiveData<UIResult<Void>> getSetupNewUserResult() {
        return setupNewUserResult;
    }

    public void restoreState(String state) {
        String[] parts = state.split(";");
        this.pin = parts[0];
        this.bank = parts[1];
    }

    public void setConsentCode(String consentCode) {
        this.consentCode = consentCode;
        this.state = State.CONSENTED;
    }

    public boolean hasConsented() {
        return this.state == State.CONSENTED;
    }

    @Override
    protected void onCleared() {
        Log.e(TAG, "on cleared setup view model");
    }
}
