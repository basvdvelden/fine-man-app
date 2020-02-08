package nl.management.finance.app.data.payment;

import javax.inject.Inject;

import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;

public class TransferRepository {
    private final UserContext userContext;
    private final TransferDataSource dataSource;

    @Inject
    public TransferRepository(UserContext userContext, TransferDataSource dataSource) {
        this.userContext = userContext;
        this.dataSource = dataSource;
    }

    public Result<String> initiatePayment(Transfer transfer) {
        return dataSource.initiatePayment(transfer);
    }
}
