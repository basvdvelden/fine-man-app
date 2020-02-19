package nl.management.finance.app.ui.transfer;

import android.net.Uri;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.R;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.contact.ContactRepository;
import nl.management.finance.app.data.payment.PaymentStatusResponse;
import nl.management.finance.app.data.payment.TransferRepository;
import nl.management.finance.app.mapper.ContactMapper;
import nl.management.finance.app.mapper.SepaCreditTransferMapper;
import nl.management.finance.app.ui.UIResult;
import nl.management.finance.app.ui.contacts.ContactView;
import nl.management.finance.app.ui.overview.BankAccountView;

public class TransferViewModel extends ViewModel {
    private static final String TAG = "TransferViewModel";
    private final TransferRepository mRepository;
    private final ContactRepository mContactRepository;
    private final SepaCreditTransferMapper mMapper;
    private final ContactMapper mContactMapper;
    private final UserContext mUserContext;

    private MutableLiveData<UIResult<String>> mInitPaymentResult = new MutableLiveData<>();
    private MutableLiveData<TransferFormState> mTransferFormState = new MutableLiveData<>();
    private SepaCreditTransferView mTransfer = null;

    private MutableLiveData<UIResult<String>> mPsuMessage = new MutableLiveData<>();

    private Map<Character, Integer> characterToNumberMap = new HashMap<Character, Integer>() {
        {
            char ch = 'A';
            for (int i = 10; i < 36; i++) {
                put(ch, i);
                ++ch;
            }
        }
    };

    @Inject
    public TransferViewModel(TransferRepository repository, SepaCreditTransferMapper mapper,
                             ContactRepository contactRepository, ContactMapper contactMapper,
                             UserContext userContext) {
        mRepository = repository;
        mContactRepository = contactRepository;
        mMapper = mapper;
        mContactMapper = contactMapper;
        mUserContext = userContext;
    }

    public SepaCreditTransferView getTransfer() {
        return mTransfer;
    }

    public void initiatePayment() {
        Completable.fromAction(() -> {
            Result<String> result = mRepository.initiatePayment(mMapper.toDomain(mTransfer));
            if (result instanceof Result.Success) {
                String scaRedirect = ((Result.Success<String>) result).getData();
                Uri uri = Uri.parse(scaRedirect);
                // Store the payment initiation id so we can get its status after the bank redirects back to us
                String paymentInitiationId = uri.getQueryParameter("paymentinitiationid")
                        .split("/")[0];
                mRepository.savePaymentInitiationId(paymentInitiationId);
                mInitPaymentResult.postValue(new UIResult<>(scaRedirect));
            } else {
                mInitPaymentResult.postValue(new UIResult<>(R.string.server_error));
            }
        }).subscribeOn(Schedulers.io())
        .subscribe(() -> mInitPaymentResult = new MutableLiveData<>());
    }

    public void transferDataChanged(BankAccountView bankAccount, Double amount, String receiversName,
                                    String receiversIban, String description, String paymentRef,
                                    String currency, String requestedExecutionDate) {
        mTransfer = new SepaCreditTransferView(bankAccount, amount, receiversName, receiversIban, description,
                paymentRef, currency, requestedExecutionDate);

        Integer amountError = getAmountError(amount);
        Integer receiversNameError = getReceiversNameError(receiversName);
        Integer receiversIbanError = getReceiversIbanError(receiversIban);
        Integer descriptionError = getDescriptionError(description);
        Integer paymentRefError = getPaymentRefError(paymentRef);
        mTransferFormState.setValue(new TransferFormState(amountError, receiversNameError,
                receiversIbanError, descriptionError, paymentRefError));
    }

    // TODO: implement payment reference validation
    private Integer getPaymentRefError(String paymentRef) {
        return null;
    }

    private Integer getDescriptionError(String description) {
        return description.length() > 140 ? R.string.transfer_description_error : null;
    }

    private Integer getReceiversIbanError(String receiversIban) {
        // IBAN validation as described in ISO 7064
        if (receiversIban.length() > 4) {
            String first4Chars = receiversIban.substring(0, 4);
            String transformedIban = receiversIban.replace(first4Chars, "")
                    .concat(first4Chars);

            for (Character ch : transformedIban.toUpperCase().toCharArray()) {
                if (characterToNumberMap.containsKey(ch)) {
                    transformedIban = transformedIban.toUpperCase().replace(ch.toString(),
                            characterToNumberMap.get(ch).toString());
                }
            }

            return new BigDecimal(transformedIban).remainder(new BigDecimal(97))
                    .compareTo(new BigDecimal(1)) != 0 ?
                    R.string.transfer_receivers_iban_error : null;
        } else {
            return R.string.transfer_receivers_iban_error;
        }
    }

    private Integer getReceiversNameError(String receiversName) {
        return receiversName.isEmpty() ? R.string.transfer_receivers_name_empty_error : null;
    }

    private Integer getAmountError(Double amount) {
        return amount > Double.valueOf(mTransfer.getBankAccount().getBalance())
                ? R.string.transfer_bank_account_error : null;
    }

    public LiveData<UIResult<String>> getInitPaymentResult() {
        return mInitPaymentResult;
    }

    public LiveData<TransferFormState> getTransferFormState() {
        return mTransferFormState;
    }

    public LiveData<UIResult<String>> getPsuMessage() {
        Completable.fromAction(() -> {
            // TODO: Uncomment below line.
//            String paymentId = mRepository.getPaymentInitiationId();
            String paymentId = testPaymentIds.get("ACTC");
            Result<PaymentStatusResponse> result = mRepository.getPaymentStatus(paymentId);

            if (result instanceof Result.Success) {
                PaymentStatusResponse response = ((Result.Success<PaymentStatusResponse>) result).getData();
                String msg = response.getPsuMessage() == null ? statusMessageMap.get(response.getTransactionStatus())
                                                              : response.getPsuMessage();
                mPsuMessage.postValue(new UIResult<>(msg));
            } else {
                mPsuMessage.postValue(new UIResult<>(R.string.server_error));
            }
        }).subscribeOn(Schedulers.io()).subscribe();

        return mPsuMessage;
    }

    private HashMap<String, String> testPaymentIds = new HashMap<String, String>() {
        {
            put("ACTC", "123e4567-e89b-42d3-a456-556642440005");
            put("PDNG", "123e4567-e89b-42d3-a456-556642440006");
            put("ACSP", "123e4567-e89b-42d3-a456-556642440007");
            put("RJCT", "123e4567-e89b-42d3-a456-556642440008");
            put("ACSC", "123e4567-e89b-42d3-a456-556642440009");
            put("ACWC", "123e4567-e89b-42d3-a456-556642440010");
            put("ACCC", "123e4567-e89b-42d3-a456-556642440011");
            put("ACCP", "123e4567-e89b-42d3-a456-556642440012");
            put("ACWP", "123e4567-e89b-42d3-a456-556642440013");
            put("CANC", "123e4567-e89b-42d3-a456-556642440014");
            put("ACFC", "123e4567-e89b-42d3-a456-556642440015");
            put("PATC", "123e4567-e89b-42d3-a456-556642440016");
            put("PART", "123e4567-e89b-42d3-a456-556642440017");
            put("RCVD", "123e4567-e89b-42d3-a456-556642440004");
        }
    };

    private HashMap<String, String> statusMessageMap = new HashMap<String, String>() {
        {
            put("ACTC", "Payment has not yet finished.");
            put("PDNG", "Payment is pending.");
            put("ACSP", "Payment is in progress.");
            put("RJCT", "Payment was rejected.");
            put("ACSC", "Payment landed at debtor's account.");
            put("ACWC", "Payment was accepted but changes were made.");
            put("ACCC", "Payment was successful.");
            put("ACCP", "Payment has not yet finished.");
            put("ACWP", "Payment accepted but is not posted to your account.");
            put("CANC", "Payment was cancelled.");
            put("ACFC", "Payment accepted");
            put("PATC", "Payment not yet authorized by all parties");
            put("PART", "Some transactions have not yet been accepted.");
            put("RCVD", "Payment was received.");
        }
    };
}
