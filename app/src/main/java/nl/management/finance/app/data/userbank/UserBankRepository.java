package nl.management.finance.app.data.userbank;

import javax.inject.Inject;

public class UserBankRepository {
    private static final String TAG = "UserBankRepository";

    private final UserBankDao userBankDao;

    @Inject
    public UserBankRepository(UserBankDao userBankDao) {
        this.userBankDao = userBankDao;
    }

    public void addUserBankLocally(UserBank userBank) {
        this.userBankDao.insertUserBank(userBank);
    }

}
