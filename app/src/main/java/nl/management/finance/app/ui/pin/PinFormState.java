package nl.management.finance.app.ui.pin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class PinFormState {
    @Nullable
    private Integer pinError;

    private boolean isDataValid;

    PinFormState(@NonNull Integer pinError) {
        this.pinError = pinError;
        this.isDataValid = false;
    }

    PinFormState(boolean isDataValid) {
        this.pinError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getPinError() {
        return pinError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
