package nl.management.finance.app.ui.transfer;

import androidx.annotation.Nullable;

public class TransferFormState {
    @Nullable
    private Integer amountError;
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer ibanError;
    @Nullable
    private Integer descriptionError;
    @Nullable
    private Integer paymentRefError;
    private boolean isDataValid;

    public TransferFormState(@Nullable Integer amountError, @Nullable Integer nameError,
                             @Nullable Integer ibanError, @Nullable Integer descriptionError,
                             @Nullable Integer paymentRefError) {
        this.amountError = amountError;
        this.nameError = nameError;
        this.ibanError = ibanError;
        this.descriptionError = descriptionError;
        this.paymentRefError = paymentRefError;
        setIsDataValid();
    }

    private void setIsDataValid() {
        isDataValid = amountError == null && nameError == null && ibanError == null &&
                descriptionError == null && paymentRefError == null;
    }

    @Nullable
    public Integer getAmountError() {
        return amountError;
    }

    @Nullable
    public Integer getNameError() {
        return nameError;
    }

    @Nullable
    public Integer getIbanError() {
        return ibanError;
    }

    @Nullable
    public Integer getDescriptionError() {
        return descriptionError;
    }

    @Nullable
    public Integer getPaymentRefError() {
        return paymentRefError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
