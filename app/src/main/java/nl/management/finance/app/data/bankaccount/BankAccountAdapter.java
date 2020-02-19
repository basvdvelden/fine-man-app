package nl.management.finance.app.data.bankaccount;


import java.util.List;

import nl.management.finance.app.data.Result;

public interface BankAccountAdapter {
    Result<List<BankAccountDto>> getBankAccounts();
}
