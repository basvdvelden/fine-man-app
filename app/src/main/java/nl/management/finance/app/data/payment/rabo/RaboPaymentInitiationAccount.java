package nl.management.finance.app.data.payment.rabo;

import androidx.annotation.NonNull;

public class RaboPaymentInitiationAccount {
    private String currency;
    private String iban;

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

    @Override
    @NonNull
    public String toString() {
        return String.format("[\ncurrency=%s, \niban=%s\n]", currency, iban);
    }
}
