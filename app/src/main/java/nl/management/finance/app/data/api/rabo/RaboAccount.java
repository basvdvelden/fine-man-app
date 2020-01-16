package nl.management.finance.app.data.api.rabo;

public class RaboAccount {
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
}
