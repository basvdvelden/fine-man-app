package nl.management.finance.app.data.transaction;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.management.finance.app.data.transaction.rabo.TransactionDto;
import nl.management.finance.app.ui.transactions.TransactionView;

public class TransactionMapper {
    @Inject
    public TransactionMapper() {

    }

    public List<Transaction> toEntities(List<TransactionDto> dtos, String bankAccountResourceId) {
        List<Transaction> result = new ArrayList<>();
        for (TransactionDto dto: dtos) {
            result.add(new Transaction(bankAccountResourceId, dto.getType(), dto.getCheckId(), dto.getBookingDate(),
                    dto.getDebtorName(), dto.getUltimateDebtor(), dto.getCreditorName(), dto.getUltimateCreditor(),
                    dto.getAmount(), dto.getDescription(), dto.getInitiatingParty()));
        }
        return result;
    }

    public List<TransactionView> toViews(List<Transaction> transactions) {
        List<TransactionView> result = new ArrayList<>();
        for (Transaction transaction: transactions) {
            result.add(new TransactionView(transaction.getBookingDate(), transaction.getUltimateDebtor(),
                    transaction.getAmount(), transaction.getDescription(), transaction.getInitiatingParty(),
                    transaction.getUltimateCreditor()));
        }

        return result;
    }
}
