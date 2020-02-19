package nl.management.finance.app.data.userbank;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.AppDataSource;
import nl.management.finance.app.data.Result;
import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class UserBankDataSource extends AppDataSource {
    private static final String TAG = "UserBankDataSource";
    private final UserBankApi api;
    private final UserContext userContext;

    @Inject
    public UserBankDataSource(UserBankApi api, UserContext userContext) {
        this.api = api;
        this.userContext = userContext;
    }

    public Result<List<UserBankDto>> getUserBanks() {
        try {
            Call<List<UserBankDto>> call = api.getUserBanks(userContext.getUserId().toString());
            Response<List<UserBankDto>> response = call.clone().execute();
            if (!response.isSuccessful()) {
                throw new IOException(getErrMsg(response));
            }
            return new Result.Success<>(response.body());
        } catch (IOException e) {
            Log.e(TAG, "io error getting user banks:", e);
            return new Result.Error(e);
        }
    }

    public Result<Void> createUserBank(UserBankDto dto) {
        try {
            Call<Void> call = api.createUserBank(userContext.getUserId().toString(), dto);
            Response<Void> response = call.clone().execute();
            if (!response.isSuccessful()) {
                throw new IOException(getErrMsg(response));
            }
            return new Result.Success<>(response.body());
        } catch (IOException e) {
            Log.e(TAG, "io error creating user bank:", e);
            return new Result.Error(e);
        }
    }

    public Result<Void> updateUserBank(UserBankDto dto) {
        try {
            Call<Void> call = api.updateUserBank(userContext.getUserId().toString(), dto);
            Response<Void> response = call.clone().execute();
            if (!response.isSuccessful()) {
                throw new IOException(getErrMsg(response));
            }
            return new Result.Success<>(response.body());
        } catch (IOException e) {
            Log.e(TAG, "io error updating user bank:", e);
            return new Result.Error(e);
        }
    }
}
