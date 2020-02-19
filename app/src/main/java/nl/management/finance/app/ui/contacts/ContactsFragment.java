package nl.management.finance.app.ui.contacts;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.R;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.MutableRecyclerViewAdapter;
import nl.management.finance.app.ui.OnClickRecyclerViewListener;
import nl.management.finance.app.ui.SwipeToDeleteCallback;

public class ContactsFragment extends Fragment implements OnClickRecyclerViewListener {

    private ContactsViewModel mViewModel;
    private List<ContactView> mContacts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ContactRecyclerViewAdapter mAdapter;
    private NavController mNavController;
    private int mColumnCount = 1;

    @Inject
    DaggerViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        // Set the adapter
        RecyclerView rView = view.findViewById(R.id.transaction_list);
        if (rView != null) {
            Context context = rView.getContext();
            mRecyclerView = rView;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new ContactRecyclerViewAdapter(requireContext(), view, mContacts,
                    new MutableRecyclerViewAdapter.OnDelete<ContactView>() {
                @Override
                public void delete(ContactView contact) {
                    mViewModel.deleteContact(contact);
                }

                @Override
                public void undoDelete(ContactView obj) {
                    mViewModel.createContact(obj);
                }
            }, this);

            mRecyclerView.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactsViewModel.class);
        mViewModel.getContacts().observe(this, contactViews -> {
            mContacts.clear();
            mContacts.addAll(contactViews);
            mAdapter.notifyDataSetChanged();
        });
        mNavController = Navigation.findNavController(requireView());
    }

    @Override
    public void onItemClick(int pos) {
        mViewModel.selectContact(mContacts.get(pos));
        mNavController.popBackStack(R.id.contactsFragment, true);
    }
}
