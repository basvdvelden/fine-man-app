package nl.management.finance.app;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import dagger.android.support.AndroidSupportInjection;
import nl.authentication.management.app.ui.AuthViewModel;
import nl.management.finance.app.di.DaggerViewModelFactory;

public class AppNavHostFragment extends NavHostFragment {
    private static final String TAG = "AppNavHostFragment";
    @Inject
    DaggerViewModelFactory viewModelFactory;
    private AuthViewModel loginViewModel;
    private UserViewModel userViewModel;
    private NavHostViewModel navHostViewModel;
    private Boolean isLoggedIn;
    private Boolean verifyPin;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        loginViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AuthViewModel.class);
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel.class);
        navHostViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NavHostViewModel.class);
        navHostViewModel.getVerifyPin().observe(this, verifyPin -> this.verifyPin = verifyPin);

        loginViewModel.isLoggedIn().observe(this, loggedIn -> {
            isLoggedIn = loggedIn;
            if (!loggedIn) {
                userViewModel.logout();
                getNavController().navigate(R.id.action_global_login_nav_graph);

            } else {
                userViewModel.setCurrentUser(loginViewModel.getCurrentUser());

                if (verifyPin) {
                    userViewModel.hasUserRegisteredPin();
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
            requireView().setVisibility(View.VISIBLE);
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
}
