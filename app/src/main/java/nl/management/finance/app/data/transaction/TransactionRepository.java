package nl.management.finance.app.data.transaction;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.transaction.rabo.TransactionDto;

public class TransactionRepository {
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

    public Result<List<Transaction>> getTransactions(String bankAccountResourceId) {
        List<Transaction> transactions = transactionDao.getByBankAccountId(bankAccountResourceId);
        if (transactions.size() < 1) {
            Result<List<TransactionDto>> dtoResult = dataSource.getTransactions(bankAccountResourceId);
            if (dtoResult instanceof Result.Success) {
                transactions = mapper.toEntities(
                        ((Result.Success<List<TransactionDto>>) dtoResult).getData(), bankAccountResourceId);
                transactionDao.insertTransactions(transactions);
                return new Result.Success<>(transactions);
            }
            return new Result.Error(((Result.Error) dtoResult).getError());
        }
        return new Result.Success<>(transactions);
    }

    public void delete() {
        Completable.fromAction(transactionDao::deleteAll)
                .subscribeOn(Schedulers.io()).subscribe();
       
    }
}
