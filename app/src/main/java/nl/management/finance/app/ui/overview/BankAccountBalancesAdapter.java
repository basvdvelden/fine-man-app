package nl.management.finance.app.ui.overview;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import nl.management.finance.app.R;

class BankAccountBalancesAdapter extends RecyclerView.Adapter<BankAccountBalancesAdapter.BankAccountBalanceViewHolder> {
    private static final String TAG = "BalancesAdapter";
    private List<BankAccountView> dataSet;
    private PublishSubject<BankAccountView> onClickSubject = PublishSubject.create();

    public static class BankAccountBalanceViewHolder extends RecyclerView.ViewHolder {
        public TextView bankAccountName;
        public TextView bankAccountIban;
        public TextView bankAccountBalance;
        public ConstraintLayout bankAccountBalanceItem;

        public BankAccountBalanceViewHolder(ConstraintLayout bankAccountBalanceItem) {
            super(bankAccountBalanceItem);
            this.bankAccountBalanceItem = bankAccountBalanceItem;

            this.bankAccountName = bankAccountBalanceItem.findViewById(R.id.bankAccountName);
            this.bankAccountIban = bankAccountBalanceItem.findViewById(R.id.bankAccountIban);
            this.bankAccountBalance = bankAccountBalanceItem.findViewById(R.id.bankAccountBalance);
        }
    }

    public BankAccountBalancesAdapter(List<BankAccountView> bankAccounts) {
        dataSet = bankAccounts;
    }

    @NonNull
    @Override
    public BankAccountBalancesAdapter.BankAccountBalanceViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                      int viewType) {
        ConstraintLayout item = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bank_account_balance_item, parent, false);
        return new BankAccountBalanceViewHolder(item);
    }

    @Override
    public void onBindViewHolder(BankAccountBalanceViewHolder viewHolder, int position) {
        viewHolder.bankAccountName.setText(dataSet.get(position).getName());
        viewHolder.bankAccountIban.setText(dataSet.get(position).getIban());
        viewHolder.bankAccountBalance.setTypeface(Typeface.MONOSPACE);
        viewHolder.bankAccountBalance.setText(dataSet.get(position).getBalance());
        viewHolder.bankAccountBalanceItem.setOnClickListener((v) -> {
            Log.d(TAG, "item clicked");
            onClickSubject.onNext(dataSet.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public Observable<BankAccountView> getPositionClicks() {
        return onClickSubject.hide();
    }
}
