package nl.management.finance.app.data.transaction.rabo;

public class TransactionDto {
    private String type;
    private String checkId;
    private String bookingDate;
    private String debtorName;
    private String ultimateDebtor;
    private String creditorName;
    private String ultimateCreditor;
    private String amount;
    private String description;
    private String initiatingParty;

    TransactionDto(String type, String checkId, String bookingDate, String debtorName,
                   String ultimateDebtor, String creditorName, String ultimateCreditor, String amount,
                   String description, String initiatingParty) {
        this.type = type;
        this.checkId = checkId;
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

    public String getCheckId() {
        return checkId;
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
}
