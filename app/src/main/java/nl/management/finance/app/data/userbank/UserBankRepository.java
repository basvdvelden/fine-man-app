package nl.management.finance.app.data.userbank;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserSubject;
import nl.management.finance.app.data.user.User;

public class UserBankRepository {
    private static final String TAG = "UserBankRepository";

    private final UserBankDao userBankDao;
    private final UserContext context;

    @Inject
    public UserBankRepository(UserBankDao userBankDao, UserContext context, UserSubject userSubject) {
        this.userBankDao = userBankDao;
        this.context = context;
        userSubject.get().subscribe(optUser -> {
            User user = optUser.get();
            if (user != null) {
                Completable.fromAction(() -> {
                    // TODO: should return a list since this is a many to many
                    UserBank userBank = userBankDao.getByUserId(user.getId());
                    switch (userBank.getBankId()) {
                        case BuildConfig.RABO_BANK_ID:
                            context.setBank(BuildConfig.RABO_BANK_NAME);
                            context.setConsentCode(userBank.getConsentCode());
                            break;
                        default:
                            throw new RuntimeException(String.format("no bank with id: %d", userBank.getBankId()));

                    }
                }).subscribeOn(Schedulers.io())
                        .subscribe();
            }
        });
    }

    public void addUserBankLocally(UserBank userBank) {
        this.userBankDao.insertUserBank(userBank);
    }

}
