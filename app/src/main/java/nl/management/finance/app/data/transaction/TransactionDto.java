package nl.management.finance.app.data.transaction;

import java.util.UUID;

public class TransactionDto {
    private UUID bankAccountId;
    private String checkId;
    private String type;
    private String bookingDate;
    private String debtorName;
    private String ultimateDebtor;
    private String creditorName;
    private String ultimateCreditor;
    private String amount;
    private String description;
    private String initiatingParty;

    public TransactionDto(UUID bankAccountId, String checkId, String type, String bookingDate, String debtorName,
                          String ultimateDebtor, String creditorName, String ultimateCreditor, String amount,
                          String description, String initiatingParty) {
        this.bankAccountId = bankAccountId;
        this.checkId = checkId;
        this.type = type;
        this.bookingDate = bookingDate;
        this.debtorName = debtorName;
        this.ultimateDebtor = ultimateDebtor;
        this.creditorName = creditorName;
        this.ultimateCreditor = ultimateCreditor;
        this.amount = amount;
        this.description = description;
        this.initiatingParty = initiatingParty;
    }

    public String getType() {
        return type;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getInitiatingParty() {
        return initiatingParty;
    }

    public String getUltimateDebtor() {
        return ultimateDebtor;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public String getUltimateCreditor() {
        return ultimateCreditor;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public String getCheckId() {
        return checkId;
    }
}
