package nl.management.finance.app.ui.transactions;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.bankaccount.BankAccountRepository;
import nl.management.finance.app.data.transaction.Transaction;
import nl.management.finance.app.data.transaction.TransactionMapper;
import nl.management.finance.app.data.transaction.TransactionRepository;

public class TransactionViewModel extends ViewModel {
    private final TransactionRepository repository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionMapper mapper;

    private MutableLiveData<List<TransactionView>> transactions = new MutableLiveData<>();

    @Inject
    public TransactionViewModel(TransactionRepository repository, BankAccountRepository bankAccountRepository, TransactionMapper mapper) {
        this.repository = repository;
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;
    }

    public LiveData<List<TransactionView>> getTransactions(String iban) {
        Observable.fromCallable(() -> {
            String bankAccountResourceId = bankAccountRepository.getByIban(iban).getResourceId();
            return repository.getTransactions(bankAccountResourceId);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    if (result instanceof Result.Success) {
                        List<Transaction> data = ((Result.Success<List<Transaction>>) result).getData();
                        transactions.setValue(mapper.toViews(data));
                    }
                    // TODO: remove
                    repository.delete();
                });
        return transactions;
    }

    public void refresh() {
        bankAccountRepository.refreshBankAccounts();
    }
}
