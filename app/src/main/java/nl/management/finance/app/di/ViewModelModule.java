package nl.management.finance.app.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import nl.authentication.management.app.di.LoginModule;
import nl.authentication.management.app.ui.AuthViewModel;
import nl.management.finance.app.NavHostViewModel;
import nl.management.finance.app.UserViewModel;
import nl.management.finance.app.ui.transfer.TransferViewModel;
import nl.management.finance.app.ui.overview.OverviewViewModel;
import nl.management.finance.app.ui.pin.VerifyPinViewModel;
import nl.management.finance.app.ui.pin.RegisterPinViewModel;
import nl.management.finance.app.ui.setup.SetupViewModel;
import nl.management.finance.app.ui.transactions.TransactionViewModel;

@Module(includes = LoginModule.class)
public abstract class ViewModelModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(VerifyPinViewModel.class)
    public abstract ViewModel bindPinCodeViewModel(VerifyPinViewModel verifyPinViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterPinViewModel.class)
    public abstract ViewModel bindRegisterPinViewModel(RegisterPinViewModel registerPinViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    public abstract ViewModel bindFinemanAuthViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindLoginViewModel(AuthViewModel authViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel.class)
    public abstract ViewModel bindSetupViewModel(SetupViewModel setupViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NavHostViewModel.class)
    public abstract ViewModel bindNavHostViewModel(NavHostViewModel navHostViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel.class)
    public abstract ViewModel bindOverviewViewModel(OverviewViewModel overviewViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel.class)
    public abstract ViewModel bindTransactionViewModel(TransactionViewModel transactionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransferViewModel.class)
    public abstract ViewModel bindPaymentViewModel(TransferViewModel transferViewModel);
}
