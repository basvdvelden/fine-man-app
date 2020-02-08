package nl.management.finance.app.ui.overview;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.IRefreshFinishedCallback;
import nl.management.finance.app.data.bankaccount.BankAccountRepository;

public class OverviewViewModel extends ViewModel {
    private final static String TAG = "OverviewViewModel";
    private final BankAccountRepository bankAccountRepository;
    private final LiveData<List<BankAccountView>> bankAccounts;

    @Inject
    public OverviewViewModel(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
        bankAccounts = bankAccountRepository.getBankAccounts();
    }

    public LiveData<List<BankAccountView>> getBankAccounts()  {
        return this.bankAccounts;
    }

    public void refreshBankAccounts(IRefreshFinishedCallback callback) {
        Completable.fromAction(bankAccountRepository::refreshBankAccounts)
                .subscribeOn(Schedulers.io())
                .subscribe(callback::refreshDone);
    }
}
