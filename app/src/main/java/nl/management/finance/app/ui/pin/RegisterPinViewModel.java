package nl.management.finance.app.ui.pin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import nl.management.finance.app.R;

import javax.inject.Inject;

import nl.management.finance.app.data.PinCode;

public class RegisterPinViewModel extends PinViewModel {
    private static final String TAG = "RegisterPinViewModel";

    private final PinCode pin = new PinCode();
    private MutableLiveData<PinFormState> pinFormState = new MutableLiveData<>();

    @Inject
    RegisterPinViewModel() {
        this.currentPin = pin;
    }

    void initial() {
        this.pinFormState = new MutableLiveData<>();
        this.currentPin.reset();
        this.pin.reset();
    }

    LiveData<PinFormState> getPinFormState() { return pinFormState; }

    void pinRegistrationDataFilledIn() {
        if (pin.getValue().equals(currentPin.getValue())) {
            pinFormState.setValue(new PinFormState(true));
        } else {
            pinFormState.setValue(new PinFormState(R.string.pin_codes_not_equal));
        }
    }

    void nextPin() {
        currentPin = new PinCode();
    }

    void resetAllPins() {
        currentPin.reset();
        pin.reset();
        currentPin = pin;
    }

    public String getPin() {
        return this.currentPin.getValue();
    }
}
