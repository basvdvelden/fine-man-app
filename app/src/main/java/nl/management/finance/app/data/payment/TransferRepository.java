package nl.management.finance.app.data.payment;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Named;

import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.storage.UserSharedPreferences;
import nl.management.finance.app.mapper.SepaCreditTransferMapper;

public class TransferRepository {
    private final UserContext userContext;
    private final TransferDataSource dataSource;
    private final SepaCreditTransferMapper mMapper;
    private final UserSharedPreferences mSharedPreferences;

    @Inject
    public TransferRepository(UserContext userContext, TransferDataSource dataSource,
                              SepaCreditTransferMapper mapper,
                              UserSharedPreferences sharedPreferences) {
        this.userContext = userContext;
        this.dataSource = dataSource;
        mMapper = mapper;
        mSharedPreferences = sharedPreferences;
    }

    public Result<String> initiatePayment(SepaCreditTransfer sepaCreditTransfer) {
        return dataSource.initiatePayment(mMapper.toDto(sepaCreditTransfer));
    }

    public void savePaymentInitiationId(String paymentInitiationId) {
        mSharedPreferences.setPaymentInitiationId(paymentInitiationId);
    }

    public String getPaymentInitiationId() {
        return mSharedPreferences.getPaymentInitiationId();
    }

    public Result<PaymentStatusResponse> getPaymentStatus(String paymentId) {
        return dataSource.getPaymentStatus(paymentId);
    }
}
