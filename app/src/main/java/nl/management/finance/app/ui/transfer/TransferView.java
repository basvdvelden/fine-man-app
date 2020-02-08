package nl.management.finance.app.ui.transfer;

import nl.management.finance.app.ui.overview.BankAccountView;

public class TransferView {
    private BankAccountView mTransferBankAccount;
    private Double mTransferAmount;
    private String mTransferReceiversName;
    private String mTransferReceiversIban;
    private String mTransferDescription;
    private String mTransferPaymentRef;

    public TransferView(BankAccountView mTransferBankAccount, Double mTransferAmount,
                        String mTransferReceiversName, String mTransferReceiversIban,
                        String mTransferDescription, String mTransferPaymentRef) {
        this.mTransferBankAccount = mTransferBankAccount;
        this.mTransferAmount = mTransferAmount;
        this.mTransferReceiversName = mTransferReceiversName;
        this.mTransferReceiversIban = mTransferReceiversIban;
        this.mTransferDescription = mTransferDescription;
        this.mTransferPaymentRef = mTransferPaymentRef;
    }

    public BankAccountView getTransferBankAccount() {
        return mTransferBankAccount;
    }

    public Double getTransferAmount() {
        return mTransferAmount;
    }

    public String getTransferReceiversName() {
        return mTransferReceiversName;
    }

    public String getTransferReceiversIban() {
        return mTransferReceiversIban;
    }

    public String getTransferDescription() {
        return mTransferDescription;
    }

    public String getTransferPaymentRef() {
        return mTransferPaymentRef;
    }
}
