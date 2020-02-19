package nl.management.finance.app.data.payment;

import java.util.Locale;

import androidx.annotation.NonNull;

public class SepaCreditTransferAmountDto {
    private String content;
    private String currency;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format(Locale.getDefault(), "[\ncontent=%s, \ncurrency=%s\n]", content, currency);
    }
}
