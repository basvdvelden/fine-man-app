package nl.management.finance.app.data.bankaccount;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboAccountBalance;
import nl.management.finance.app.data.api.rabo.RaboApi;
import nl.management.finance.app.data.api.rabo.RaboBalance;
import nl.management.finance.app.data.api.rabo.RaboBankAccount;
import retrofit2.Call;
import retrofit2.Response;

public class RaboBankAccountAdapter implements BankAccountAdapter {
    private static final String TAG = "RaboBankAccountAdapter";
    private final RaboApi api;
    private final UserContext userContext;

    public RaboBankAccountAdapter(RaboApi api, UserContext userContext) {
        this.api = api;
        this.userContext = userContext;
    }

    public Result<List<BankAccountDto>> getBankAccounts() {
        try {
            Call<RaboBankAccounts> call = api.getAccounts();
            Response<RaboBankAccounts> response = call.clone().execute();
            if (!response.isSuccessful()) {
                Exception error = new Exception(response.errorBody().string());
                Log.e(TAG, "error getting rabobank bank accounts", error);
                return new Result.Error(error);
            }
            List<RaboBankAccount> raboAccounts = response.body().getAccounts();
            List<RaboAccountBalance> raboBalances = new ArrayList<>();
            for (RaboBankAccount bankAccount: raboAccounts) {
                raboBalances.add(getBalance(bankAccount.getResourceId()));
            }
            return new Result.Success<>(toBankAccountDtos(raboAccounts, raboBalances));
        } catch (IOException e) {
            Log.e(TAG, "io error fetching rabo bank accounts.", e);
            return new Result.Error(e);
        }
    }

    private RaboAccountBalance getBalance(String resourceId) throws IOException {
        Response<RaboAccountBalance> response = api.getBalance(resourceId).clone().execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null ? response.errorBody().string() : response.toString());
        }
        return response.body();
    }

    private List<BankAccountDto> toBankAccountDtos(List<RaboBankAccount> raboAccounts,
                                                   List<RaboAccountBalance> raboBalances) {
        List<BankAccountDto> result = new ArrayList<>();
        for (int i = 0; i < raboAccounts.size(); i ++) {
            RaboBankAccount raboAccount = raboAccounts.get(i);
            result.add(new BankAccountDto(userContext.getUserId(), BuildConfig.RABO_BANK_ID,
                    raboAccount.getName(), raboAccount.getCurrency(), raboAccount.getIban(),
                    raboAccount.getResourceId(), toDouble(raboBalances.get(i))));
        }

        return result;
    }

    private Double toDouble(RaboAccountBalance raboAccountBalance) {
        String balanceType = "expected";
        for (RaboBalance balance: raboAccountBalance.getBalances()) {
            if (balance.getBalanceType().equals(balanceType)) {
               return balance.getBalanceAmount().getAmount();
            }
        }
        throw new RuntimeException(String.format("no balance with type: %s", balanceType));
    }
}
