package nl.management.finance.app.data.bankaccount;

import androidx.annotation.NonNull;

public class BankAccountDto {
    private String name;
    private String currency;
    private String iban;
    private String resourceId;

    public BankAccountDto(String name, String currency, String iban, String resourceId) {
        this.name = name;
        this.currency = currency;
        this.iban = iban;
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public String getIban() {
        return iban;
    }

    public String getResourceId() {
        return resourceId;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[name=%s, currency=%s, iban=%s, resourceId=%s]\n",
                name, currency, iban, resourceId);
    }
}
