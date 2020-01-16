package nl.management.finance.app.data.api;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.data.user.Authentication;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Singleton
public class RaboTokenInterceptor implements Interceptor {
    private static final String TAG = "RaboTokenInterceptor";
    private final BankAuthNotifier authNotifier;
    private Authentication auth;

    @Inject
    public RaboTokenInterceptor(BankAuthNotifier authNotifier) {
        this.authNotifier = authNotifier;
        this.authNotifier.getAuthentication().subscribe(authentication -> this.auth = authentication);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        if (shouldFilter(originalRequest.url().toString())) {
            Log.d(TAG, String.format("filtering: url=%s", originalRequest.url().toString()));
            if (this.auth == null) {
                throw new IllegalStateException("bank auth info was null");
            }
            if (shouldRefreshAccessToken()) {
                Log.d(TAG, "bank token expired");
                Request tokenRequest = createRefreshRequest(originalRequest);
                Response response = chain.proceed(tokenRequest);
                if (!response.isSuccessful()) {
                    Log.d(TAG, String.format("refreshing bank token failed, error: %s", response.body().string()));
                    response.close();
                    response = chain.proceed(tokenRequest);
                    if (!response.isSuccessful()) {
                        Log.w(TAG, String.format("refreshing bank token failed again, giving up. code: %d, error: %s",
                                response.code(), response.body().string()));
                        response.close();
                        return chain.proceed(originalRequest);
                    }
                }
                Authentication auth = new Gson().fromJson(response.body().string(), Authentication.class);
                authNotifier.updateAuthentication(auth);
            }
            originalRequest = originalRequest.newBuilder().addHeader(
                    "Authorization",
                    this.auth.getTokenType() + " " + this.auth.getAccessToken())
                .build();
        }
        return chain.proceed(originalRequest);
    }

    private boolean shouldFilter(String url) {
        if (url.contains(BuildConfig.RABO_API_URL) && !url.contains("oauth")) {
            return true;
        }
        return false;
    }

    private boolean shouldRefreshAccessToken() {
        return (System.currentTimeMillis() - 5000) > auth.getExpiresAt();
    }

    private Request createRefreshRequest(Request originalRequest) {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", auth.getRefreshToken())
                .build();
        return originalRequest
                .newBuilder()
                .post(body)
                .url(BuildConfig.RABO_API_URL + "oauth2/token")
                .addHeader("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s",
                        BuildConfig.RABO_CLIENT_ID, BuildConfig.RABO_CLIENT_SECRET).getBytes(), Base64.NO_WRAP))
                .build();
    }
}
