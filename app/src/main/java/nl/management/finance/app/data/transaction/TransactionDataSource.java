package nl.management.finance.app.data.transaction;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;
import nl.management.finance.app.data.transaction.rabo.RaboTransactionAdapter;
import nl.management.finance.app.data.transaction.rabo.TransactionDto;

@Singleton
public class TransactionDataSource {
    private final RaboApi raboApi;
    private final UserContext context;

    @Inject
    public TransactionDataSource(RaboApi raboApi, UserContext context) {
        this.raboApi = raboApi;
        this.context = context;
    }

    public Result<List<TransactionDto>> getTransactions(String bankAccountResourceId) {
        TransactionAdapter adapter;
        switch (context.getBankName()) {
            case BuildConfig.RABO_BANK_NAME:
                adapter = new RaboTransactionAdapter(raboApi);
                break;
            default:
                throw new RuntimeException(String.format("no bank with name: %s", context.getBankName()));
        }
        return adapter.getTransactions(bankAccountResourceId);
    }
}
