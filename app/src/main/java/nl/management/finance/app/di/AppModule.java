package nl.management.finance.app.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private static final String FINE_PREF_NAME = "fine-man";

    @Singleton
    @Provides
    @Named("fine")
    public SharedPreferences provideFineSharedPreferences(Application context) {
        return context.getSharedPreferences(FINE_PREF_NAME, Context.MODE_PRIVATE);
    }
}
