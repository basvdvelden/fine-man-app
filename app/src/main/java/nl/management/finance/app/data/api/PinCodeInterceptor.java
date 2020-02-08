package nl.management.finance.app.data.api;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.UserSubject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PinCodeInterceptor implements Interceptor {
    private static final String TAG = "PinCodeInterceptor";
    private String pinCode;

    @Inject
    public PinCodeInterceptor(UserSubject userSubject) {
        userSubject.getPin().subscribe(pin -> pinCode = pin.get());
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        if (shouldFilter(request)) {
            Log.d(TAG, String.format("filtering: url=%s", request.url().toString()));
            request = request.newBuilder().addHeader("Pin-Code", pinCode).build();
        }
        return chain.proceed(request);
    }

    private boolean shouldFilter(Request request) {
        String url = request.url().toString();
        if (url.contains(BuildConfig.API_URL) && !url.contains("/register")
                && !url.contains("/has-registered") && !url.contains("/pin/verify")) {
            return true;
        }
        return false;
    }
}
