package nl.management.finance.app.data.bankaccount;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public RaboBankAccountAdapter(RaboApi api) {
        this.api = api;
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
            return new Result.Success<>(toBankAccountDtos(raboAccounts));
        } catch (IOException e) {
            Log.e(TAG, "io error fetching rabo bank accounts.", e);
            return new Result.Error(e);
        }
    }

    private List<BankAccountDto> toBankAccountDtos(List<RaboBankAccount> raboAccounts) {
        List<BankAccountDto> result = new ArrayList<>();
        for (RaboBankAccount raboAccount: raboAccounts) {
            result.add(new BankAccountDto(raboAccount.getName(), raboAccount.getCurrency(),
                    raboAccount.getIban(), raboAccount.getResourceId()));
        }

        return result;
    }

    public Result<Double> getBalance(String resourceId) {
        try {
            Call<RaboAccountBalance> call = api.getBalance(resourceId);
            Response<RaboAccountBalance> response = call.clone().execute();
            if (!response.isSuccessful()) {
                Exception error = new Exception(response.errorBody().string());
                Log.e(TAG, "error getting rabobank account balance", error);
                return new Result.Error(error);
            }
            return new Result.Success<>(toBalanceDto(response.body()));
        } catch (IOException e) {
            Log.e(TAG, "io error fetching rabo balance.", e);
            return new Result.Error(e);
        }
    }

    private Double toBalanceDto(RaboAccountBalance raboAccountBalance) {
        String balanceType = "expected";
        for (RaboBalance balance: raboAccountBalance.getBalances()) {
            if (balance.getBalanceType().equals(balanceType)) {
               return balance.getBalanceAmount().getAmount();
            }
        }
        throw new RuntimeException(String.format("no balance with type: %s", balanceType));
    }
}
