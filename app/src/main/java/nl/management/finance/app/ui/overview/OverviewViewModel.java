package nl.management.finance.app.ui.overview;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.NotYetImplementedException;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.bankaccount.BankAccount;
import nl.management.finance.app.data.bankaccount.BankAccountMapper;
import nl.management.finance.app.data.bankaccount.BankAccountRepository;
import nl.management.finance.app.ui.overview.model.BankAccountView;

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

    public void refreshBankAccounts() {
        bankAccountRepository.refreshBankAccounts();
    }
}
