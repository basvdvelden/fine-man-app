package nl.management.finance.app.data.user;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.authentication.management.app.LoginNotifier;
import nl.authentication.management.app.api.AuthNotifier;
import nl.authentication.management.app.data.login.LoggedInUser;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserSubject;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.BankAuthNotifier;
import nl.management.finance.app.data.storage.UserCache;
import nl.management.finance.app.data.storage.UserSharedPreferences;
import nl.management.finance.app.data.userbank.UserBank;
import nl.management.finance.app.data.userbank.UserBankDao;
import nl.management.finance.app.data.userbank.UserBankDto;

@Singleton
public class UserRepository {
    private static final String TAG = "UserRepository";
    private final UserDataSource userDataSource;
    private final UserCache userCache;
    private final UserSharedPreferences userSharedPrefs;
    private final UserContext userContext;
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final UserSubject userSubject;
    private final AuthNotifier authNotifier;

    @Inject
    public UserRepository(UserDataSource userDataSource, UserCache userCache,
                          UserSharedPreferences userSharedPrefs, UserContext userContext,
                          UserDao userDao, UserMapper userMapper, LoginNotifier loginNotifier,
                          UserSubject userSubject,
                          AuthNotifier authNotifier) {
        this.userDataSource = userDataSource;
        this.userCache = userCache;
        this.userSharedPrefs = userSharedPrefs;
        this.userContext = userContext;
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.userSubject = userSubject;
        this.authNotifier = authNotifier;
        loginNotifier.getLoggedInSubject().subscribe(optLoggedInUser -> {
            LoggedInUser loggedInUser = optLoggedInUser.get();
            if (loggedInUser != null) {
                Completable.fromAction(() -> {
                    User user = getUser();
                    this.userSubject.setUser(user);
                }).subscribeOn(Schedulers.io())
                .subscribe();
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
                    authNotifier.notifyToLogout();
                }
                return result;
            } finally {
                if (hasRegisteredPin != null) {
                    setHasUserRegisteredPin(hasRegisteredPin);
                }
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

    public Result<User> getUserRemotely() {
        Result<UserDto> result = userDataSource.getUser();
        if (result instanceof Result.Success) {
            return new Result.Success<>(userMapper.toEntity(((Result.Success<UserDto>) result).getData()));
        }
        return new Result.Error(((Result.Error) result).getError());
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

    public void createUser(User user) {
        userDao.upsertUser(user);
    }

    public void cachePin(String pin) {
        userSubject.setPin(pin);
    }
}
