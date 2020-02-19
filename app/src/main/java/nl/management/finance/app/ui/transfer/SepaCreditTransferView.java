package nl.management.finance.app.ui.transfer;

import nl.management.finance.app.ui.overview.BankAccountView;

public class SepaCreditTransferView {
    private BankAccountView mBankAccount;
    private Double mAmount;
    private String mReceiversName;
    private String mReceiversIban;
    private String mDescription;
    private String mPaymentRef;
    private String mCurrency;
    private String mRequestedExecutionDate;

    public SepaCreditTransferView(BankAccountView bankAccount, Double amount,
                                  String receiversName, String receiversIban,
                                  String description, String paymentRef, String currency,
                                  String requestedExecutionDate) {
        mBankAccount = bankAccount;
        mAmount = amount;
        mReceiversName = receiversName;
        mReceiversIban = receiversIban;
        mDescription = description;
        mPaymentRef = paymentRef;
        mCurrency = currency;
        mRequestedExecutionDate = requestedExecutionDate;
    }

    public BankAccountView getBankAccount() {
        return mBankAccount;
    }

    public Double getAmount() {
        return mAmount;
    }

    public String getReceiversName() {
        return mReceiversName;
    }

    public String getReceiversIban() {
        return mReceiversIban;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPaymentRef() {
        return mPaymentRef;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getRequestedExecutionDate() {
        return mRequestedExecutionDate;
    }
}
