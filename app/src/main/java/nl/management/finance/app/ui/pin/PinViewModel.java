package nl.management.finance.app.ui.pin;

import androidx.lifecycle.ViewModel;

import nl.management.finance.app.data.PinCode;

public class PinViewModel extends ViewModel {
    protected PinCode currentPin;

    void numEntered(char num) {
        currentPin.append(num);
    }

    void deleteClicked() {
        currentPin.deleteLast();
    }
}
