package nl.management.finance.app.data.user;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.authentication.management.app.LoginNotifier;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserSubject;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.BankAuthNotifier;
import nl.management.finance.app.data.storage.UserCache;
import nl.management.finance.app.data.storage.UserSharedPreferences;
import nl.management.finance.app.data.userbank.UserBankRepository;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final UserDataSource userDataSource;
    private final UserCache userCache;
    private final UserSharedPreferences userSharedPrefs;
    private final UserContext userContext;
    private final UserDao userDao;
    private final UserBankRepository userBankRepository;
    private final BankAuthNotifier bankAuthNotifier;
    private final LoginNotifier loginNotifier;
    private final UserSubject userSubject;

    @Inject
    public UserRepository(UserDataSource userDataSource, UserCache userCache,
                          UserSharedPreferences userSharedPrefs, UserContext userContext,
                          UserDao userDao, UserBankRepository userBankRepository,
                          BankAuthNotifier bankAuthNotifier, LoginNotifier loginNotifier, UserSubject userSubject) {
        this.userDataSource = userDataSource;
        this.userCache = userCache;
        this.userSharedPrefs = userSharedPrefs;
        this.userContext = userContext;
        this.userDao = userDao;
        this.userBankRepository = userBankRepository;
        this.bankAuthNotifier = bankAuthNotifier;
        this.loginNotifier = loginNotifier;
        this.userSubject = userSubject;
        this.bankAuthNotifier.getUpdatedAuthentication().subscribe(authentication -> {
            updateBankAuthInfo(authentication.getTokenType(), authentication.getExpiresAt(),
                    authentication.getAccessToken(), authentication.getRefreshToken());
        });
        this.loginNotifier.getLoggedInSubject().subscribe(loggedInUser -> {
            if (loggedInUser != null) {
                Completable.fromAction(() -> {
                    User user = getUser();
                    this.userSubject.setUser(user);
                }).subscribeOn(Schedulers.io())
                .subscribe();
            }
        });
        this.userSubject.get().subscribe(user -> {
            if (user.get() != null) {
                this.bankAuthNotifier.updateBankAuthInfo(userSharedPrefs.getTokenType(),
                        userSharedPrefs.getExpiresAt(), userSharedPrefs.getAccessToken(),
                        userSharedPrefs.getRefreshToken());
            }
        });
        // TODO: weghalen
        this.userSharedPrefs.wipeHasRegisteredPin();
    }

    public void setHasUserRegisteredPin(Boolean hasUserRegisteredPin) {
        userCache.setHasRegisteredPin(hasUserRegisteredPin);
        userSharedPrefs.setHasRegisteredPin(hasUserRegisteredPin);
    }

    public Result<Boolean> hasUserRegisteredPin() {
        Boolean hasRegisteredPin = userCache.getHasRegisteredPin();
        if (hasRegisteredPin == null) {
            try {
                hasRegisteredPin = userSharedPrefs.getHasRegisteredPin();
            } catch (NullPointerException ignored) {
                Result<Boolean> result = userDataSource.hasUserRegisteredPin(userContext.getUserId());
                if (result instanceof Result.Success) {
                    hasRegisteredPin = ((Result.Success<Boolean>) result).getData();
                } else {
                    Log.e(TAG, "error hasRegistered result: ", ((Result.Error) result).getError());
                }
                return result;
            } finally {
                setHasUserRegisteredPin(hasRegisteredPin);
            }
        }

        return new Result.Success(hasRegisteredPin);
    }

    public String getPin() {
        return userCache.getPin();
    }

    public void deletePinFromMemory() {
        userCache.setPin(null);
    }

    private User getUser() {
        return userDao.getUser(userContext.getUserId().toString());
    }

    public void setCurrentBank(String bankName) {
        this.userContext.setBank(bankName);
    }

    public void logout() {
        this.userCache.setHasRegisteredPin(null);
        userSharedPrefs.wipeHasRegisteredPin();
    }

    public Result<Void> verifyPin(String pin) {
        return userDataSource.verifyPin(pin, userContext.getUserId());
    }

    public Result<Void> register(RegisterDto registerDto) {
        return userDataSource.register(userContext.getUserId(), registerDto);
    }

    public Result<Authentication> authenticateAtBank(String code) {
        return userDataSource.authenticateBank(code);
    }

    public void updateBankAuthInfo(String tokenType, Long expiresAt, String accessToken, String refreshToken) {
        userSharedPrefs.setTokenType(tokenType);
        userSharedPrefs.setExpiresAt(expiresAt);
        userSharedPrefs.setAccessToken(accessToken);
        userSharedPrefs.setRefreshToken(refreshToken);
        this.bankAuthNotifier.updateBankAuthInfo(tokenType, expiresAt, accessToken, refreshToken);
    }

    public void addUserLocally(User user) {
        userDao.insertUser(user);
    }

    public void cachePin(String pin) {
        userCache.setPin(pin);
    }
}
