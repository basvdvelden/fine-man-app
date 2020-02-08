package nl.management.finance.app.data.payment;

public class Transfer {
    private TransferAccount creditorAccount;
    private TransferAccount debtorAccount;
    private Double amount;
    private String currency;
    private String description;
    private String paymentRef;
    private String creditorBuildingNumber;
    private String creditorStreetName;
    private String creditorPostCode;
    private String creditorCountry;
    private String creditorTownName;

    public Transfer(TransferAccount creditorAccount, TransferAccount debtorAccount, Double amount,
                    String currency, String description, String paymentRef, String creditorBuildingNumber,
                    String creditorStreetName, String creditorPostCode, String creditorCountry,
                    String creditorTownName) {
        this.creditorAccount = creditorAccount;
        this.debtorAccount = debtorAccount;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.paymentRef = paymentRef;
        this.creditorBuildingNumber = creditorBuildingNumber;
        this.creditorStreetName = creditorStreetName;
        this.creditorPostCode = creditorPostCode;
        this.creditorCountry = creditorCountry;
        this.creditorTownName = creditorTownName;
    }

    public TransferAccount getCreditorAccount() {
        return creditorAccount;
    }

    public TransferAccount getDebtorAccount() {
        return debtorAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public String getCreditorBuildingNumber() {
        return creditorBuildingNumber;
    }

    public String getCreditorStreetName() {
        return creditorStreetName;
    }

    public String getCreditorPostCode() {
        return creditorPostCode;
    }

    public String getCreditorCountry() {
        return creditorCountry;
    }

    public String getCreditorTownName() {
        return creditorTownName;
    }
}
