package nl.management.finance.app.ui.transfer;

import javax.inject.Inject;

import nl.management.finance.app.data.payment.Transfer;
import nl.management.finance.app.data.payment.TransferAccount;

public class TransferMapper {
    @Inject
    public TransferMapper() {}

    public Transfer toDomain(TransferView view) {
        TransferAccount creditor = new TransferAccount(view.getTransferBankAccount().getCurrency(),
                view.getTransferBankAccount().getName(), view.getTransferBankAccount().getIban());

        TransferAccount debtor = new TransferAccount(view.getTransferBankAccount().getCurrency(),
                view.getTransferReceiversName(), view.getTransferReceiversIban());

        return new Transfer(creditor, debtor, view.getTransferAmount(), creditor.getCurrency(),
                view.getTransferDescription(), view.getTransferPaymentRef(), "41",
                "streksingel", "3054HB", "NL", "Rotterdam");
    }
}
