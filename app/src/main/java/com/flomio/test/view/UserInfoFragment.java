package com.flomio.test.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.flomio.test.R;
import com.flomio.test.UserInfoActivity;
import com.flomio.test.async.LoadLocationTask;
import com.flomio.test.async.TaskCallback;
import com.flomio.test.controller.UserInfoController;
import com.flomio.test.exception.RequestExceededException;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.util.ConnectivityHelper;
import com.flomio.test.util.DialogHelper;
import com.flomio.test.validation.StringSizeValidator;
import com.flomio.test.validation.ValidatorHelper;

/**
 * Created by darien
 * on 5/23/16.
 */
public class UserInfoFragment extends Fragment implements TaskCallback<Location> {

    private TextInputEditText mNameEditText;
    private TextInputEditText mZipCodeEditText;
    private Button mSavePreferencesButton;

    private ProgressBar mProgressBar;

    private UserInfoController mController;

    private boolean mRunning;
    private ValidatorHelper mValidatorHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        this.mController = new UserInfoController((UserInfoActivity) getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNameEditText = (TextInputEditText) getActivity().findViewById(R.id.nameEditText);
        mZipCodeEditText = (TextInputEditText) getActivity().findViewById(R.id.zipCodeEditText);
        mSavePreferencesButton = (Button) getActivity().findViewById(R.id.savePreferencesButton);
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        mValidatorHelper = new ValidatorHelper()
                .addValidation(mNameEditText, new StringSizeValidator(3), "Your name size must be 3 letters or more")
                .addValidation(mZipCodeEditText, new StringSizeValidator(3), "Your zip code size must be 3 numbers or more");

        if (mSavePreferencesButton != null) {
            mSavePreferencesButton.setOnClickListener(saveOnClick);
        }

        // check internet connection
        checkConnection();

        if (mRunning) {
            // create an showing dialog if is running (first time)
            showProgress();
        }
    }

    @Override
    public void onSuccess(final Location location) {
        mRunning = false;
        hideProgress();

        final String name = mNameEditText.getText().toString();
        final String zipCode = mZipCodeEditText.getText().toString();

        // save the user data
        mController.saveUserPreferences(name, zipCode, location);

        String info = getString(R.string.message_locate, location.getCity(), location.getState());
        DialogHelper.showInfoDialog(getActivity(), info, new DialogHelper.ActionCallback() {
            @Override
            public void execute() {
                getActivity().finish();

                // navigate to the weather info activity
                mController.navigate(name, zipCode, location);
            }
        });
    }

    @Override
    public void onCancelled() {
        mRunning = false;
        hideProgress();
    }

    @Override
    public void onError(Throwable e) {
        mRunning = false;
        hideProgress();

        if (e instanceof RequestExceededException) {
            DialogHelper.showErrorDialog(getActivity(), R.string.error_get_request_exceeded);
        } else {
            DialogHelper.showErrorDialog(getActivity(), R.string.error_get_location);
        }
    }

    /**
     * Check if any connection to internet is available
     */
    private void checkConnection() {
        if (!ConnectivityHelper.isConnected(getActivity())) {
            DialogHelper.showNoConnectionDialog(getActivity());
        }
    }

    protected void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private final View.OnClickListener saveOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard(getActivity());
            if (mValidatorHelper.isValid()) {
                String zipCode = mZipCodeEditText.getText().toString();
                findLocationByZipCode(zipCode);
            }
        }
    };

    private void findLocationByZipCode(String zipCode) {
        // set task parameters
        LoadLocationTask task = new LoadLocationTask(this);

        // Execute task
        //noinspection unchecked
        task.execute(zipCode);
        this.mRunning = true;

        showProgress();

    }

    protected void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
