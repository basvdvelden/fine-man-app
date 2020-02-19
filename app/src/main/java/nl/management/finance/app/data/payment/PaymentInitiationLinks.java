package nl.management.finance.app.data.payment;

import androidx.annotation.NonNull;

class PaymentInitiationLinks {
    private PaymentInitiationLink scaRedirect;
    private PaymentInitiationLink scaStatus;
    private PaymentInitiationLink self;
    private PaymentInitiationLink startAuthorisationWithAuthenticationMethodSelection;
    private PaymentInitiationLink startAuthorisationWithEncryptedPsuAuthentication;
    private PaymentInitiationLink startAuthorisationWithPsuAuthentication;
    private PaymentInitiationLink startAuthorisationWithPsuIdentification;
    private PaymentInitiationLink startAuthorisationWithTransactionAuthorisation;
    private PaymentInitiationLink startAuthorisation;
    private PaymentInitiationLink status;

    @Override
    @NonNull
    public String toString() {
        return String.format("[scaRedirect=%s,\nscaStatus=%s,\nself=%s,\nstartAuthorisationWithAuthenticationMethodSelection=%s,\n" +
                "startAuthorisationWithEncryptedPsuAuthentication=%s,\n" +
                "startAuthorisationWithPsuAuthentication=%s,\nstartAuthorisationWithPsuIdentification=%s,\n" +
                "startAuthorisationWithTransactionAuthorisation=%s,\nstartAuthorisation=%s,\nstatus=%s]",
                scaRedirect, scaStatus, self, startAuthorisationWithAuthenticationMethodSelection,
                startAuthorisationWithEncryptedPsuAuthentication, startAuthorisationWithPsuAuthentication,
                startAuthorisationWithPsuIdentification, startAuthorisationWithTransactionAuthorisation,
                startAuthorisation, status);
    }

    public PaymentInitiationLink getScaRedirect() {
        return scaRedirect;
    }

    public void setScaRedirect(PaymentInitiationLink scaRedirect) {
        this.scaRedirect = scaRedirect;
    }

    public PaymentInitiationLink getScaStatus() {
        return scaStatus;
    }

    public void setScaStatus(PaymentInitiationLink scaStatus) {
        this.scaStatus = scaStatus;
    }

    public PaymentInitiationLink getSelf() {
        return self;
    }

    public void setSelf(PaymentInitiationLink self) {
        this.self = self;
    }

    public PaymentInitiationLink getStartAuthorisationWithAuthenticationMethodSelection() {
        return startAuthorisationWithAuthenticationMethodSelection;
    }

    public void setStartAuthorisationWithAuthenticationMethodSelection(PaymentInitiationLink startAuthorisationWithAuthenticationMethodSelection) {
        this.startAuthorisationWithAuthenticationMethodSelection = startAuthorisationWithAuthenticationMethodSelection;
    }

    public PaymentInitiationLink getStartAuthorisationWithEncryptedPsuAuthentication() {
        return startAuthorisationWithEncryptedPsuAuthentication;
    }

    public void setStartAuthorisationWithEncryptedPsuAuthentication(PaymentInitiationLink startAuthorisationWithEncryptedPsuAuthentication) {
        this.startAuthorisationWithEncryptedPsuAuthentication = startAuthorisationWithEncryptedPsuAuthentication;
    }

    public PaymentInitiationLink getStartAuthorisationWithPsuAuthentication() {
        return startAuthorisationWithPsuAuthentication;
    }

    public void setStartAuthorisationWithPsuAuthentication(PaymentInitiationLink startAuthorisationWithPsuAuthentication) {
        this.startAuthorisationWithPsuAuthentication = startAuthorisationWithPsuAuthentication;
    }

    public PaymentInitiationLink getStartAuthorisationWithPsuIdentification() {
        return startAuthorisationWithPsuIdentification;
    }

    public void setStartAuthorisationWithPsuIdentification(PaymentInitiationLink startAuthorisationWithPsuIdentification) {
        this.startAuthorisationWithPsuIdentification = startAuthorisationWithPsuIdentification;
    }

    public PaymentInitiationLink getStartAuthorisationWithTransactionAuthorisation() {
        return startAuthorisationWithTransactionAuthorisation;
    }

    public void setStartAuthorisationWithTransactionAuthorisation(PaymentInitiationLink startAuthorisationWithTransactionAuthorisation) {
        this.startAuthorisationWithTransactionAuthorisation = startAuthorisationWithTransactionAuthorisation;
    }

    public PaymentInitiationLink getStartAuthorisation() {
        return startAuthorisation;
    }

    public void setStartAuthorisation(PaymentInitiationLink startAuthorisation) {
        this.startAuthorisation = startAuthorisation;
    }

    public PaymentInitiationLink getStatus() {
        return status;
    }

    public void setStatus(PaymentInitiationLink status) {
        this.status = status;
    }
}
