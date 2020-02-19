package nl.management.finance.app.data.payment;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import nl.management.finance.app.IntentFilterUrls;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;
import retrofit2.Call;
import retrofit2.Response;

public class RaboPaymentInitiationAdapter implements PaymentInitiationAdapter {
    private static final String TAG = "RaboPaymentInitAdapter";
    private final RaboApi api;

    public RaboPaymentInitiationAdapter(RaboApi raboApi) {
        this.api = raboApi;
    }

    @Override
    public Result<String> initiatePayment(SepaCreditTransferDto body) {
        try {
            Log.e(TAG, body.toString());
            Call<PaymentInitiationResponse> call = api.initiatePayment(getIp(),
                    IntentFilterUrls.POST_PAYMENT, "application/json", body);

            Response<PaymentInitiationResponse> response = call.clone().execute();
            if (response.isSuccessful()) {
                Log.e(TAG, response.body().toString());
                PaymentInitiationResponse responseBody = response.body();
                return new Result.Success<>(responseBody.get_links().getScaRedirect().getHref());
            }
            Log.e(TAG, String.format("payment initiation failed, error code: %d, error body: %s",
                    response.code(), response.errorBody().string()));
            return new Result.Error(new Exception(response.errorBody().string()));
        } catch (IOException e) {
            Log.e(TAG, "io error initiating sepaCreditTransfer", e);
            return new Result.Error(e);
        }
    }

    @Override
    public Result<PaymentStatusResponse> getPaymentStatus(String paymentId) {
        try {
            Call<PaymentStatusResponse> call = api.getPaymentStatus(getIp(),
                    "application/json", paymentId);

            Response<PaymentStatusResponse> response = call.clone().execute();
            if (response.isSuccessful()) {
                Log.e(TAG, response.body().toString());
                return new Result.Success<>(response.body());
            }
            Log.e(TAG, String.format("getting payment status failed, error code: %d, error body: %s",
                    response.code(), response.errorBody().string()));
            return new Result.Error(new Exception(response.errorBody().string()));
        } catch (IOException e) {
            Log.e(TAG, "io error getting payment status", e);
            return new Result.Error(e);
        }
    }

    private String getIp() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (isIPv4) {
                            return sAddr;
                        } else {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "exception getting ip", e);
        } // for now eat exceptions // TODO: what to do here?
        return "";
    }
}
