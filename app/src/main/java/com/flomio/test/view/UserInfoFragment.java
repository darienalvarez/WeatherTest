package com.flomio.test.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

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

    private ProgressDialog mProgressDialog;

    private UserInfoController mController;

    private boolean mRunning;
    private ValidatorHelper mValidatorHelper;


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

        this.mNameEditText = (TextInputEditText) getActivity().findViewById(R.id.nameEditText);
        this.mZipCodeEditText = (TextInputEditText) getActivity().findViewById(R.id.zipCodeEditText);

        mValidatorHelper = new ValidatorHelper()
                .addValidation(mNameEditText, new StringSizeValidator(3), "Your name size must be 3 letters or more")
                .addValidation(mZipCodeEditText, new StringSizeValidator(3), "Your zip code size must be 3 numbers or more");

        Button mSavePreferencesButton = (Button) getActivity().findViewById(R.id.savePreferencesButton);
        if (mSavePreferencesButton != null) {
            mSavePreferencesButton.setOnClickListener(saveOnClick);
        }

        // check internet connection
        checkConnection();

        if (mProgressDialog == null && mRunning) {
            // create an showing dialog if is running (first time)
            this.mProgressDialog = DialogHelper.buildProgressDialog(getActivity());
            this.mProgressDialog.show();
        }
    }

    @Override
    public void onSuccess(final Location location) {
        hideProgressDialog();

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
        hideProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        hideProgressDialog();

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

    protected void hideProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
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
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.

        // set task parameters
        LoadLocationTask task = new LoadLocationTask(this);

        // Execute task
        //noinspection unchecked
        task.execute(zipCode);
        this.mRunning = true;

    }
}
