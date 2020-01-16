package nl.management.finance.app.data.storage;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import nl.management.finance.app.data.bankaccount.BankAccount;

public class UserCache {
    private UUID userId;
    private String pin;
    private Boolean hasRegisteredPin;
    private List<BankAccount> bankAccounts;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Boolean getHasRegisteredPin() {
        return hasRegisteredPin;
    }

    public void setHasRegisteredPin(Boolean hasRegisteredPin) {
        this.hasRegisteredPin = hasRegisteredPin;
    }

    @NonNull
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(@NonNull List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
