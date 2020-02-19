package nl.management.finance.app.data.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.storage.AppSharedPreferences;

public class TransactionRepository {
    private static final String TAG = "TransactionRepository";
    private final TransactionDao transactionDao;
    private final TransactionDataSource dataSource;
    private final TransactionMapper mapper;

    @Inject
    public TransactionRepository(TransactionDao transactionDao, TransactionDataSource dataSource,
                                 TransactionMapper mapper) {
        this.transactionDao = transactionDao;
        this.mapper = mapper;
        this.dataSource = dataSource;
    }

    public LiveData<List<Transaction>> getTransactions(UUID bankAccountId) {
        return transactionDao.getByBankAccountId(bankAccountId.toString());
    }

    public void refreshTransactions(String bankAccountResourceId, UUID bankAccountId) {
        Result<List<TransactionDto>> dtoResult = dataSource.getTransactions(bankAccountResourceId, bankAccountId);
        if (dtoResult instanceof Result.Success) {
            List<Transaction> transactions = mapper.toEntities(
                    ((Result.Success<List<TransactionDto>>) dtoResult).getData(), bankAccountId);
            transactionDao.upsertTransactions(transactions);
        }
    }

    public void delete() {
        Completable.fromAction(transactionDao::deleteAll)
                .subscribeOn(Schedulers.io()).subscribe();
       
    }
}
