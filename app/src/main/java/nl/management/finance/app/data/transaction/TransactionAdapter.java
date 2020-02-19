package nl.management.finance.app.data.transaction;

import java.util.List;
import java.util.UUID;

import nl.management.finance.app.data.Result;

public interface TransactionAdapter {
    Result<List<TransactionDto>> getTransactions(String bankAccountResourceId, UUID bankAccountId);
}
