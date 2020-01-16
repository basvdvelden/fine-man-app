package nl.management.finance.app;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class UserContext {
    private UUID userId;
    private String displayName;
    private String username;
    private String bankName;

    @Inject
    public UserContext() {
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
}
