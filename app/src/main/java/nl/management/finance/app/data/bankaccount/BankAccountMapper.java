package nl.management.finance.app.data.bankaccount;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.bank.Bank;
import nl.management.finance.app.ui.overview.BankAccountView;

public class BankAccountMapper {
    private final UserContext context;

    @Inject
    public BankAccountMapper(UserContext context) {
        this.context = context;
    }

    public List<BankAccount> toEntity(List<BankAccountDto> dtos) {
        List<BankAccount> bankAccounts = new ArrayList<>();
        for (BankAccountDto dto: dtos) {
            bankAccounts.add(toEntity(dto));
        }
        return bankAccounts;
    }

    public BankAccount toEntity(BankAccountDto dto) {
        BankAccount bankAccount;
        if (dto.getId() != null) {
            bankAccount = new BankAccount(dto.getId().toString(), context.getBankId(), context.getUserId().toString(),
                    dto.getName(), dto.getCurrency(), dto.getIban(), dto.getBalance(), dto.getResourceId());
        } else {
            bankAccount = new BankAccount(context.getBankId(), context.getUserId().toString(),
                    dto.getName(), dto.getCurrency(), dto.getIban(), dto.getBalance(), dto.getResourceId());
        }
        return bankAccount;
    }

    public List<BankAccountView> toView(List<BankAccount> bankAccounts) {
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

        return new BankAccountView(UUID.fromString(bankAccount.getId()), bankAccount.getResourceId(),
                bankAccount.getName(), bankAccount.getIban(),
                balance, bankAccount.getCurrency());
    }

    public List<BankAccountDto> toDto(List<BankAccount> bankAccounts) {
        List<BankAccountDto> dtos = new ArrayList<>();
        for (BankAccount bankAccount: bankAccounts) {
            BankAccountDto dto = new BankAccountDto();
            dto.setId(UUID.fromString(bankAccount.getId()));
            dto.setUserId(UUID.fromString(bankAccount.getUserId()));
            dto.setBankId(bankAccount.getBankId());
            dto.setCurrency(bankAccount.getCurrency());
            dto.setName(bankAccount.getName());
            dto.setIban(bankAccount.getIban());
            dto.setResourceId(bankAccount.getResourceId());
            dto.setBalance(bankAccount.getBalance());
            dtos.add(dto);
        }
        return dtos;
    }
}
