package nl.management.finance.app.data.user;

import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import nl.authentication.management.app.data.login.FineAuthenticationFailedException;
import nl.authentication.management.app.data.login.PinVerificationFailedException;
import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.UserApi;
import nl.management.finance.app.data.api.rabo.RaboApi;
import retrofit2.Call;
import retrofit2.Response;

public class UserDataSource {
    private static final String TAG = "UserDataSource";
    private final UserContext userContext;
    private final UserApi api;
    private final RaboApi raboApi;

    @Inject
    public UserDataSource(UserContext userContext, UserApi api, RaboApi raboApi) {
        this.userContext = userContext;
        this.api = api;
        this.raboApi = raboApi;
    }

    Result<Boolean> hasUserRegisteredPin(UUID userId) {
        try {
            Call<Boolean> call = api.hasRegisteredPin(userId);
            Response<Boolean> response = call.clone().execute();
            if (response.isSuccessful()) {
                return new Result.Success<Boolean>(response.body());
            }
            Log.w(TAG, String.format("has-registered error, [code=%d, msg=%s]", response.code(), response.message()));
            return new Result.Error(new FineAuthenticationFailedException(response.message()));
        } catch (IOException e) {
            Log.e(TAG, "I/O error occurred: ", e);
            return new Result.Error(new FineAuthenticationFailedException("I/O error occurred", e));
        } catch (Exception e) {
            Log.e(TAG, "exception has-registered request ", e);
            return new Result.Error(new FineAuthenticationFailedException("I/O error occurred", e));
        }
    }

    public Result<Void> verifyPin(String pin, UUID userId) {
        try {
            Call<Void> call = api.verifyPin(userId, pin);
            Response<Void> response = call.clone().execute();
            handlePinVerificationResponse(response);
            return new Result.Success<Void>(null);
        } catch (WrongPinCodeException e) {
            Log.w(TAG, "non server error occured while verifying pin", e);
            return new Result.Error(new PinVerificationFailedException("Error verifying pin", e));
        } catch (Exception e) {
            Log.e(TAG, "error occured while verifying pin", e);
            return new Result.Error(new PinVerificationFailedException("Error verifying pin", e));
        }
    }

    private void handlePinVerificationResponse(Response<Void> response) throws
            WrongPinCodeException {

        Log.d(TAG, String.format("response code from pin verification: %d", response.code()));
        switch(response.code()) {
            case 202:
                return;
            case 403:
                throw new WrongPinCodeException("The entered pin code was incorrect!");
            default:
                Log.e(TAG, "error verifying pin, error: ".concat(response.message()));
        }

    }

    Result<Void> register(@NonNull UUID userId, @NonNull RegisterDto registerDto) {
        try {
            Log.w(TAG, registerDto.getBank() + " " + registerDto.getConsentCode() + " " + registerDto.getPin());
            Call<Void> call = api.register(userId, registerDto);
            Response<Void> response = call.clone().execute();
            if (response.isSuccessful()) {
                return new Result.Success<Void>(null);
            }
            throw new RuntimeException("call to register did not return successful response");
        } catch (Exception e) {
            Log.e(TAG, "error occured while registering", e);
            return new Result.Error(new RegistrationFailedException("Error while registering", e));
        }
    }

    Result<Authentication> authenticateBank(String code) {
        OAuthAdapter adapter;
        switch (userContext.getBankName()) {
            case BuildConfig.RABO_BANK_NAME:
                adapter = new RaboOAuthAdapter(raboApi);
                break;
            default:
                throw new RuntimeException(String.format("no bank with name: %s", userContext.getBankName()));
        }
        return adapter.authenticate(code);
    }
}
