package nl.management.finance.app.data.bankaccount;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.inject.Inject;

import nl.management.finance.app.UserContext;
import nl.management.finance.app.ui.overview.BankAccountView;

public class BankAccountMapper {
    private final UserContext context;

    @Inject
    public BankAccountMapper(UserContext context) {
        this.context = context;
    }

    public BankAccount toEntity(BankAccountDto dto, Double balance) {
        return new BankAccount(
                context.getBankId(), context.getUserId().toString(), dto.getName(),
                dto.getCurrency(), dto.getIban(), balance, dto.getResourceId()
        );
    }

    public List<BankAccountView> toViews(List<BankAccount> bankAccounts) {
        List<BankAccountView> result = new ArrayList<>();
        for (BankAccount bankAccount: bankAccounts) {
            result.add(toView(bankAccount));
        }
        return result;
    }

    private BankAccountView toView(BankAccount bankAccount) {
        DecimalFormat format = new DecimalFormat("###,###.##");
        String balance = format.format(bankAccount.getBalance());
        String decimalSeparator = String.valueOf(format.getDecimalFormatSymbols().getDecimalSeparator());
        if (!balance.contains(decimalSeparator)) {
            balance += decimalSeparator + "- ";
        }
        balance = Currency.getInstance(bankAccount.getCurrency()).getSymbol() + balance;

        return new BankAccountView(bankAccount.getResourceId(), bankAccount.getName(), bankAccount.getIban(),
                balance, bankAccount.getCurrency());
    }
}
