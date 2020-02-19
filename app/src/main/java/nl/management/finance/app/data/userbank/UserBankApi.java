package nl.management.finance.app.data.userbank;

import java.util.List;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

@Singleton
public interface UserBankApi {
    @GET("users/{userId}/user-banks")
    Call<List<UserBankDto>> getUserBanks(@Path("userId") String userId);

    @POST("users/{userId}/user-banks")
    Call<Void> createUserBank(@Path("userId") String userId, @Body UserBankDto dto);

    @PUT("users/{userId}/user-banks")
    Call<Void> updateUserBank(@Path("userId") String userId, @Body UserBankDto dto);
}
