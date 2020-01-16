package nl.management.finance.app.data.bankaccount;

import java.util.List;

import javax.inject.Inject;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;

public class BankAccountDataSource {
    private RaboApi raboApi;
    private UserContext context;

    @Inject
    public BankAccountDataSource(RaboApi raboApi, UserContext context) {
        this.raboApi = raboApi;
        this.context = context;
    }

    public Result<Double> getBalance(String resourceId) {
        BankAccountAdapter adapter;
        switch (context.getBankName()) {
            case BuildConfig.RABO_BANK_NAME:
                adapter = new RaboBankAccountAdapter(raboApi);
                break;
            default:
                throw new RuntimeException(String.format("no bank with name: %s", context.getBankName()));
        }
        return adapter.getBalance(resourceId);
    }

    public Result<List<BankAccountDto>> getBankAccounts() {
        BankAccountAdapter adapter;
        switch (context.getBankName()) {
            case BuildConfig.RABO_BANK_NAME:
                adapter = new RaboBankAccountAdapter(raboApi);
                break;
            default:
                throw new RuntimeException(String.format("no bank with name: %s", context.getBankName()));
        }
        return adapter.getBankAccounts();
    }
}
