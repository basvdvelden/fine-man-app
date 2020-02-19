package nl.management.finance.app.data.api;

import java.util.List;

import javax.inject.Singleton;

import nl.management.finance.app.data.contact.ContactDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

@Singleton
public interface ContactApi {
    @POST("users/{userId}/contacts")
    Call<Void> createContact(@Path("userId") String userId, @Body ContactDto dto);

    @GET("users/{userId}/contacts")
    Call<List<ContactDto>> getContacts(@Path("userId") String userId);

    @DELETE("users/{userId}/contacts/{iban}")
    Call<Void> deleteContact(@Path("userId") String userId, @Path("iban") String iban);
}
