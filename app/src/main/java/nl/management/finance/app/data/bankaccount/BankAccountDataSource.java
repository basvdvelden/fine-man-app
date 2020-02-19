package nl.management.finance.app.data.bankaccount;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.AppDataSource;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;
import retrofit2.Call;
import retrofit2.Response;

public class BankAccountDataSource extends AppDataSource {
    private static final String TAG = "BankAccountDataSource";
    private final RaboApi raboApi;
    private final BankAccountApi api;
    private final UserContext context;

    @Inject
    public BankAccountDataSource(RaboApi raboApi, BankAccountApi api, UserContext context) {
        this.raboApi = raboApi;
        this.api = api;
        this.context = context;
    }

    public Result<List<BankAccountDto>> getBankAccounts() {
        BankAccountAdapter adapter;
        switch (context.getBankName()) {
            case BuildConfig.RABO_BANK_NAME:
                adapter = new RaboBankAccountAdapter(raboApi, context);
                break;
            default:
                throw new RuntimeException(String.format("no bank with name: %s", context.getBankName()));
        }
        return adapter.getBankAccounts();
    }

    public void saveBankAccounts(List<BankAccountDto> dtos) {
        try {
            Call<Void> call = api.saveBankAccounts(context.getUserId().toString(), dtos);
            Response<Void> response = call.clone().execute();
            if (!response.isSuccessful()) {
                throw new IOException(getErrMsg(response));
            }
        } catch (IOException e) {
            Log.e(TAG, "IO error saving bank accounts.", e);
        }
    }
}
