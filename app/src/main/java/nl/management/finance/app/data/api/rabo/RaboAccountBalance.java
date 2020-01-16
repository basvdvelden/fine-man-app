package nl.management.finance.app.data.api.rabo;

import java.util.List;

public class RaboAccountBalance {
    private RaboAccount account;
    private List<RaboBalance> balances;

    public RaboAccount getAccount() {
        return account;
    }

    public void setAccount(RaboAccount account) {
        this.account = account;
    }

    public List<RaboBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<RaboBalance> balances) {
        this.balances = balances;
    }
}
