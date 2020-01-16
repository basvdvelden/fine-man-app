package nl.management.finance.app.data.userbank;

import android.os.Build;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.user.User;

public class UserBankRepository {
    private static final String TAG = "UserBankRepository";

    private final UserBankDao userBankDao;
    private final UserContext context;

    @Inject
    public UserBankRepository(UserBankDao userBankDao, UserContext context) {
        this.userBankDao = userBankDao;
        this.context = context;

    }

    public void addUserBankLocally(UserBank userBank) {
        this.userBankDao.insertUserBank(userBank);
    }

    public void setCurrentBank(String userId) {
        Completable.fromAction(() -> {
            UserBank userBank = userBankDao.getByUserId(userId);
            if (userBank != null) {
                switch (userBank.getBankId()) {
                    case BuildConfig.RABO_BANK_ID:
                        context.setBank(BuildConfig.RABO_BANK_NAME);
                        break;
                    default:
                        throw new RuntimeException(String.format("no bank with id: %d", userBank.getBankId()));

                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public UserBank getForUser(User user) {
        return userBankDao.getByUserId(user.getId());
    }
}
