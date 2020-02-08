package nl.management.finance.app.data.payment.rabo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RaboTransfer {
//  Optional fields
    private RaboPaymentInitiationAddress creditorAddress;
    private String creditorAgent;
    private String requestedExecutionDate;
    private String remittanceInformationUnstructured;
    private RaboPaymentInitiationRemittanceInformationStructured remittanceInformationStructured;

//  Mandatory fields
    private RaboPaymentInitiationAccount creditorAccount;
    private String creditorName;
    private RaboPaymentInitiationAccount debtorAccount;
    private String endToEndIdentification;
    private RaboPaymentInitiationAmount instructedAmount;

    public RaboPaymentInitiationAccount getCreditorAccount() {
        return creditorAccount;
    }

    public void setCreditorAccount(RaboPaymentInitiationAccount creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public RaboPaymentInitiationAddress getCreditorAddress() {
        return creditorAddress;
    }

    public void setCreditorAddress(RaboPaymentInitiationAddress creditorAddress) {
        this.creditorAddress = creditorAddress;
    }

    public String getCreditorAgent() {
        return creditorAgent;
    }

    public void setCreditorAgent(String creditorAgent) {
        this.creditorAgent = creditorAgent;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public RaboPaymentInitiationAccount getDebtorAccount() {
        return debtorAccount;
    }

    public void setDebtorAccount(RaboPaymentInitiationAccount debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public String getEndToEndIdentification() {
        return endToEndIdentification;
    }

    public void setEndToEndIdentification(String endToEndIdentification) {
        this.endToEndIdentification = endToEndIdentification;
    }

    public RaboPaymentInitiationAmount getInstructedAmount() {
        return instructedAmount;
    }

    public void setInstructedAmount(RaboPaymentInitiationAmount instructedAmount) {
        this.instructedAmount = instructedAmount;
    }

    public String getRemittanceInformationUnstructured() {
        return remittanceInformationUnstructured;
    }

    public void setRemittanceInformationUnstructured(String remittanceInformationUnstructured) {
        this.remittanceInformationUnstructured = remittanceInformationUnstructured;
    }

    public RaboPaymentInitiationRemittanceInformationStructured getRemittanceInformationStructured() {
        return remittanceInformationStructured;
    }

    public void setRemittanceInformationStructured(RaboPaymentInitiationRemittanceInformationStructured remittanceInformationStructured) {
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
        return String.format("[\ncreditorAccount=%s, \ncreditorAddress=%s, \ncreditorAgent=%s, " +
                        "\ncreditorName=%s, \ndebtorAccount=%s, \nendToEndIdentification=%s, \ninstructedAmount=%s, " +
                        "\nrequestedExecutionDate=%s, \nremittanceInformationUnstructured=%s, " +
                        "\nremittanceInformationStructured=%s\n]", creditorAccount, creditorAddress,
                creditorAgent, creditorName, debtorAccount, endToEndIdentification, instructedAmount,
                requestedExecutionDate, remittanceInformationUnstructured, remittanceInformationStructured);
    }

}
