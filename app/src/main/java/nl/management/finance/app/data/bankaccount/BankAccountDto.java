package nl.management.finance.app.data.bankaccount;

import java.util.UUID;

import androidx.annotation.NonNull;

public class BankAccountDto {
    private UUID id;
    private UUID userId;
    private int bankId;
    private String name;
    private String currency;
    private String iban;
    private String resourceId;
    private Double balance;

    public BankAccountDto(UUID userId, int bankId, String name, String currency, String iban,
                          String resourceId, Double balance) {
        this.userId = userId;
        this.bankId = bankId;
        this.name = name;
        this.currency = currency;
        this.iban = iban;
        this.resourceId = resourceId;
        this.balance = balance;
    }

    public BankAccountDto() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[id=%s, name=%s, currency=%s, iban=%s, resourceId=%s]\n",
                id, name, currency, iban, resourceId);
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }
}
