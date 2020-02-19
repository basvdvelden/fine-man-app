package nl.management.finance.app.ui.contacts;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import nl.management.finance.app.R;
import nl.management.finance.app.data.contact.Contact;
import nl.management.finance.app.ui.MutableRecyclerViewAdapter;
import nl.management.finance.app.ui.OnClickRecyclerViewListener;

import java.util.List;


public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>
        implements MutableRecyclerViewAdapter {

    private final Context mContext;
    private final View mView;
    private final List<ContactView> dataSet;
    private final OnDelete<ContactView> mOnDelete;
    private final OnClickRecyclerViewListener mListener;
    private ContactView mRecentlyDeleted;

    public ContactRecyclerViewAdapter(Context context, View view, List<ContactView> items,
                                      OnDelete<ContactView> onDelete, OnClickRecyclerViewListener listener) {
        mContext = context;
        mView = view;
        dataSet = items;
        mOnDelete = onDelete;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contacts_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = dataSet.get(position);
        holder.contactName.setText(dataSet.get(position).getName());
        holder.contactIban.setText(dataSet.get(position).getIban());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void deleteItem(int pos) {
        mRecentlyDeleted = dataSet.get(pos);
        dataSet.remove(pos);
        mOnDelete.delete(mRecentlyDeleted);
        notifyItemRemoved(pos);
        showUndoSnackBar(mView, String.format("Removed %s from contacts.", mRecentlyDeleted.getName()),
                v -> undoDelete());
    }

    private void undoDelete() {
        mOnDelete.undoDelete(mRecentlyDeleted);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        final TextView contactName;
        final TextView contactIban;
        final OnClickRecyclerViewListener mListener;
        ContactView mItem;


        ViewHolder(View view, OnClickRecyclerViewListener listener) {
            super(view);
            mView = view;
            contactName = view.findViewById(R.id.contact_name);
            contactIban = view.findViewById(R.id.contact_iban);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(getAdapterPosition());
        }
    }
}
