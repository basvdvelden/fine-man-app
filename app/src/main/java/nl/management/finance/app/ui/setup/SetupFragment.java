package nl.management.finance.app.ui.setup;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.BuildConfig;
import nl.management.finance.app.R;
import nl.management.finance.app.di.DaggerViewModelFactory;

public class SetupFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "SetupFragment";
    private Button button;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private SetupViewModel setupViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        setupViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SetupViewModel.class);
        navController = Navigation.findNavController(view);
        setupViewModel.getRegisterResult().observe(this, (registerResult) -> {
            if (registerResult.getError() == null) {
                navController.navigate(R.id.action_setupFragment_to_mainFragment);
            } else {
                // TODO: show error popup
            }
        });

        Uri uri = requireActivity().getIntent().getData();
        if (uri != null) {
            String state = uri.getQueryParameter("state");
            String consentCode = uri.getQueryParameter("code");
            setupViewModel.restoreState(state);
            setupViewModel.setConsentCode(consentCode);
        }

        button = requireView().findViewById(R.id.give_consent);
        Spinner spinner = requireView().findViewById(R.id.banks_spinner);

        if (setupViewModel.hasConsented()) {
            button.setText(R.string.setup_account);
            spinner.setEnabled(false);
            button.setOnClickListener((v) -> setupViewModel.register());
        } else {
            button.setEnabled(false);
            button.setOnClickListener((v) -> {
                String intentUrl = "";
                switch (setupViewModel.getBank()) {
                    case "Rabobank":
                        String state = setupViewModel.getPin() + ";" + setupViewModel.getBank();
                        intentUrl = String.format(
                                "%s/oauth2/authorize?client_id=%s&response_type=code&scope=ais.balances.read ais.transactions.read-90days&state=%s",
                                BuildConfig.RABO_API_URL, BuildConfig.RABO_CLIENT_ID, state);
                        Log.d(TAG, intentUrl);
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentUrl));
                startActivity(intent);
            });
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                requireContext(), android.R.layout.simple_spinner_item,  getResources().getStringArray(R.array.banks)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be used for hint
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos > 0) {
            setupViewModel.setBank((String) parent.getItemAtPosition(pos));
            button.setEnabled(true);
            Log.d(TAG, String.format("item %s was selected", setupViewModel.getBank()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        button.setEnabled(false);
    }
}
