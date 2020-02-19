package nl.management.finance.app.ui.overview;

import java.util.UUID;

import androidx.annotation.NonNull;

public class BankAccountView {
    private UUID id;
    private String resourceId;
    private String name;
    private String iban;
    private String balance;
    private String currency;

    public BankAccountView(@NonNull UUID id, @NonNull String resourceId, @NonNull String name, @NonNull String iban,
                           @NonNull String balance, @NonNull String currency) {
        this.id = id;
        this.resourceId = resourceId;
        this.name = name;
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getIban() {
        return iban;
    }

    public String getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public UUID getId() {
        return id;
    }

    public String getResourceId() {
        return resourceId;
    }
}
