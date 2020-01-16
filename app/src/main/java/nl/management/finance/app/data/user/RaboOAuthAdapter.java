package nl.management.finance.app.data.user;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;
import nl.management.finance.app.data.api.rabo.RaboOAuthResponse;
import retrofit2.Call;
import retrofit2.Response;

public class RaboOAuthAdapter implements OAuthAdapter {
    private static final String TAG = "RaboOAuthAdapter";
    private RaboApi api;

    public RaboOAuthAdapter(RaboApi api) {
        this.api = api;
    }

    @Override
    public Result<Authentication> authenticate(String code) {
        try {
            String authHeader = "Basic " + Base64.encodeToString(String.format(
                    "%s:%s", BuildConfig.RABO_CLIENT_ID, BuildConfig.RABO_CLIENT_SECRET).getBytes(),
                    Base64.NO_WRAP);

            Call<RaboOAuthResponse> call = api.authenticate(authHeader, "authorization_code", code);
            Response<RaboOAuthResponse> response = call.clone().execute();
            if (!response.isSuccessful()) {
                Exception error = new Exception(response.errorBody().string());
                Log.e(TAG, String.format("response authenticating at rabobank unsuccessful, [code=%d, errorBody=%s]",
                        response.code(), response.errorBody().string()), error);
                return new Result.Error(error);
            }
            return new Result.Success<>(generify(response.body()));
        } catch (IOException e) {
            Log.e(TAG, "io error authenticating at rabobank, ERROR: ", e);
            return new Result.Error(e);
        } catch (Exception e) {
            Log.e(TAG, "error authenticating at rabobank, ERROR: ", e);
            return new Result.Error(e);
        }
    }

    private Authentication generify(RaboOAuthResponse raboAuth) {
        Log.d(TAG, raboAuth.toString());

        Authentication result = new Authentication();
        result.setAccessToken(raboAuth.getAccessToken());
        result.setExpiresAt(System.currentTimeMillis() + raboAuth.getExpiresIn() * 1000);
        result.setRefreshToken(raboAuth.getRefreshToken());
        result.setScope(raboAuth.getScope());
        result.setTokenType(raboAuth.getTokenType());

        return result;
    }
}
