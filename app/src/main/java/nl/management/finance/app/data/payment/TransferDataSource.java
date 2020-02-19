package nl.management.finance.app.data.payment;

import javax.inject.Inject;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;

class TransferDataSource {
    private final RaboApi raboApi;
    private final UserContext userContext;

    @Inject
    public TransferDataSource(RaboApi raboApi, UserContext userContext) {
        this.raboApi = raboApi;
        this.userContext = userContext;
    }

    public Result<String> initiatePayment(SepaCreditTransferDto sepaCreditTransfer) {
        PaymentInitiationAdapter adapter = getAdapter();
        return adapter.initiatePayment(sepaCreditTransfer);
    }

    public Result<PaymentStatusResponse> getPaymentStatus(String paymentId) {
        PaymentInitiationAdapter adapter = getAdapter();
        return adapter.getPaymentStatus(paymentId);
    }

    private PaymentInitiationAdapter getAdapter() {
        PaymentInitiationAdapter adapter = null;
        switch (userContext.getBankName()) {
            case BuildConfig
                    .RABO_BANK_NAME:
                adapter = new RaboPaymentInitiationAdapter(raboApi);
        }
        return adapter;
    }
}
