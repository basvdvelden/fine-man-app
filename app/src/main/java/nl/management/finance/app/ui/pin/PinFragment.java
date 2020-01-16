package nl.management.finance.app.ui.pin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.android.support.AndroidSupportInjection;
import nl.management.finance.app.R;
import nl.management.finance.app.UserViewModel;
import nl.management.finance.app.di.DaggerViewModelFactory;
import nl.management.finance.app.ui.UIResult;
import nl.management.finance.app.ui.setup.SetupViewModel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class PinFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "PinFragment";

    private List<RadioButton> unchecked;
    private List<RadioButton> checked = new LinkedList<>();
    private NavController navController;
    private UserViewModel userViewModel;
    private State state = State.UNDECIDED;

    // VERIFYING state fields
    private VerifyPinViewModel verifyViewModel = null;

    // REGISTERING state fields
    private boolean firstPinEntered;
    private RegisterPinViewModel registerViewModel = null;
    private SetupViewModel setupViewModel = null;

    @Inject
    DaggerViewModelFactory viewModelFactory;

    private enum State {
        UNDECIDED,
        REGISTERING,
        VERIFYING
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pin, null);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        navController = Navigation.findNavController(view);
        userViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel.class);
        userViewModel.hasRegisteredPin().observe(this, (hasRegisteredPin) -> {
            Log.d(TAG, "observed has registered pin");
            setStateToUndecided();
            if (hasRegisteredPin.getSuccess() != null) {
                Boolean value = hasRegisteredPin.getSuccess();
                Log.d(TAG, value.toString());
                if (value) {
                    setStateToVerifying();
                } else {
                    setStateToRegistering();
                }
            }
        });

        boolean exitOnBack = requireArguments().getBoolean("exitOnBack");
        if (exitOnBack) {
            requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            });
        }

        this.unchecked = new LinkedList<>(Arrays.asList(
                view.findViewById(R.id.pinRB1),
                view.findViewById(R.id.pinRB2),
                view.findViewById(R.id.pinRB3),
                view.findViewById(R.id.pinRB4),
                view.findViewById(R.id.pinRB5)
        ));
        setOnClickListeners();
    }

    private void setStateToUndecided() {
        this.state = State.UNDECIDED;
        this.verifyViewModel = null;
        this.registerViewModel = null;
        this.setupViewModel = null;
        TextView pinText = requireView().findViewById(R.id.pinText);
        pinText.setText(null);
    }

    private void setStateToRegistering() {
        this.state = State.REGISTERING;
        this.firstPinEntered = false;
        this.registerViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RegisterPinViewModel.class);
        this.setupViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SetupViewModel.class);
        TextView pinText = requireView().findViewById(R.id.pinText);
        pinText.setText(R.string.register_pin1);
        setObserversRegisteringState();
    }

    private void setObserversRegisteringState() {
        registerViewModel.getPinFormState().observe(this, new Observer<PinFormState>() {
            @Override
            public void onChanged(@Nullable PinFormState pinFormState) {
                if (pinFormState == null) {
                    return;
                }

                if (pinFormState.getPinError() != null) {
                    showToastMessage(pinFormState.getPinError());
                    reset();
                } else if (pinFormState.isDataValid()) {
                    setupViewModel.setPin(registerViewModel.getPin());
                    registerViewModel.initial();
                    navController.navigate(R.id.action_pinFragment_to_setupFragment);
                }
            }
        });
    }

    private void reset() {
        TextView textView = requireView().findViewById(R.id.pinText);
        textView.setText(R.string.register_pin1);
        firstPinEntered = false;
        registerViewModel.resetAllPins();
        uncheckAllRadioButtons();
    }

    private void setStateToVerifying() {
        this.state = State.VERIFYING;
        this.verifyViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(VerifyPinViewModel.class);
        TextView pinText = requireView().findViewById(R.id.pinText);
        pinText.setText(R.string.verify_pin);
        setObserversVerifyingState();
    }

    private void setObserversVerifyingState() {
        verifyViewModel.getPinVerificationResult().observe(this, new Observer<UIResult<Void>>() {
            @Override
            public void onChanged(UIResult<Void> result) {
                if (result == null) {
                    return;
                }
                if (result.getError() == null) {
                    Log.i(TAG, "pin code correct!");
                    showToastMessage(R.string.pin_verification_successful);
                    verifyViewModel.initial();
                    navController.popBackStack(R.id.pinFragment, true);
                } else {
                    showToastMessage(result.getError());
                    verifyViewModel.resetPin();
                    uncheckAllRadioButtons();
                }
            }
        });
    }

    protected void setOnClickListeners() {
        List<Button> buttons = Arrays.asList(
                requireView().findViewById(R.id.pinButt0),
                requireView().findViewById(R.id.pinButt1),
                requireView().findViewById(R.id.pinButt2),
                requireView().findViewById(R.id.pinButt3),
                requireView().findViewById(R.id.pinButt4),
                requireView().findViewById(R.id.pinButt5),
                requireView().findViewById(R.id.pinButt6),
                requireView().findViewById(R.id.pinButt7),
                requireView().findViewById(R.id.pinButt8),
                requireView().findViewById(R.id.pinButt9),
                requireView().findViewById(R.id.pinButtDel)
        );
        for (Button button: buttons) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (this.state == State.UNDECIDED) {
            return;
        }
        PinViewModel viewModel = verifyViewModel;
        if (this.state == State.REGISTERING) {
            viewModel = registerViewModel;
        }
        if (checked.size() < 5) {
            switch (v.getId()) {
                case R.id.pinButt0:
                    viewModel.numEntered('0');
                    break;
                case R.id.pinButt1:
                    viewModel.numEntered('1');
                    break;
                case R.id.pinButt2:
                    viewModel.numEntered('2');
                    break;
                case R.id.pinButt3:
                    viewModel.numEntered('3');
                    break;
                case R.id.pinButt4:
                    viewModel.numEntered('4');
                    break;
                case R.id.pinButt5:
                    viewModel.numEntered('5');
                    break;
                case R.id.pinButt6:
                    viewModel.numEntered('6');
                    break;
                case R.id.pinButt7:
                    viewModel.numEntered('7');
                    break;
                case R.id.pinButt8:
                    viewModel.numEntered('8');
                    break;
                case R.id.pinButt9:
                    viewModel.numEntered('9');
                    break;
                case R.id.pinButtDel:
                    viewModel.deleteClicked();
                    break;

            }
            if (v.getId() == R.id.pinButtDel) {
                uncheckRadioButton();
            } else {
                checkRadioButton();
            }
        }
        if (checked.size() == 5) {
            handlePinEntered();
        }
    }

    private void checkRadioButton() {
        RadioButton rb = unchecked.get(0);
        unchecked.remove(rb);
        rb.setChecked(true);
        checked.add(rb);
    }

    private void uncheckRadioButton() {
        if (checked.size() > 0) {
            RadioButton rb = checked.get(checked.size() - 1);
            rb.setChecked(false);
            checked.remove(rb);
            unchecked.add(0, rb);
        }
    }


    private void uncheckAllRadioButtons() {
        int iterations = checked.size();
        for (int i = 0; i < iterations; i ++) {
            uncheckRadioButton();
        }
    }

    private void handlePinEntered() {
        if (this.state == State.REGISTERING) {
            handlePinEnteredRegisteringState();
        } else if (this.state == State.VERIFYING) {
            handlePinEnteredVerifyingState();
        }
    }

    private void handlePinEnteredRegisteringState() {
        if (!firstPinEntered) {
            registerViewModel.nextPin();
            uncheckAllRadioButtons();
            TextView pinText = requireView().findViewById(R.id.pinText);
            pinText.setText(R.string.register_pin2);
            firstPinEntered = true;
        } else {
            registerViewModel.pinRegistrationDataFilledIn();
        }
    }

    private void handlePinEnteredVerifyingState() {
        verifyViewModel.verifyPin();
    }

    private void showToastMessage(@StringRes Integer errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
