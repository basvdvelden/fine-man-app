package nl.management.finance.app.data.bankaccount;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserSubject;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.ui.overview.BankAccountView;

@Singleton
public class BankAccountRepository {
    private static final String TAG = "BankAccountRepository";

    private final BankAccountDataSource dataSource;
    private final BankAccountDao bankAccountDao;
    private final UserContext userContext;
    private final BankAccountMapper mapper;
    private LiveData<List<BankAccount>> bankAccounts;

    @Inject
    public BankAccountRepository(BankAccountDataSource dataSource, BankAccountDao bankAccountDao,
                                 UserContext userContext, BankAccountMapper mapper,
                                 UserSubject userSubject) {
        this.dataSource = dataSource;
        this.bankAccountDao = bankAccountDao;
        this.userContext = userContext;
        this.mapper = mapper;
        bankAccounts = bankAccountDao.getBankAccounts(userContext.getUserId().toString());

        Completable.fromAction(() -> {
            // TODO: Bank accounts are refreshed twice (here and in fragment)
            if (bankAccounts.getValue() == null || bankAccounts.getValue().size() < 1) {
                refreshBankAccounts();
            }
        }).subscribeOn(Schedulers.io())
        .subscribe();
    }

    public LiveData<List<BankAccount>> getBankAccounts() {
        return bankAccounts;
    }

    public void refreshBankAccounts() {
        Result<List<BankAccountDto>> bankAccountDtos = dataSource.getBankAccounts();
        if (bankAccountDtos instanceof Result.Success) {
            List<BankAccountDto> dtos = ((Result.Success<List<BankAccountDto>>) bankAccountDtos).getData();
            List<BankAccount> actedOn = bankAccountDao.upsertBankAccounts(mapper.toEntity(dtos));
            boolean bool = true;
            if (bool == true || actedOn.size() > 0) {
                dataSource.saveBankAccounts(mapper.toDto(actedOn));
            }
        } else {
            // TODO:
        }
    }

    public void saveBankAccounts(List<BankAccount> bankAccounts) {
        bankAccountDao.insertBankAccounts(bankAccounts);
    }

    public BankAccount getByIban(String iban) {
        return bankAccountDao.getByIban(iban);
    }
}
