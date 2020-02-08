package nl.management.finance.app.ui.transfer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import nl.management.finance.app.data.bankaccount.BankAccountRepository;
import nl.management.finance.app.data.payment.Transfer;
import nl.management.finance.app.data.payment.TransferRepository;
import nl.management.finance.app.ui.UIResult;
import nl.management.finance.app.ui.overview.BankAccountView;

public class TransferViewModel extends ViewModel {
    private static final String TAG = "TransferViewModel";
    private final TransferRepository mRepository;
    private final TransferMapper mMapper;
    private BankAccountRepository mBaRepo;
    private UserContext mContext;
    private MutableLiveData<UIResult<String>> mInitPaymentResult = new MutableLiveData<>();
    private MutableLiveData<TransferFormState> mTransferFormState = new MutableLiveData<>();
    private TransferView mTransfer = null;

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
    public TransferViewModel(TransferRepository repository, TransferMapper mapper,
                             BankAccountRepository baRepo, UserContext context) {
        mRepository = repository;
        mMapper = mapper;
        mBaRepo = baRepo;
        mContext = context;
    }

    public TransferView getTransfer() {
        return mTransfer;
    }

    public void initiatePayment() {
        Completable.fromAction(() -> {
            Result<String> result = mRepository.initiatePayment(mMapper.toDomain(mTransfer));
            if (result instanceof Result.Success) {
                mInitPaymentResult.postValue(new UIResult<>(((Result.Success<String>) result).getData()));
            } else {
                mInitPaymentResult.postValue(new UIResult<>(R.string.server_error));
            }
        }).subscribeOn(Schedulers.io())
        .subscribe();
    }

    public void transferDataChanged(BankAccountView bankAccount, Double amount, String receiversName,
                                    String receiversIban, String description, String paymentRef) {
        mTransfer = new TransferView(bankAccount, amount, receiversName, receiversIban, description,
                paymentRef);

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
        return amount > Double.valueOf(mTransfer.getTransferBankAccount().getBalance())
                ? R.string.transfer_bank_account_error : null;
    }

    public LiveData<UIResult<String>> getInitPaymentResult() {
        return mInitPaymentResult;
    }

    public MutableLiveData<TransferFormState> getTransferFormState() {
        return mTransferFormState;
    }
}
