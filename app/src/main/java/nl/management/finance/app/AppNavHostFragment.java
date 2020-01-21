package nl.management.finance.app;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Observable;

import javax.inject.Inject;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.authentication.management.app.LoginNotifier;
import nl.management.finance.app.di.App;
import nl.management.finance.app.di.DaggerViewModelFactory;

public class AppNavHostFragment extends NavHostFragment {
    private static final String KEY_GRAPH_ID = "android-support-nav:fragment:graphId";
    private static final String TAG = "AppNavHostFragment";
    @Inject
    DaggerViewModelFactory viewModelFactory;
    @Inject
    LoginNotifier loginNotifier;
    private UserViewModel userViewModel;
    private NavHostViewModel navHostViewModel;
    private Boolean isLoggedIn;
    private Boolean verifyPin;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel.class);
        navHostViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NavHostViewModel.class);
        navHostViewModel.getVerifyPin().observe(this, verifyPin -> this.verifyPin = verifyPin);

        loginNotifier.getLoggedInLive().observe(this, loggedInUser -> {
            Log.e(TAG, "observed logged in user  " + verifyPin);
            isLoggedIn = loggedInUser != null;

            if (!isLoggedIn) {
                userViewModel.logout();
                getNavController().navigate(R.id.action_global_login_nav_graph);
            } else {
                if (verifyPin) {
                    userViewModel.hasUserRegisteredPin();
                    // pop login off so user cant press back to go to login
                    getNavController().popBackStack(R.id.login_nav_graph, true);
                    getNavController().navigate(R.id.action_global_pinFragment);
                }
            }
        });

        Uri uri = requireActivity().getIntent().getData();
        if (uri != null) {
            getNavController().navigate(R.id.action_global_setupFragment);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        if (isLoggedIn != null && !isLoggedIn) {
            requireView().setVisibility(View.VISIBLE);
        }
        if (isLoggedIn != null && isLoggedIn) {
            NavDestination currDestination = getNavController().getCurrentDestination();
            if (currDestination == null || verifyPin) {
                userViewModel.hasUserRegisteredPin();
                getNavController().navigate(R.id.action_global_pinFragment);
            }
            Completable.fromAction(() -> {
                // Screen before pin fragment is visible for a short time if we set view to visible too early
                // TODO: look if can sleep less
                Thread.sleep(400L);
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> requireView().setVisibility(View.VISIBLE));
        }
        navHostViewModel.setVerifyPin(true);
        super.onResume();
    }

    @Override
    public void onPause() {
        userViewModel.deletePinFromMemory();
        requireView().setVisibility(View.INVISIBLE);
        Integer currDest = getNavController().getCurrentDestination().getId();
        if (currDest == R.id.setupFragment || currDest == R.id.pinFragment) {
            navHostViewModel.setVerifyPin(false);
        }
        super.onPause();
    }

    @NonNull
    public static AppNavHostFragment create(@NavigationRes int graphResId) {
        Bundle b = null;
        if (graphResId != 0) {
            b = new Bundle();
            b.putInt(KEY_GRAPH_ID, graphResId);
        }
        AppNavHostFragment result = new AppNavHostFragment();
        if (b != null) {
            result.setArguments(b);
        }
        return result;
    }
}
