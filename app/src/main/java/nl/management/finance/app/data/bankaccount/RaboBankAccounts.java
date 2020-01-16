package nl.management.finance.app.data.bankaccount;

import java.util.List;

import nl.management.finance.app.data.api.rabo.RaboBankAccount;

public class RaboBankAccounts {
    private List<RaboBankAccount> accounts;

    public List<RaboBankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<RaboBankAccount> accounts) {
        this.accounts = accounts;
    }
}
