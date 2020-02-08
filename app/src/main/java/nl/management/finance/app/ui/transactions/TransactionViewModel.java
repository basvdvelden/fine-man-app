package nl.management.finance.app.ui.transactions;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.IRefreshFinishedCallback;
import nl.management.finance.app.data.bankaccount.BankAccountRepository;
import nl.management.finance.app.data.transaction.TransactionMapper;
import nl.management.finance.app.data.transaction.TransactionRepository;

public class TransactionViewModel extends ViewModel {
    private final TransactionRepository repository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionMapper mapper;

    @Inject
    public TransactionViewModel(TransactionRepository repository, BankAccountRepository bankAccountRepository, TransactionMapper mapper) {
        this.repository = repository;
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;
    }

    public LiveData<List<TransactionView>> getTransactions(String iban) {
        return repository.getTransactions(iban);
    }

    public void refreshTransactions(IRefreshFinishedCallback callback) {
        Completable.fromAction(repository::refreshTransactions)
                .subscribeOn(Schedulers.io())
                .subscribe(callback::refreshDone);
    }

    public void refresh() {
        bankAccountRepository.refreshBankAccounts();
    }

    public String getResourceIdForIBAN(String iban) {
        return bankAccountRepository.getByIban(iban).getResourceId();
    }
}
