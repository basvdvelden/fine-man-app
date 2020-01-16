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
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.bankaccount.BankAccount;
import nl.management.finance.app.data.bankaccount.BankAccountRepository;
import nl.management.finance.app.data.bank.BankRepository;
import nl.management.finance.app.data.user.Authentication;
import nl.management.finance.app.data.user.RegisterDto;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.userbank.UserBankRepository;
import nl.management.finance.app.data.user.UserRepository;
import nl.management.finance.app.data.bank.Bank;
import nl.management.finance.app.data.user.User;
import nl.management.finance.app.data.userbank.UserBank;
import nl.management.finance.app.ui.UIResult;

@Singleton
public class SetupViewModel extends ViewModel {
    private static final String TAG = "SetupViewModel";
    private String pin;
    private String bank;
    private String consentCode;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final UserBankRepository userBankRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserContext userContext;

    private MutableLiveData<UIResult<Void>> registerResult = new MutableLiveData<>();
    private boolean networkError = false;
    private State state = State.NOT_CONSENTED;

    private enum State {
        NOT_CONSENTED,
        CONSENTED;
    }

    @Inject
    public SetupViewModel(UserRepository userRepository, BankRepository bankRepository,
                          UserBankRepository userBankRepository, BankAccountRepository bankAccountRepository, UserContext userContext) {
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
        this.userBankRepository = userBankRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.userContext = userContext;
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

    public void register() {
        userRepository.setCurrentBank(this.bank);
        Completable registerComp = registerCompletable();
        Completable authenticateAtBankComp = authenticateAtBankCompletable();
        Completable getBankAccountsComp = getBankAccountsCompletable();

        Completable mergedComp = Completable.mergeArray(authenticateAtBankComp, registerComp);

        mergedComp.andThen(getBankAccountsComp).subscribe();
    }

    private Completable registerCompletable() {
        return Completable.fromAction(() -> {
            Log.d(TAG, "registering");
            RegisterDto dto = new RegisterDto();
            dto.setPin(pin);
            dto.setConsentCode(consentCode);
            dto.setBank(bank);

            Result<Void> result = userRepository.register(dto);
            if (result instanceof Result.Error) {
                networkError = true;
            }
            Log.d(TAG, "finished registering");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Completable authenticateAtBankCompletable() {
        return Completable.fromAction(() -> {
            Log.d(TAG, "authenticating at bank");
            Result<Authentication> result = userRepository.authenticateAtBank(consentCode);
            if (result instanceof Result.Success) {
                Authentication data = ((Result.Success<Authentication>) result).getData();
                userRepository.updateBankAuthInfo(data.getTokenType(), data.getExpiresAt(),
                        data.getAccessToken(), data.getRefreshToken());
            } else {
                networkError = true;
            }
            Log.d(TAG, "finished authenticating at bank");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Completable getBankAccountsCompletable() {
        return Completable.fromAction(() -> {
            Log.d(TAG, "getting bank accounts");
            Result<List<BankAccount>> result;
            if (!networkError) {
                result = bankAccountRepository.getBankAccountsRemotely();
            } else {
                result = new Result.Error(new Exception("there was a network error. fetching bank accounts skipped."));
            }
            this.handleGetBankAccountsResult(result);
            Log.d(TAG, "finished getting bank accounts");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void handleGetBankAccountsResult(Result<List<BankAccount>> result) {
        if (result instanceof Result.Error) {
            networkError = true;
            Log.e(TAG, "registration error:", ((Result.Error) result).getError());
        } else {
            userRepository.setHasUserRegisteredPin(true);
            List<BankAccount> data = ((Result.Success<List<BankAccount>>) result).getData();
            setupLocally(data);
            registerResult.postValue(new UIResult<>(null));
        }

    }

    private void setupLocally(List<BankAccount> bankAccounts) {
        User user = new User(userContext.getUserId().toString(), userContext.getUsername());
        Bank bank = bankRepository.getBankByName(this.bank);
        UserBank userBank = new UserBank(userContext.getUserId().toString(), bank.getId(), consentCode);

        userBankRepository.addUserBankLocally(userBank);
        userRepository.addUserLocally(user);
        bankAccountRepository.saveBankAccounts(bankAccounts);
    }

    public LiveData<UIResult<Void>> getRegisterResult() {
        return registerResult;
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
}
