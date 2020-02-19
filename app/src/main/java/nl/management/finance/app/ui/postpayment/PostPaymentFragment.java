package nl.management.finance.app.ui.postpayment;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.IntentFilterUrls;
import nl.management.finance.app.R;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.transfer.TransferViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostPaymentFragment extends Fragment {
    private static final String TAG = "PostPaymentFragment";

    private TransferViewModel viewModel;

    @Inject
    DaggerViewModelFactory viewModelFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_payment, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransferViewModel.class);
        Uri uri = requireActivity().getIntent().getData();
        if (uri != null && uri.toString().contains(IntentFilterUrls.POST_PAYMENT)) {
            Log.i(TAG, "filtering intent, url: " + uri.toString());
            viewModel.getPsuMessage().observe(this, psuMessage -> {
                TextView textView = view.findViewById(R.id.psu_message);
                if (psuMessage.getError() == null) {
                    textView.setText(psuMessage.getSuccess());
                } else {
                    textView.setText(psuMessage.getError());
                }
            });

        }
        Button button = view.findViewById(R.id.postPaymentButton);
        button.setOnClickListener(view1 -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_global_overviewFragment);
        });
    }

}
