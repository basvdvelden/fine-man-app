package nl.management.finance.app.data.payment;

import androidx.annotation.NonNull;

public class SepaCreditTransferRemittanceInformationStructuredDto {
    private String reference;
    private String referenceIssuer;
    private String referenceType;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceIssuer() {
        return referenceIssuer;
    }

    public void setReferenceIssuer(String referenceIssuer) {
        this.referenceIssuer = referenceIssuer;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[\nreference=%s, \nreferenceIssuer=%s, \nreferenceType=%s\n]",
                reference, referenceIssuer, referenceType);
    }
}
