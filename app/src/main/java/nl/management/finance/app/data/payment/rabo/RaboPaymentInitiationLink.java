package nl.management.finance.app.data.payment.rabo;

import androidx.annotation.NonNull;

class RaboPaymentInitiationLink {
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[href=%s]", href);
    }
}
