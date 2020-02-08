package nl.management.finance.app.data.payment.rabo;

import androidx.annotation.NonNull;

class RaboPaymentInitiationLinks {
    private RaboPaymentInitiationLink scaRedirect;
    private RaboPaymentInitiationLink scaStatus;
    private RaboPaymentInitiationLink self;
    private RaboPaymentInitiationLink startAuthorisationWithAuthenticationMethodSelection;
    private RaboPaymentInitiationLink startAuthorisationWithEncryptedPsuAuthentication;
    private RaboPaymentInitiationLink startAuthorisationWithPsuAuthentication;
    private RaboPaymentInitiationLink startAuthorisationWithPsuIdentification;
    private RaboPaymentInitiationLink startAuthorisationWithTransactionAuthorisation;
    private RaboPaymentInitiationLink startAuthorisation;
    private RaboPaymentInitiationLink status;

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

    public RaboPaymentInitiationLink getScaRedirect() {
        return scaRedirect;
    }

    public void setScaRedirect(RaboPaymentInitiationLink scaRedirect) {
        this.scaRedirect = scaRedirect;
    }

    public RaboPaymentInitiationLink getScaStatus() {
        return scaStatus;
    }

    public void setScaStatus(RaboPaymentInitiationLink scaStatus) {
        this.scaStatus = scaStatus;
    }

    public RaboPaymentInitiationLink getSelf() {
        return self;
    }

    public void setSelf(RaboPaymentInitiationLink self) {
        this.self = self;
    }

    public RaboPaymentInitiationLink getStartAuthorisationWithAuthenticationMethodSelection() {
        return startAuthorisationWithAuthenticationMethodSelection;
    }

    public void setStartAuthorisationWithAuthenticationMethodSelection(RaboPaymentInitiationLink startAuthorisationWithAuthenticationMethodSelection) {
        this.startAuthorisationWithAuthenticationMethodSelection = startAuthorisationWithAuthenticationMethodSelection;
    }

    public RaboPaymentInitiationLink getStartAuthorisationWithEncryptedPsuAuthentication() {
        return startAuthorisationWithEncryptedPsuAuthentication;
    }

    public void setStartAuthorisationWithEncryptedPsuAuthentication(RaboPaymentInitiationLink startAuthorisationWithEncryptedPsuAuthentication) {
        this.startAuthorisationWithEncryptedPsuAuthentication = startAuthorisationWithEncryptedPsuAuthentication;
    }

    public RaboPaymentInitiationLink getStartAuthorisationWithPsuAuthentication() {
        return startAuthorisationWithPsuAuthentication;
    }

    public void setStartAuthorisationWithPsuAuthentication(RaboPaymentInitiationLink startAuthorisationWithPsuAuthentication) {
        this.startAuthorisationWithPsuAuthentication = startAuthorisationWithPsuAuthentication;
    }

    public RaboPaymentInitiationLink getStartAuthorisationWithPsuIdentification() {
        return startAuthorisationWithPsuIdentification;
    }

    public void setStartAuthorisationWithPsuIdentification(RaboPaymentInitiationLink startAuthorisationWithPsuIdentification) {
        this.startAuthorisationWithPsuIdentification = startAuthorisationWithPsuIdentification;
    }

    public RaboPaymentInitiationLink getStartAuthorisationWithTransactionAuthorisation() {
        return startAuthorisationWithTransactionAuthorisation;
    }

    public void setStartAuthorisationWithTransactionAuthorisation(RaboPaymentInitiationLink startAuthorisationWithTransactionAuthorisation) {
        this.startAuthorisationWithTransactionAuthorisation = startAuthorisationWithTransactionAuthorisation;
    }

    public RaboPaymentInitiationLink getStartAuthorisation() {
        return startAuthorisation;
    }

    public void setStartAuthorisation(RaboPaymentInitiationLink startAuthorisation) {
        this.startAuthorisation = startAuthorisation;
    }

    public RaboPaymentInitiationLink getStatus() {
        return status;
    }

    public void setStatus(RaboPaymentInitiationLink status) {
        this.status = status;
    }
}
