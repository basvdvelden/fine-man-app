package nl.management.finance.app.data.bank;

import android.util.Log;

import javax.inject.Inject;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserSubject;
import nl.management.finance.app.data.storage.UserSharedPreferences;


public class BankRepository {
    private static final String TAG = "BankRepository";
    private final BankDao bankDao;
    private final UserContext userContext;
    private final UserSharedPreferences sharedPreferences;

    @Inject
    public BankRepository(BankDao bankDao, UserContext userContext, UserSharedPreferences sharedPreferences,
                          UserSubject userSubject) {
        this.bankDao = bankDao;
        this.userContext = userContext;
        this.sharedPreferences = sharedPreferences;
        userSubject.getUser().subscribe(user -> {
            if (user.get() != null) {
                try {
                    userContext.getBankName();
                } catch (IllegalStateException e) {
                    // set current bank if not yet set
                    setCurrentBank(sharedPreferences.getMostRecentBank(), false);
                    userSubject.bankSelected();
                }
            }
        });
    }

    public void setCurrentBank(String bankName, boolean saveInSharedPrefs) {
        this.userContext.setBank(bankName);
        if (saveInSharedPrefs) {
            sharedPreferences.setMostRecentBank(bankName);
        }
    }

    public void setCurrentBank(int bankId, boolean saveInSharedPrefs) {
        setCurrentBank(getBankName(bankId), saveInSharedPrefs);
    }

    private String getBankName(int bankId) {
        switch(bankId) {
            case BuildConfig.RABO_BANK_ID:
                return BuildConfig.RABO_BANK_NAME;
            default:
                throw new IllegalStateException("Couldn't find bank name for id " + bankId);
        }
    }

    public int getBankId(String bankName) {
        switch(bankName) {
            case BuildConfig.RABO_BANK_NAME:
                return BuildConfig.RABO_BANK_ID;
            default:
                throw new IllegalStateException("Couldn't find bank id for name " + bankName);
        }
    }
}
