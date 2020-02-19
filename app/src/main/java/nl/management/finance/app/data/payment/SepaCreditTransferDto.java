package nl.management.finance.app.data.payment;

import androidx.annotation.NonNull;

public class SepaCreditTransferDto {
//  Mandatory fields
    private SepaCreditTransferAccountDto creditorAccount;
    private String creditorName;
    private SepaCreditTransferAccountDto debtorAccount;
    private String endToEndIdentification;
    private SepaCreditTransferAmountDto instructedAmount;
//  Optional fields
    private String requestedExecutionDate;
    private String remittanceInformationUnstructured;
    private SepaCreditTransferRemittanceInformationStructuredDto remittanceInformationStructured;

    public SepaCreditTransferAccountDto getCreditorAccount() {
        return creditorAccount;
    }

    public void setCreditorAccount(SepaCreditTransferAccountDto creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public SepaCreditTransferAccountDto getDebtorAccount() {
        return debtorAccount;
    }

    public void setDebtorAccount(SepaCreditTransferAccountDto debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public String getEndToEndIdentification() {
        return endToEndIdentification;
    }

    public void setEndToEndIdentification(String endToEndIdentification) {
        this.endToEndIdentification = endToEndIdentification;
    }

    public SepaCreditTransferAmountDto getInstructedAmount() {
        return instructedAmount;
    }

    public void setInstructedAmount(SepaCreditTransferAmountDto instructedAmount) {
        this.instructedAmount = instructedAmount;
    }

    public String getRemittanceInformationUnstructured() {
        return remittanceInformationUnstructured;
    }

    public void setRemittanceInformationUnstructured(String remittanceInformationUnstructured) {
        this.remittanceInformationUnstructured = remittanceInformationUnstructured;
    }

    public SepaCreditTransferRemittanceInformationStructuredDto getRemittanceInformationStructured() {
        return remittanceInformationStructured;
    }

    public void setRemittanceInformationStructured(SepaCreditTransferRemittanceInformationStructuredDto remittanceInformationStructured) {
        this.remittanceInformationStructured = remittanceInformationStructured;
    }

    public String getRequestedExecutionDate() {
        return requestedExecutionDate;
    }

    public void setRequestedExecutionDate(String requestedExecutionDate) {
        this.requestedExecutionDate = requestedExecutionDate;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[\ncreditorAccount=%s, \ncreditorName=%s, \ndebtorAccount=%s, " +
                        "\nendToEndIdentification=%s, \ninstructedAmount=%s, \nrequestedExecutionDate=%s, " +
                        "\nremittanceInformationUnstructured=%s, \nremittanceInformationStructured=%s\n]",
                creditorAccount, creditorName, debtorAccount, endToEndIdentification, instructedAmount,
                requestedExecutionDate, remittanceInformationUnstructured, remittanceInformationStructured);
    }

}
