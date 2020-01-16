package nl.management.finance.app.data.bankaccount;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;

public class BankAccountRepository {
    private static final String TAG = "BankAccountRepository";

    private final BankAccountDataSource dataSource;
    private final BankAccountDao bankAccountDao;
    private final UserContext userContext;
    private final BankAccountMapper mapper;

    @Inject
    public BankAccountRepository(BankAccountDataSource dataSource, BankAccountDao bankAccountDao,
                                 UserContext userContext, BankAccountMapper mapper) {
        this.dataSource = dataSource;
        this.bankAccountDao = bankAccountDao;
        this.userContext = userContext;
        this.mapper = mapper;
    }

    public Result<List<BankAccount>> getBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountDao.getBankAccounts(userContext.getUserId().toString());
        if (bankAccounts.size() < 1) {
            Result<List<BankAccount>> result = getBankAccountsRemotely();
            if (result instanceof Result.Success) {
                bankAccounts = ((Result.Success<List<BankAccount>>) result).getData();
                bankAccountDao.insertBankAccounts(bankAccounts);
            }
            return result;
        }
        return  new Result.Success<List<BankAccount>>(bankAccounts);
    }

    public Result<List<BankAccount>> getBankAccountsRemotely() {
        List<BankAccount> data = new ArrayList<>();

        Result<List<BankAccountDto>> bankAccountDtos = dataSource.getBankAccounts();
        if (bankAccountDtos instanceof Result.Success) {
            List<BankAccountDto> dtos = ((Result.Success<List<BankAccountDto>>) bankAccountDtos).getData();
            for (BankAccountDto dto: dtos) {
                Result<Double> balanceResult = getBalance(dto.getResourceId());

                if (balanceResult instanceof Result.Success) {
                    Double balance = ((Result.Success<Double>) balanceResult).getData();
                    data.add(mapper.toEntity(dto, balance));
                } else {
                    return new Result.Error(((Result.Error) balanceResult).getError());
                }
            }
        } else {
            return new Result.Error(((Result.Error) bankAccountDtos).getError());
        }
        return new Result.Success<>(data);
    }

    private Result<Double> getBalance(String resourceId) {
        return dataSource.getBalance(resourceId);
    }

    public void saveBankAccounts(List<BankAccount> bankAccounts) {
        bankAccountDao.insertBankAccounts(bankAccounts);
    }

    public BankAccount getByIban(String iban) {
        return bankAccountDao.getByIban(iban);
    }
}
