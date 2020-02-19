package nl.management.finance.app.ui.overview;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.IRefreshFinishedCallback;
import nl.management.finance.app.data.bankaccount.BankAccount;
import nl.management.finance.app.data.bankaccount.BankAccountMapper;
import nl.management.finance.app.data.bankaccount.BankAccountRepository;

public class OverviewViewModel extends ViewModel {
    private final static String TAG = "OverviewViewModel";
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper mapper;
    private final LiveData<List<BankAccount>> bankAccounts;
    private final MutableLiveData<List<BankAccountView>> bankAccountViews = new MutableLiveData<>();
    private final Observer<List<BankAccount>> mObserver = new Observer<List<BankAccount>>() {
        @Override
        public void onChanged(List<BankAccount> bankAccounts) {
            bankAccountViews.setValue(mapper.toView(bankAccounts));
        }
    };

    @Inject
    public OverviewViewModel(BankAccountRepository bankAccountRepository, BankAccountMapper mapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;
        bankAccounts = bankAccountRepository.getBankAccounts();
        bankAccounts.observeForever(mObserver);
    }

    public LiveData<List<BankAccountView>> getBankAccounts()  {
        return this.bankAccountViews;
    }

    public void refreshBankAccounts(IRefreshFinishedCallback callback) {
        Completable.fromAction(bankAccountRepository::refreshBankAccounts)
                .subscribeOn(Schedulers.io())
                .subscribe(callback::refreshDone);
    }

    @Override
    public void onCleared() {
        bankAccounts.removeObserver(mObserver);
    }
}
