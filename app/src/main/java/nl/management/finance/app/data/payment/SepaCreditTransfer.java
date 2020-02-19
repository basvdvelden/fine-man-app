package nl.management.finance.app.data.payment;

public class SepaCreditTransfer {
    private SepaCreditTransferAccount creditorAccount;
    private SepaCreditTransferAccount debtorAccount;
    private Double amount;
    private String currency;
    private String description;
    private String paymentRef;
    private String requestedExecutionDate;

    public SepaCreditTransferAccount getCreditorAccount() {
        return creditorAccount;
    }

    public SepaCreditTransferAccount getDebtorAccount() {
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

    public void setCreditorAccount(SepaCreditTransferAccount creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public void setDebtorAccount(SepaCreditTransferAccount debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public String getRequestedExecutionDate() {
        return requestedExecutionDate;
    }

    public void setRequestedExecutionDate(String requestedExecutionDate) {
        this.requestedExecutionDate = requestedExecutionDate;
    }
}
