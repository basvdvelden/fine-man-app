package nl.management.finance.app.ui.overview.model;

import androidx.annotation.NonNull;

public class BankAccountView {
    private String name;
    private String iban;
    private String balance;
    private String currency;

    public BankAccountView(@NonNull String name, @NonNull String iban,
                           @NonNull String balance, @NonNull String currency) {
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
}
