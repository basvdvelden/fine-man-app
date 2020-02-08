package nl.management.finance.app.data.user;

import android.util.Log;

import com.google.android.gms.auth.api.Auth;

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
import nl.management.finance.app.data.bank.BankMapper;
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
    private final UserBankDao userBankDao;
    private final BankAuthNotifier bankAuthNotifier;
    private final UserSubject userSubject;
    private final BankMapper bankMapper;
    private final AuthNotifier authNotifier;

    @Inject
    public UserRepository(UserDataSource userDataSource, UserCache userCache,
                          UserSharedPreferences userSharedPrefs, UserContext userContext,
                          UserDao userDao, UserBankDao userBankDao, BankAuthNotifier bankAuthNotifier,
                          LoginNotifier loginNotifier, UserSubject userSubject, BankMapper bankMapper,
                          AuthNotifier authNotifier) {
        this.userDataSource = userDataSource;
        this.userCache = userCache;
        this.userSharedPrefs = userSharedPrefs;
        this.userContext = userContext;
        this.userDao = userDao;
        this.userBankDao = userBankDao;
        this.bankAuthNotifier = bankAuthNotifier;
        this.userSubject = userSubject;
        this.bankMapper = bankMapper;
        this.authNotifier = authNotifier;
        this.bankAuthNotifier.getUpdatedAuthentication().subscribe(authentication -> {
            updateBankAuthInfo(authentication.getTokenType(), authentication.getExpiresAt(),
                    authentication.getAccessToken(), authentication.getRefreshToken(), true);
        });
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
        this.userSubject.getUser().subscribe(user -> {
            if (user.get() != null) {
                try {
                    userContext.getBankName();
                } catch (IllegalStateException e) {
                    // set current bank if not yet set
                    setCurrentBank(userSharedPrefs.getMostRecentBank(), false);
                }
                Completable.fromAction(() -> {
                    Authentication auth = userBankDao.getAuthByUserIdAndBankId(
                            userContext.getUserId().toString(), userContext.getBankId());
                    this.bankAuthNotifier.updateBankAuthInfo(auth.getTokenType(),
                            auth.getExpiresAt(), auth.getAccessToken(),
                            auth.getRefreshToken());
                }).subscribeOn(Schedulers.io())
                        .subscribe();
            }
        });
        // TODO: weghalen5
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

    public void setCurrentBank(String bankName, boolean saveInSharedPrefs) {
        this.userContext.setBank(bankName);
        if (saveInSharedPrefs) {
            userSharedPrefs.setMostRecentBank(bankName);
        }
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

    public void updateBankAuthInfo(String tokenType, Long expiresAt, String accessToken, String refreshToken,
                                   boolean updateEntity) {
        this.bankAuthNotifier.updateBankAuthInfo(tokenType, expiresAt, accessToken, refreshToken);

        if (updateEntity) {
            Completable.fromAction(() -> {
                UserBank userBank = userBankDao.getByUserIdAndBankId(userContext.getUserId().toString(), userContext.getBankId());
                userBank.setTokenType(tokenType);
                userBank.setExpiresAt(expiresAt);
                userBank.setAccessToken(accessToken);
                userBank.setRefreshToken(refreshToken);
                Authentication auth = new Authentication(accessToken, expiresAt, refreshToken, tokenType);
                Log.e(TAG, auth.getExpiresAt().toString());
                userDataSource.updateBankAuthentication(auth);
                userBankDao.updateUserBank(userBank);
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    public void addUserLocally(User user) {
        userDao.insertUser(user);
    }

    public void cachePin(String pin) {
        userSubject.setPin(pin);
    }

    public boolean syncUserWithServer() {
        Result<UserDto> result = userDataSource.getUser();
        if (result instanceof Result.Success) {
            User user = new User(userContext.getUserId().toString(), userContext.getUsername());
            List<UserBank> userBanks = bankMapper.toUserBankEntities(
                    ((Result.Success<UserDto>) result).getData().getUserBanks());
            UserBankDto ub = ((Result.Success<UserDto>) result).getData().getUserBanks().get(0);
            setCurrentBank(ub.getBank().getName(),
                    true);
            updateBankAuthInfo(ub.getTokenType(), ub.getExpiresAt(), ub.getAccessToken(), ub.getRefreshToken(), false);
            userDao.upsertUser(user);
            userBankDao.upsertUserBanks(userBanks);
            return true;
        }
        return false;
    }
}
