package nl.management.finance.app.data.api.rabo;

import javax.inject.Singleton;

import nl.management.finance.app.data.bankaccount.RaboBankAccounts;
import nl.management.finance.app.data.payment.rabo.RaboTransfer;
import nl.management.finance.app.data.payment.rabo.RaboPaymentInitiationResponse;
import nl.management.finance.app.data.transaction.rabo.RaboTransactions;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Singleton
public interface RaboApi {
    @FormUrlEncoded
    @POST("oauth2/token")
    Call<RaboOAuthResponse> authenticate(@Header("Authorization") String authHeader,
                                         @Field("grant_type") String grantType,
                                         @Field("code") String consentCode);

    @GET("payments/account-information/ais/v3/accounts/{accountId}/balances")
    Call<RaboAccountBalance> getBalance(@Path("accountId") String accountId);

    @GET("payments/account-information/ais/v3/accounts")
    Call<RaboBankAccounts> getAccounts();

    @GET("payments/account-information/ais/v3/accounts/{accountId}/transactions")
    Call<RaboTransactions> getTransactions(@Path("accountId") String accountId,
                                           @Query("bookingStatus") String bookingStatus);

    @POST("payments/payment-initiation/pis/v1/payments/sepa-credit-transfers")
    Call<RaboPaymentInitiationResponse> initiatePayment(@Header("PSU-IP-Address") String userIp,
                                                        @Header("TPP-Redirect-URI") String redirectUri,
                                                        @Header("Content-Type") String contentType,
                                                        @Body RaboTransfer body);
}
