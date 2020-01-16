package nl.management.finance.app.ui.transactions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.management.finance.app.R;

import java.util.List;


public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {

    private final List<TransactionView> dataSet;

    public TransactionRecyclerViewAdapter(List<TransactionView> items) {
        dataSet = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = dataSet.get(position);
        holder.transactionDebtor.setText(dataSet.get(position).getDebtorName());
        holder.transactionAmount.setText(dataSet.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView transactionDebtor;
        public final TextView transactionAmount;
        public TransactionView mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            transactionDebtor = (TextView) view.findViewById(R.id.transaction_debtor);
            transactionAmount = (TextView) view.findViewById(R.id.transaction_amount);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + transactionAmount.getText() + "'";
        }
    }
}
