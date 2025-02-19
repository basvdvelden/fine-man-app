package nl.management.finance.app.data.payment;

import nl.management.finance.app.data.Result;

public interface PaymentInitiationAdapter {

    Result<String> initiatePayment(SepaCreditTransferDto sepaCreditTransfer);

    Result<PaymentStatusResponse> getPaymentStatus(String paymentId);
}
