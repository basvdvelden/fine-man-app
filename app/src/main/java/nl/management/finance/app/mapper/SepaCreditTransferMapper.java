package nl.management.finance.app.mapper;

import java.util.UUID;

import javax.inject.Inject;

import nl.management.finance.app.data.payment.SepaCreditTransfer;
import nl.management.finance.app.data.payment.SepaCreditTransferAccount;
import nl.management.finance.app.data.payment.SepaCreditTransferAccountDto;
import nl.management.finance.app.data.payment.SepaCreditTransferAmountDto;
import nl.management.finance.app.data.payment.SepaCreditTransferDto;
import nl.management.finance.app.data.payment.SepaCreditTransferRemittanceInformationStructuredDto;
import nl.management.finance.app.ui.transfer.SepaCreditTransferView;

public class SepaCreditTransferMapper {
    @Inject
    public SepaCreditTransferMapper() {}

    public SepaCreditTransfer toDomain(SepaCreditTransferView view) {
        SepaCreditTransferAccount creditor = new SepaCreditTransferAccount();
        creditor.setIban(view.getBankAccount().getIban());
        creditor.setName(view.getBankAccount().getName());

        SepaCreditTransferAccount debtor = new SepaCreditTransferAccount();
        debtor.setName(view.getReceiversName());
        debtor.setIban(view.getReceiversIban());

        SepaCreditTransfer result = new SepaCreditTransfer();
        result.setCreditorAccount(creditor);
        result.setDebtorAccount(debtor);
        result.setAmount(view.getAmount());
        result.setCurrency(view.getCurrency());
        result.setPaymentRef(view.getPaymentRef());
        result.setDescription(view.getDescription());
        result.setRequestedExecutionDate(view.getRequestedExecutionDate());

        return result;
    }

    public SepaCreditTransferDto toDto(SepaCreditTransfer domain) {
        SepaCreditTransferAccountDto creditor = new SepaCreditTransferAccountDto();
        creditor.setIban(domain.getCreditorAccount().getIban());

        SepaCreditTransferAccountDto debtor = new SepaCreditTransferAccountDto();
        debtor.setIban(domain.getDebtorAccount().getIban());

        SepaCreditTransferAmountDto amount = new SepaCreditTransferAmountDto();
        amount.setContent(domain.getAmount().toString());
        amount.setCurrency(domain.getCurrency());

        SepaCreditTransferRemittanceInformationStructuredDto ris = new SepaCreditTransferRemittanceInformationStructuredDto();
        ris.setReference(domain.getPaymentRef());

        SepaCreditTransferDto result = new SepaCreditTransferDto();
        result.setCreditorAccount(creditor);
        result.setDebtorAccount(debtor);
        result.setInstructedAmount(amount);
        result.setCreditorName(domain.getCreditorAccount().getName());
        result.setEndToEndIdentification(UUID.randomUUID().toString().substring(0, 35));
        result.setRequestedExecutionDate(domain.getRequestedExecutionDate());

        // TODO: We get an error from the bank server if we set both RemittanceInformation fields
        if (domain.getDescription() != null && !domain.getDescription().trim().isEmpty()) {
            result.setRemittanceInformationUnstructured(domain.getDescription());
        }
        //result.setRemittanceInformationStructured(ris);

        return result;
    }
}
