package nl.management.finance.app.ui.transfer;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.R;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.UIResult;
import nl.management.finance.app.ui.overview.OverviewViewModel;
import nl.management.finance.app.ui.overview.BankAccountView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferFragment extends Fragment {
    private static final String TAG = "TransferFragment";
    private TransferViewModel viewModel;
    private OverviewViewModel overviewViewModel;
    private BankAccountAdapter mAdapter;

    private TextInputLayout mInputAmount;
    private TextInputLayout mInputReceiversName;
    private TextInputLayout mInputReceiversIban;
    private TextInputLayout mInputDescription;
    private TextInputLayout mInputPaymentRef;
    private Button mTransferButton;

    @Inject
    DaggerViewModelFactory viewModelFactory;

    private List<BankAccountView> mBankAccounts = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onViewCreated(view, savedInstanceState);

        mInputAmount = view.findViewById(R.id.transfer_amount_input);
        mInputReceiversName = view.findViewById(R.id.transfer_receiver_name_input);
        mInputReceiversIban = view.findViewById(R.id.transfer_receiver_iban_input);
        mInputDescription = view.findViewById(R.id.transfer_description_input);
        mInputPaymentRef = view.findViewById(R.id.transfer_payment_ref_input);

        mTransferButton = view.findViewById(R.id.transfer_button);

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TransferViewModel.class);
        overviewViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OverviewViewModel.class);
        overviewViewModel.getBankAccounts().observe(this, bankAccountViews -> {
            mBankAccounts.addAll(bankAccountViews);
            mAdapter.notifyDataSetChanged();
        });

        mAdapter = new BankAccountAdapter(requireActivity(), this.mBankAccounts);
        Spinner baSpinner = view.findViewById(R.id.transfer_ba_spinner);
        baSpinner.setAdapter(mAdapter);

        TransferView transfer = viewModel.getTransfer();
        if (transfer != null) {
            baSpinner.setSelection(mBankAccounts.indexOf(transfer.getTransferBankAccount()));

            EditText amountEdit = mInputAmount.getEditText();
            amountEdit.setText(transfer.getTransferAmount().toString());
            EditText receiversNameEdit = mInputReceiversName.getEditText();
            receiversNameEdit.setText(transfer.getTransferReceiversName());
            EditText receiversIbanEdit = mInputReceiversIban.getEditText();
            receiversIbanEdit.setText(transfer.getTransferReceiversIban());
            EditText descriptionEdit = mInputDescription.getEditText();
            descriptionEdit.setText(transfer.getTransferDescription());
            EditText paymentRefEdit = mInputPaymentRef.getEditText();
            paymentRefEdit.setText(transfer.getTransferPaymentRef());
        }
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int pos = baSpinner.getSelectedItemPosition() == -1 ? 0 : baSpinner.getSelectedItemPosition();
                Double amount = mInputAmount.getEditText().getText().toString().isEmpty() ? 0.0
                        : Double.valueOf(mInputAmount.getEditText().getText().toString());
                viewModel.transferDataChanged(mBankAccounts.get(pos), amount,
                        mInputReceiversName.getEditText().getText().toString(),
                        mInputReceiversIban.getEditText().getText().toString(),
                        mInputDescription.getEditText().getText().toString(),
                        mInputPaymentRef.getEditText().getText().toString());
            }
        };
        mInputAmount.getEditText().addTextChangedListener(tw);
        mInputReceiversName.getEditText().addTextChangedListener(tw);
        mInputReceiversIban.getEditText().addTextChangedListener(tw);
        mInputDescription.getEditText().addTextChangedListener(tw);
        mInputPaymentRef.getEditText().addTextChangedListener(tw);

        // Always convert iban to upper case
        InputFilter[] editFilters = mInputReceiversIban.getEditText().getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        mInputReceiversIban.getEditText().setFilters(newFilters);


        viewModel.getTransferFormState().observe(this, transferFormState -> {
            if (transferFormState != null) {
                mTransferButton.setEnabled(transferFormState.isDataValid());
                if (transferFormState.getAmountError() != null) {
                    mInputAmount.setError(getString(transferFormState.getAmountError()));
                } else {
                    mInputAmount.setError(null);
                }
                if (transferFormState.getNameError() != null) {
                    mInputReceiversName.setError(getString(transferFormState.getNameError()));
                } else {
                    mInputReceiversName.setError(null);
                }
                if (transferFormState.getIbanError() != null) {
                    mInputReceiversIban.setError(getString(transferFormState.getIbanError()));
                } else {
                    mInputReceiversIban.setError(null);
                }
                if (transferFormState.getDescriptionError() != null) {
                    mInputDescription.setError(getString(transferFormState.getDescriptionError()));
                } else {
                    mInputDescription.setError(null);
                }
                if (transferFormState.getPaymentRefError() != null) {
                    mInputPaymentRef.setError(getString(transferFormState.getPaymentRefError()));
                } else {
                    mInputPaymentRef.setError(null);
                }
            }
        });

        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.initiatePayment();
            }
        });

        viewModel.getInitPaymentResult().observe(this, result -> {
            if (result.getError() == null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getSuccess()));
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), getString(result.getError()), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.getInitPaymentResult().removeObservers(this);
    }

}
