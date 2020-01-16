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
    private final BankAccountMapper mapper;
    private final MutableLiveData<List<BankAccountView>> liveBankAccounts = new MutableLiveData<>();

    @Inject
    public OverviewViewModel(BankAccountRepository bankAccountRepository, BankAccountMapper mapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;

        Completable.fromAction(() -> liveBankAccounts.postValue(getBankAccounts()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<BankAccountView>> getLiveBankAccounts()  {
        return this.liveBankAccounts;
    }

    private List<BankAccountView> getBankAccounts() {
        Result<List<BankAccount>> bankAccountsResult = bankAccountRepository.getBankAccounts();
        if (bankAccountsResult instanceof Result.Success) {
            List<BankAccount> bankAccounts = ((Result.Success<List<BankAccount>>) bankAccountsResult)
                    .getData();
            return mapper.toViews(bankAccounts);
        } else {
            Log.e(TAG, "error getting bank accounts: ", ((Result.Error) bankAccountsResult).getError());
            throw new NotYetImplementedException("activity should show error pop up");
        }
    }
}
