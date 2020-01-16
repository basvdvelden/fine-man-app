package nl.management.finance.app.data.user;

public class RegistrationFailedException extends Exception {
    RegistrationFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
