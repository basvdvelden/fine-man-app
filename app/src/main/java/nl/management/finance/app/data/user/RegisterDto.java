package nl.management.finance.app.data.user;

public class RegisterDto {
    private String pin;
    private String bank;
    private String consentCode;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setConsentCode(String consentCode) {
        this.consentCode = consentCode;
    }

    public String getConsentCode() {
        return consentCode;
    }
}
