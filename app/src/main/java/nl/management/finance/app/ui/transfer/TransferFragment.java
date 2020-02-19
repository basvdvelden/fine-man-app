package nl.management.finance.app.ui.transfer;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.text.TextUtilsCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.R;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.contacts.ContactsViewModel;
import nl.management.finance.app.ui.overview.OverviewViewModel;
import nl.management.finance.app.ui.overview.BankAccountView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferFragment extends Fragment {
    private static final String TAG = "TransferFragment";
    private TransferViewModel viewModel;
    private ContactsViewModel mContactViewModel;
    private OverviewViewModel overviewViewModel;
    private BankAccountAdapter mAdapter;
    private Spinner mBankAccountSpinner;
    private TextInputLayout mAmountInput;
    private TextInputLayout mReceiversNameInput;
    private TextInputLayout mReceiversIbanInput;
    private TextInputLayout mDescriptionInput;
    private TextInputLayout mPaymentRefInput;
    private TextInputLayout mPaymentDateInput;
    private String mSelectedCurrency;
    private Spinner mCurrencySpinner;
    private CurrencySpinnerAdapter mCurrencyAdapter;
    private Button mTransferButton;
    private Button mContactsButton;

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

        mAmountInput = view.findViewById(R.id.transfer_amount_input);
        mReceiversNameInput = view.findViewById(R.id.transfer_receiver_name_input);
        mReceiversIbanInput = view.findViewById(R.id.transfer_receiver_iban_input);
        mDescriptionInput = view.findViewById(R.id.transfer_description_input);
        mPaymentRefInput = view.findViewById(R.id.transfer_payment_ref_input);
        mPaymentDateInput = view.findViewById(R.id.transfer_exec_date_input);
        mTransferButton = view.findViewById(R.id.transfer_button);
        mContactsButton = view.findViewById(R.id.contacts_button);
        mCurrencySpinner = view.findViewById(R.id.currency_spinner);
        mCurrencyAdapter = new CurrencySpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.currencies));
        mCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCurrencySpinner.setAdapter(mCurrencyAdapter);
        mCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] selectedSplit = ((String)adapterView.getSelectedItem()).split(" ");
                String currency = selectedSplit[selectedSplit.length - 1];
                try {
                    Currency.getInstance(currency);
                    mSelectedCurrency = currency;
                } catch (IllegalArgumentException ignored) {
                    // If we get here the selected currency is not available.
                    mTransferButton.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TransferViewModel.class);
        mContactViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ContactsViewModel.class);
        overviewViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OverviewViewModel.class);
        overviewViewModel.getBankAccounts().observe(this, bankAccountViews -> {
            mBankAccounts.addAll(bankAccountViews);
            mAdapter.notifyDataSetChanged();
        });

        mAdapter = new BankAccountAdapter(requireActivity(), this.mBankAccounts);
        mBankAccountSpinner = view.findViewById(R.id.transfer_ba_spinner);
        mBankAccountSpinner.setAdapter(mAdapter);
        mContactViewModel.getSelectedContact().observe(this, contact -> {
            mReceiversIbanInput.getEditText().setText(contact.getIban());
            mReceiversNameInput.getEditText().setText(contact.getName());
        });

        SepaCreditTransferView transfer = viewModel.getTransfer();
        if (transfer != null) {
            mBankAccountSpinner.setSelection(mBankAccounts.indexOf(transfer.getBankAccount()));

            String[] currencies = getResources().getStringArray(R.array.currencies);
            for (int position = 0; position < currencies.length; position++) {
                if (currencies[position].contains(transfer.getCurrency())) {
                    mCurrencySpinner.setSelection(position);
                }
            }

            EditText amountEdit = mAmountInput.getEditText();
            amountEdit.setText(String.format(Locale.getDefault(), "%s", transfer.getAmount().toString()));
            EditText receiversNameEdit = mReceiversNameInput.getEditText();
            receiversNameEdit.setText(transfer.getReceiversName());
            EditText receiversIbanEdit = mReceiversIbanInput.getEditText();
            receiversIbanEdit.setText(transfer.getReceiversIban());
            EditText descriptionEdit = mDescriptionInput.getEditText();
            descriptionEdit.setText(transfer.getDescription());
            EditText paymentRefEdit = mPaymentDateInput.getEditText();
            paymentRefEdit.setText(transfer.getPaymentRef());
        }
        setTextWatcher();

        Calendar today = Calendar.getInstance();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        mPaymentDateInput.getEditText().setText(sdf.format(today.getTime()));
        mPaymentDateInput.getEditText().setOnClickListener(textView -> {
            DatePickerDialog datePickerDialog = getDatePickerDialog((TextView)textView, sdf);
            datePickerDialog.show();
        });

        // Always convert iban to upper case
        setIbanInputFilter();

        viewModel.getTransferFormState().observe(this, transferFormState -> {
            if (transferFormState != null) {
                mTransferButton.setEnabled(transferFormState.isDataValid());
                if (transferFormState.getAmountError() != null) {
                    mAmountInput.setError(getString(transferFormState.getAmountError()));
                } else {
                    mAmountInput.setError(null);
                }
                if (transferFormState.getNameError() != null) {
                    mReceiversNameInput.setError(getString(transferFormState.getNameError()));
                } else {
                    mReceiversNameInput.setError(null);
                }
                if (transferFormState.getIbanError() != null) {
                    mReceiversIbanInput.setError(getString(transferFormState.getIbanError()));
                } else {
                    mReceiversIbanInput.setError(null);
                }
                if (transferFormState.getDescriptionError() != null) {
                    mDescriptionInput.setError(getString(transferFormState.getDescriptionError()));
                } else {
                    mDescriptionInput.setError(null);
                }
                if (transferFormState.getPaymentRefError() != null) {
                    mPaymentRefInput.setError(getString(transferFormState.getPaymentRefError()));
                } else {
                    mPaymentRefInput.setError(null);
                }
            }
        });

        mTransferButton.setOnClickListener(view12 -> {
            viewModel.initiatePayment();
            mContactViewModel.createContactIfNotExists(mReceiversIbanInput.getEditText().getText().toString(),
                    mReceiversNameInput.getEditText().getText().toString());
        });

        mContactsButton.setOnClickListener(view13 -> Navigation.findNavController(view13)
                .navigate(R.id.action_transferFragment_to_contactsFragment));

        observePaymentResult();
    }

    private void setTextWatcher() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mBankAccounts.size() > 0) {
                    int pos = mBankAccountSpinner.getSelectedItemPosition() == -1 ? 0
                            : mBankAccountSpinner.getSelectedItemPosition();
                    Double amount = mAmountInput.getEditText().getText().toString().isEmpty() ? 0.0
                            : Double.valueOf(mAmountInput.getEditText().getText().toString());
                    viewModel.transferDataChanged(mBankAccounts.get(pos), amount,
                            mReceiversNameInput.getEditText().getText().toString(),
                            mReceiversIbanInput.getEditText().getText().toString(),
                            mDescriptionInput.getEditText().getText().toString(),
                            mPaymentRefInput.getEditText().getText().toString(),
                            mSelectedCurrency, mPaymentDateInput.getEditText().getText().toString());
                }
            }
        };
        mAmountInput.getEditText().addTextChangedListener(tw);
        mReceiversNameInput.getEditText().addTextChangedListener(tw);
        mReceiversIbanInput.getEditText().addTextChangedListener(tw);
        mDescriptionInput.getEditText().addTextChangedListener(tw);
        mPaymentRefInput.getEditText().addTextChangedListener(tw);
        mPaymentDateInput.getEditText().addTextChangedListener(tw);
    }

    private DatePickerDialog getDatePickerDialog(TextView view, SimpleDateFormat sdf) {
        Calendar today = Calendar.getInstance();
        return new DatePickerDialog(requireActivity(), (datePicker, year, monthOfYear, dayOfMonth) -> {

            Calendar date = Calendar.getInstance();
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            view.setText(sdf.format(date.getTime()));

        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }

    private void setIbanInputFilter() {
        InputFilter[] editFilters = mReceiversIbanInput.getEditText().getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        mReceiversIbanInput.getEditText().setFilters(newFilters);
    }

    private void observePaymentResult() {
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
