package nl.management.finance.app.ui.overview;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;
import nl.authentication.management.app.ui.AuthViewModel;
import nl.management.finance.app.R;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserViewModel;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.overview.model.BankAccountView;

public class OverviewFragment extends Fragment {
    private static final String TAG = "OverviewFragment";

    private NavController navController;
    private AuthViewModel loginViewModel;
    private UserViewModel userViewModel;
    private OverviewViewModel overviewViewModel;
    private ProgressBar loadingProgressBar;
    private RecyclerView recyclerView;
    private BankAccountBalancesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<BankAccountView> bankAccounts = new ArrayList<>();

    @Inject
    DaggerViewModelFactory viewModelFactory;
    @Inject
    UserContext context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        navController = Navigation.findNavController(requireView());
        loginViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(AuthViewModel.class);
        userViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(UserViewModel.class);
        overviewViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OverviewViewModel.class);
        loadingProgressBar = requireView().findViewById(R.id.loading);

        recyclerView = requireView().findViewById(R.id.bank_account_balances);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BankAccountBalancesAdapter(bankAccounts);

        adapter.getPositionClicks().subscribe((bankAccount) -> {
            Log.d(TAG, navController.getCurrentDestination().getLabel().toString());
            Bundle args = new Bundle();
            args.putString("iban", bankAccount.getIban());
            navController.navigate(R.id.action_mainFragment_to_transactionFragment, args);
        });
        recyclerView.setAdapter(adapter);

        overviewViewModel.getLiveBankAccounts().observe(this,
                (bankAccountViews) -> {
            bankAccounts.clear();
            bankAccounts.addAll(bankAccountViews);
            adapter.notifyDataSetChanged();
        });

        boolean exitOnBack = requireArguments().getBoolean("exitOnBack");
        if (exitOnBack) {
            requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            });
        }
        setListeners();
    }

    private void setListeners() {
        Button mClickButton2 = requireView().findViewById(R.id.button2);
        mClickButton2.setOnClickListener(v -> {
            loginViewModel.logout();
            userViewModel.logout();
        });
    }

    private void showErrorString(@StringRes Integer error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
}
