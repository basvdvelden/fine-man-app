package nl.management.finance.app.ui.transfer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nl.management.finance.app.R;
import nl.management.finance.app.ui.overview.BankAccountView;

public class BankAccountAdapter extends ArrayAdapter<BankAccountView> {

    public BankAccountAdapter(@NonNull Context context, @NonNull List<BankAccountView> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.bank_account_balance_item, parent, false
            );
        }

        TextView baName = convertView.findViewById(R.id.bankAccountName);
        TextView baIban = convertView.findViewById(R.id.bankAccountIban);
        TextView baBalance = convertView.findViewById(R.id.bankAccountBalance);
        BankAccountView currentItem = getItem(position);

        if (currentItem != null) {
            baName.setText(currentItem.getName());
            baIban.setText(currentItem.getIban());
            baBalance.setText(currentItem.getBalance());
        }

        return convertView;
    }

}
