package nl.management.finance.app.data.payment;

import androidx.annotation.NonNull;

public class SepaCreditTransferAccountDto {
    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[\niban=%s\n]", iban);
    }
}
