package nl.management.finance.app.data.api.rabo;

public class RaboBalance {
    private RaboBalanceAmount balanceAmount;
    private String balanceType;
    private String lastChangeDateTime;

    public String getLastChangeDateTime() {
        return lastChangeDateTime;
    }

    public void setLastChangeDateTime(String lastChangeDateTime) {
        this.lastChangeDateTime = lastChangeDateTime;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public RaboBalanceAmount getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(RaboBalanceAmount balanceAmount) {
        this.balanceAmount = balanceAmount;
    }
}
