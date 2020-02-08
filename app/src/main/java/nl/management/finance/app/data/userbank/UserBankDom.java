package nl.management.finance.app.data.userbank;

import nl.management.finance.app.data.bank.BankDom;

public class UserBankDom {
    private BankDom bankDom;
    private String consentCode;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long expiresAt;

    public BankDom getBankDom() {
        return bankDom;
    }

    public void setBankDom(BankDom bankDom) {
        this.bankDom = bankDom;
    }

    public String getConsentCode() {
        return consentCode;
    }

    public void setConsentCode(String consentCode) {
        this.consentCode = consentCode;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
