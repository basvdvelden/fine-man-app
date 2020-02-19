package nl.management.finance.app.ui.transfer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import nl.management.finance.app.R;

public class CurrencySpinnerAdapter extends ArrayAdapter<String> {

    public CurrencySpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] currencies) {
        super(context, resource, currencies);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TextView view = (TextView)super.getView(position, convertView, viewGroup);
        if (getItem(position).contains(getContext().getString(R.string.currency_not_available))) {
            view.setText(getContext().getString(R.string.currency_not_available));
        } else {
            view.setText(getItem(position).split(" ")[0]);
        }
        return view;
    }
}
