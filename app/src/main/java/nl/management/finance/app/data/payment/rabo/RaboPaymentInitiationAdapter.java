package nl.management.finance.app.data.payment.rabo;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.rabo.RaboApi;
import nl.management.finance.app.data.payment.PaymentInitiationAdapter;
import nl.management.finance.app.data.payment.Transfer;
import nl.management.finance.app.data.payment.TransferAccount;
import retrofit2.Call;
import retrofit2.Response;

public class RaboPaymentInitiationAdapter implements PaymentInitiationAdapter {
    private static final String TAG = "RaboPaymentInitAdapter";
    private final RaboApi api;
    private final UserContext context;

    public RaboPaymentInitiationAdapter(RaboApi raboApi, UserContext userContext) {
        this.api = raboApi;
        this.context = userContext;
    }

    public Result<String> initiatePayment(Transfer transfer) {
        try {
            RaboTransfer body = toRaboTransfer(transfer);
            Call<RaboPaymentInitiationResponse> call = api.initiatePayment(getIp(),
                    "http://fine-man.nl", "application/json", body);

            Response<RaboPaymentInitiationResponse> response = call.clone().execute();
            if (response.isSuccessful()) {
                Log.e(TAG, "payment initiation success!");
                Log.e(TAG, response.body().toString());
                RaboPaymentInitiationResponse responseBody = response.body();
                return new Result.Success<>(responseBody.get_links().getScaRedirect().getHref());
            }
            Log.e(TAG, String.format("payment initiation failed, error code: %d, error body: %s",
                    response.code(), response.errorBody().string()));
            return new Result.Error(new Exception(response.errorBody().string()));
        } catch (IOException e) {
            Log.e(TAG, "io error initiating sepa transfer", e);
            return new Result.Error(e);
        }
    }

    private RaboTransfer toRaboTransfer(Transfer transfer) {
        RaboTransfer result = new RaboTransfer();

        RaboPaymentInitiationAccount creditor = new RaboPaymentInitiationAccount();
        creditor.setCurrency(transfer.getCreditorAccount().getCurrency());
        creditor.setIban(transfer.getCreditorAccount().getIban());
        result.setCreditorAccount(creditor);

        // Optional
//        RaboPaymentInitiationAddress address = new RaboPaymentInitiationAddress();
//        address.setBuildingNumber(transfer.getCreditorBuildingNumber());
//        address.setCountry(transfer.getCreditorCountry());
//        address.setPostcode(transfer.getCreditorPostCode());
//        address.setTownName(transfer.getCreditorTownName());
//        address.setStreetName(transfer.getCreditorStreetName());
//        result.setCreditorAddress(address);

        RaboPaymentInitiationAccount debtor = new RaboPaymentInitiationAccount();
        debtor.setCurrency(transfer.getDebtorAccount().getCurrency());
        debtor.setIban(transfer.getDebtorAccount().getIban());
        result.setDebtorAccount(debtor);

        // Optional
        //result.setCreditorAgent("RABONL32");
        result.setCreditorName(transfer.getCreditorAccount().getName());
        result.setEndToEndIdentification(UUID.randomUUID().toString().substring(0, 35));

        RaboPaymentInitiationAmount amount = new RaboPaymentInitiationAmount();
        DecimalFormat df = new DecimalFormat("0.00");
        amount.setContent(df.format(transfer.getAmount()).replace(",", "."));
        amount.setCurrency(transfer.getCurrency());
        result.setInstructedAmount(amount);

        // Optional
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        date.setMinutes(date.getMinutes() + 30);
        //result.setRequestedExecutionDate(df.format(date));

        //result.setRemittanceInformationUnstructured(transfer.getDescription());
        // Optional
//        RaboPaymentInitiationRemittanceInformationStructured ris = new RaboPaymentInitiationRemittanceInformationStructured();
//        ris.setReference(transfer.getPaymentRef());
//        ris.setReferenceIssuer(""); // TODO:
//        ris.setReferenceType(""); // TODO:
        Log.e(TAG, result.toString());
        return result;
    }

    private String getIp() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
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
        } // for now eat exceptions
        return "";
    }
}
