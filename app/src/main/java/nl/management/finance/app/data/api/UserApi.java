package nl.management.finance.app.data.api;

import java.util.UUID;

import javax.inject.Singleton;

import nl.management.finance.app.data.user.RegisterDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

@Singleton
public interface UserApi {

    @GET("users/{userId}/has-registered")
    Call<Boolean> hasRegisteredPin(@Path("userId") UUID userId);

    @POST("users/{userId}/register")
    Call<Void> register(@Path("userId") UUID userId, @Body RegisterDto registerDto);

    @FormUrlEncoded
    @POST("users/{userId}/pin/verify")
    Call<Void> verifyPin(@Path("userId") UUID userId,
                        @Field("pin") String pin);
}
