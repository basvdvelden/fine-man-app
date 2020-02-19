package nl.management.finance.app.di;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.data.api.ContactApi;
import nl.management.finance.app.data.api.PinCodeInterceptor;
import nl.management.finance.app.data.api.RaboHeaderInterceptor;
import nl.management.finance.app.data.api.RaboTokenInterceptor;
import nl.management.finance.app.data.api.UserApi;
import nl.management.finance.app.data.api.rabo.RaboApi;
import nl.management.finance.app.data.bankaccount.BankAccountApi;
import nl.management.finance.app.data.userbank.UserBankApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class AppApiModule {

    @Provides
    @Singleton
    @Named("fine")
    public Retrofit provideFineRetrofit(Gson gson, OkHttpClient client, PinCodeInterceptor pinCodeInterceptor) {
        client = client.newBuilder()
                .addInterceptor(pinCodeInterceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    @Named("rabo")
    public Retrofit provideRaboRetrofit(Gson gson, OkHttpClient client, RaboTokenInterceptor tokenInterceptor) {
        client = client.newBuilder()
                .addInterceptor(new RaboHeaderInterceptor())
                .addInterceptor(tokenInterceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.RABO_API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    public UserApi provideUserApi(@Named("fine") Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

    @Provides
    @Singleton
    public UserBankApi provideUserBankApi(@Named("fine") Retrofit retrofit) {
        return retrofit.create(UserBankApi.class);
    }

    @Provides
    @Singleton
    public ContactApi provideContactApi(@Named("fine") Retrofit retrofit) {
        return retrofit.create(ContactApi.class);
    }

    @Provides
    @Singleton
    public BankAccountApi provideBankAccountApi(@Named("fine") Retrofit retrofit) {
        return retrofit.create(BankAccountApi.class);
    }

    @Provides
    @Singleton
    public RaboApi provideRaboApi(@Named("rabo") Retrofit retrofit) {
        return retrofit.create(RaboApi.class);
    }
}
