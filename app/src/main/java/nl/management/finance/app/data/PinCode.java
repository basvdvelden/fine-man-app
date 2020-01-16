package nl.management.finance.app.data;

public class PinCode {
    private final static String VALID_DIGITS = "0123456789";
    private String value = "";

    public PinCode() {
    }

    public String getValue() {
        return value;
    }

    public void append(char num) {
        if (isValidNum(num)) {
            this.setValue(this.value.concat(Character.toString(num)));
        }
    }

    public void deleteLast() {
        if (canDelete()) {
            setValue(this.value.substring(0, this.value.length() - 1));
        }
    }

    public void reset() {
        this.value = "";
    }

    private boolean canDelete() {
        return this.value.length() > 0;
    }

    private void setValue(String value) {
        this.value = value;
    }

    private boolean isValidNum(char digit) {
        for (char validNum: VALID_DIGITS.toCharArray()) {
            if (validNum == digit) {
                return true;
            }
        }
        return false;
    }
}
