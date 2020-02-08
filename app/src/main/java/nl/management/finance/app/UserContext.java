package nl.management.finance.app;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.authentication.management.app.LoginNotifier;
import nl.authentication.management.app.data.login.LoggedInUser;
import nl.management.finance.app.data.bankaccount.BankAccount;
import nl.management.finance.app.data.user.User;


@Singleton
public class UserContext {
    private UUID userId;
    private String displayName;
    private String username;
    private String bankName;
    private User user;
    private BankAccount account;
    private BankAccount counterAccount;
    private String consentCode;
    private LoginNotifier loginNotifier;

    @Inject
    public UserContext(LoginNotifier loginNotifier, UserSubject userSubject) {
        this.loginNotifier = loginNotifier;
        this.loginNotifier.getLoggedInSubject().subscribe((optLoggedInUser) -> {
            LoggedInUser loggedInUser = optLoggedInUser.get();
            if (loggedInUser != null) {
                userDataChanged(loggedInUser.getUserId(), loggedInUser.getDisplayName(), loggedInUser.getUsername());
            }
        });
        userSubject.getUser().subscribe(user -> this.user = user.get());
    }

    public void userDataChanged(UUID userId, String displayName, String username) {
        this.userId = userId;
        this.displayName = displayName;
        this.username = username;
    }

    public void setBank(String bankName) {
        this.bankName = bankName;
    }

    public UUID getUserId() {
        if (userId == null) {
            throw new IllegalStateException("user id was accessed before it was set");
        }
        return userId;
    }

    public String getDisplayName() {
        if (displayName == null) {
            throw new IllegalStateException("display name was accessed before it was set");
        }
        return displayName;
    }

    public String getUsername() {
        if (username == null) {
             throw new IllegalStateException("username was accessed before it was set");
        }
        return username;
    }

    public String getBankName() {
        if (bankName == null) {
            throw new IllegalStateException("bank name was accessed before it was set");
        }
        return bankName;
    }

    public int getBankId() {
        switch (getBankName()) {
            case BuildConfig.RABO_BANK_NAME:
                return BuildConfig.RABO_BANK_ID;
            default:
                throw new RuntimeException(String.format("no bank with name: %s", getBankName()));
        }
    }

    public User getUser() {
        return user;
    }

    public String getConsentCode() {
        return consentCode;
    }

    public void setConsentCode(String consentCode) {
        this.consentCode = consentCode;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public BankAccount getCounterAccount() {
        return counterAccount;
    }

    public void setCounterAccount(BankAccount counterAccount) {
        this.counterAccount = counterAccount;
    }
}
