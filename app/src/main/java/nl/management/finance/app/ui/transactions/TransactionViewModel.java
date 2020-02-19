package nl.management.finance.app.ui.transactions;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.IRefreshFinishedCallback;
import nl.management.finance.app.data.transaction.Transaction;
import nl.management.finance.app.data.transaction.TransactionMapper;
import nl.management.finance.app.data.transaction.TransactionRepository;

@Singleton
public class TransactionViewModel extends ViewModel {
    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final MutableLiveData<List<TransactionView>> mTransactionViews = new MutableLiveData<>();
    private final Observer<List<Transaction>> mObserver = new Observer<List<Transaction>>() {
        @Override
        public void onChanged(List<Transaction> transactions) {
            mTransactionViews.setValue(mapper.toViews(transactions));
        }
    };

    private LiveData<List<Transaction>> mTransactions;

    private String bankAccountResourceId;
    private UUID bankAccountId;

    @Inject
    public TransactionViewModel(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public LiveData<List<TransactionView>> getTransactions() {
        mTransactions = repository.getTransactions(bankAccountId);
        mTransactions.observeForever(mObserver);
        return mTransactionViews;
    }

    public void refreshTransactions(IRefreshFinishedCallback callback) {
        Completable.fromAction(() -> repository.refreshTransactions(bankAccountResourceId, bankAccountId))
                .subscribeOn(Schedulers.io())
                .subscribe(callback::refreshDone);
    }

    @Override
    public void onCleared() {
        mTransactions.removeObserver(mObserver);
    }

    public void setBankAccountResourceId(String bankAccountResourceId) {
        this.bankAccountResourceId = bankAccountResourceId;
    }

    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
}
