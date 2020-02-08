package nl.management.finance.app.data.payment;

import javax.inject.Inject;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;
import nl.management.finance.app.data.payment.rabo.RaboPaymentInitiationAdapter;
import nl.management.finance.app.data.payment.rabo.RaboPaymentInitiationResponse;

class TransferDataSource {
    private final RaboApi raboApi;
    private final UserContext userContext;

    @Inject
    public TransferDataSource(RaboApi raboApi, UserContext userContext) {
        this.raboApi = raboApi;
        this.userContext = userContext;
    }

    public Result<String> initiatePayment(Transfer transfer) {
        PaymentInitiationAdapter adapter = null;
        switch (userContext.getBankName()) {
            case BuildConfig
                    .RABO_BANK_NAME:
                adapter = new RaboPaymentInitiationAdapter(raboApi, userContext);
        }
        return adapter.initiatePayment(transfer);
    }
}
