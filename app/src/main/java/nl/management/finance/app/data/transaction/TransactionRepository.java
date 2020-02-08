package nl.management.finance.app.data.transaction;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.transaction.rabo.TransactionDto;
import nl.management.finance.app.ui.transactions.TransactionView;

public class TransactionRepository {
    private static final String TAG = "TransactionRepository";
    private final TransactionDao transactionDao;
    private final TransactionDataSource dataSource;
    private final TransactionMapper mapper;
    private String mBankAccountId;

    @Inject
    public TransactionRepository(TransactionDao transactionDao, TransactionDataSource dataSource,
                                 TransactionMapper mapper) {
        this.transactionDao = transactionDao;
        this.mapper = mapper;
        this.dataSource = dataSource;
    }

    public LiveData<List<TransactionView>> getTransactions(String bankAccountId) {
        mBankAccountId = bankAccountId;
        return transactionDao.getByBankAccountId(mBankAccountId);
    }

    public void refreshTransactions() {
        Result<List<TransactionDto>> dtoResult = dataSource.getTransactions(mBankAccountId);
        if (dtoResult instanceof Result.Success) {
            List<Transaction> transactions = mapper.toEntities(
                    ((Result.Success<List<TransactionDto>>) dtoResult).getData(), mBankAccountId);
            transactionDao.upsertTransactions(transactions);
        }
    }

    public void delete() {
        Completable.fromAction(transactionDao::deleteAll)
                .subscribeOn(Schedulers.io()).subscribe();
       
    }
}
