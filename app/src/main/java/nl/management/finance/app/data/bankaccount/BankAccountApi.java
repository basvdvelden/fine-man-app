package nl.management.finance.app.data.bankaccount;

import java.util.List;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

@Singleton
public interface BankAccountApi {
    @PUT("users/{userId}/bank-accounts")
    Call<Void> saveBankAccounts(@Path("userId") String userId, @Body List<BankAccountDto> dtos);
}
