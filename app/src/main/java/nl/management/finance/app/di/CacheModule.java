package nl.management.finance.app.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.management.finance.app.data.storage.UserCache;

@Module
public class CacheModule {
    @Provides
    @Singleton
    UserCache provideUserCache() {
        return new UserCache();
    }
}
