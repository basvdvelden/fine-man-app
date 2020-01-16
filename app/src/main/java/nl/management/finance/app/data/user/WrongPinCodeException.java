package nl.management.finance.app.data.user;

public class WrongPinCodeException extends Exception {
    WrongPinCodeException(String msg) {
        super(msg);
    }
}
