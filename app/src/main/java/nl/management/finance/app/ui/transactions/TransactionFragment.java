package nl.management.finance.app.ui.transactions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.R;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.transactions.dummy.DummyContent;

public class TransactionFragment extends Fragment {
    private TransactionViewModel viewModel;
    private TransactionRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<TransactionView> transactions = new ArrayList<>();

    @Inject
    DaggerViewModelFactory viewModelFactory;

    // TODO: Customize parameters
    private int mColumnCount = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
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
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TransactionViewModel.class);
        Bundle args = getArguments();
        String iban = Objects.requireNonNull(args).getString("iban");
        viewModel.getTransactions(iban).observe(this, transactionViews -> {
           this.transactions.clear();
           this.transactions.addAll(transactionViews);
           adapter.notifyDataSetChanged();
        });
    }
}
