package nl.management.finance.app.data.payment;

public class TransferAccount {
    private String currency;
    private String name;
    private String iban;

    public TransferAccount(String currency, String name, String iban) {
        this.currency = currency;
        this.name = name;
        this.iban = iban;
    }

    public String getCurrency() {
        return currency;
    }

    public String getIban() {
        return iban;
    }

    public String getName() {
        return name;
    }
}
