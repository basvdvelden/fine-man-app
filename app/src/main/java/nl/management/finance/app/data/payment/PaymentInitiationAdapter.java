package nl.management.finance.app.data.payment;

import nl.management.finance.app.data.Result;

public interface PaymentInitiationAdapter {

    Result<String> initiatePayment(Transfer transfer);
}
