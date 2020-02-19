package nl.management.finance.app.ui.transactions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import nl.authentication.management.app.api.NetworkNotifier;
import nl.management.finance.app.R;
import nl.management.finance.app.di.DaggerViewModelFactory;

public class TransactionFragment extends Fragment {
    private static final String TAG = "TransactionFragment";
    private TransactionViewModel viewModel;
    private TransactionRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<TransactionView> transactions = new ArrayList<>();
    private SwipeRefreshLayout mTransactionRefreshLayout;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    @Inject
    NetworkNotifier networkNotifier;
    private Disposable networkDisposable;

    // TODO: Customize parameters
    private int mColumnCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        // Set the adapter
        RecyclerView rView = view.findViewById(R.id.transaction_list);
        if (rView != null) {
            Context context = rView.getContext();
            recyclerView = rView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new TransactionRecyclerViewAdapter(transactions);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        mTransactionRefreshLayout = view.findViewById(R.id.transaction_swipe_refresh);
        networkDisposable = networkNotifier.getNetworkStatus().observeOn(AndroidSchedulers.mainThread()).subscribe(event -> {
            Log.d(TAG, "showing snackbar");
            Snackbar snackbar = Snackbar.make(view, "Network is currently not available", Snackbar.LENGTH_LONG);
            snackbar.show();
            networkDisposable.dispose();
        });
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TransactionViewModel.class);

        viewModel.getTransactions().observe(this, transactionViews -> {
            transactions.clear();
            transactions.addAll(transactionViews);
            adapter.notifyDataSetChanged();
        });
        viewModel.refreshTransactions(() -> {});
        mTransactionRefreshLayout.setOnRefreshListener(() -> viewModel.refreshTransactions(() -> {
            if (mTransactionRefreshLayout.isRefreshing()) {
                mTransactionRefreshLayout.setRefreshing(false);
            }
        }));
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
