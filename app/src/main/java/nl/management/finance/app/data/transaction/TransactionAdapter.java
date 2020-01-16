package nl.management.finance.app.data.transaction;

import java.util.List;

import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.transaction.rabo.TransactionDto;

public interface TransactionAdapter {
    Result<List<TransactionDto>> getTransactions(String bankAccountResourceId);
}
