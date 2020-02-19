package nl.management.finance.app.ui.overview;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dagger.android.support.AndroidSupportInjection;
import nl.authentication.management.app.ui.AuthViewModel;
import nl.management.finance.app.R;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.transactions.TransactionViewModel;

public class OverviewFragment extends Fragment {
    private static final String TAG = "OverviewFragment";

    private NavController navController;
    private AuthViewModel loginViewModel;
    private OverviewViewModel overviewViewModel;
    private ProgressBar loadingProgressBar;
    private RecyclerView recyclerView;
    private BankAccountBalancesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mBaRefreshLayout;

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
        overviewViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OverviewViewModel.class);
        loadingProgressBar = requireView().findViewById(R.id.loading);

        mBaRefreshLayout = view.findViewById(R.id.ba_swipe_refresh);
        recyclerView = requireView().findViewById(R.id.bank_account_balances);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BankAccountBalancesAdapter(bankAccounts);

        adapter.getPositionClicks().subscribe((bankAccount) -> {
            // TODO: Use shared viewModel instead of bundle maybe?
            TransactionViewModel viewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(TransactionViewModel.class);
            viewModel.setBankAccountId(bankAccount.getId());
            viewModel.setBankAccountResourceId(bankAccount.getResourceId());
            navController.navigate(R.id.action_mainFragment_to_transactionFragment);
        });
        recyclerView.setAdapter(adapter);

        overviewViewModel.getBankAccounts().observe(this,
                (bankAccountViews) -> {
            if (bankAccountViews != null) {
                bankAccounts.clear();
                bankAccounts.addAll(bankAccountViews);
                adapter.notifyDataSetChanged();
            }
            if (mBaRefreshLayout.isRefreshing()) {
                mBaRefreshLayout.setRefreshing(false);
            }
        });

        Button transferButt = view.findViewById(R.id.initiate_payment);
        transferButt.setOnClickListener((v) -> {
            navController.navigate(R.id.action_mainFragment_to_transferFragment);
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
        mClickButton2.setOnClickListener(v -> loginViewModel.logout());
        mBaRefreshLayout.setOnRefreshListener(() -> overviewViewModel.refreshBankAccounts(() ->
                mBaRefreshLayout.setRefreshing(false)));
    }

    private void showErrorString(@StringRes Integer error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
}
