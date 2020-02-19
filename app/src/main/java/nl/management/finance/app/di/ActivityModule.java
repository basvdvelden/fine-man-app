package nl.management.finance.app.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import nl.management.finance.app.AppNavHostFragment;
import nl.management.finance.app.MainActivity;
import nl.management.finance.app.ui.RootFragment;
import nl.management.finance.app.ui.contacts.ContactsFragment;
import nl.management.finance.app.ui.overview.OverviewFragment;
import nl.management.finance.app.ui.pin.PinFragment;
import nl.management.finance.app.ui.postpayment.PostPaymentFragment;
import nl.management.finance.app.ui.setup.SetupFragment;
import nl.management.finance.app.ui.transactions.TransactionFragment;
import nl.management.finance.app.ui.transfer.TransferFragment;

@Module(includes = nl.authentication.management.app.di.ActivityModule.class)
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivityInjector();
    @ContributesAndroidInjector
    abstract OverviewFragment contributeMainFragmentInjector();
    @ContributesAndroidInjector
    abstract PinFragment contributePinFragmentInjector();
    @ContributesAndroidInjector
    abstract AppNavHostFragment contributeAppNavHostFragmentInjector();
    @ContributesAndroidInjector
    abstract SetupFragment contributeSetupFragmentInjector();
    @ContributesAndroidInjector
    abstract RootFragment contributeRootFragmentInjector();
    @ContributesAndroidInjector
    abstract TransactionFragment contributeTransactionFragmentInjector();
    @ContributesAndroidInjector
    abstract TransferFragment contributeTransferFragmentInjector();
    @ContributesAndroidInjector
    abstract PostPaymentFragment contributePostPaymentFragmentInjector();
    @ContributesAndroidInjector
    abstract ContactsFragment contributeContactsFragmentInjector();
}
