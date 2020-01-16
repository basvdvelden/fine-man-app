package nl.management.finance.app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.R;
import nl.management.finance.app.UserViewModel;
import nl.management.finance.app.di.DaggerViewModelFactory;


public class RootFragment extends Fragment {
    private UserViewModel userViewModel;
    private NavController navController;

    @Inject
    DaggerViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_root, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel.class);
        this.navController = Navigation.findNavController(view);
        userViewModel.hasRegisteredPin().observe(this, (hasRegistered) -> {
            if (hasRegistered.getSuccess() != null) {
                if (hasRegistered.getSuccess() && navController.getCurrentDestination().getId() == R.id.rootFragment) {
                    navController.navigate(R.id.action_rootFragment_to_mainFragment);
                }
            }
        });
    }
}
