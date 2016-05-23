package com.flomio.test;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.flomio.test.async.LoadLocationTask;
import com.flomio.test.async.TaskCallback;
import com.flomio.test.controller.UserInfoController;
import com.flomio.test.exception.RequestExceededException;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.util.ConnectivityHelper;
import com.flomio.test.util.DialogHelper;
import com.flomio.test.validation.StringSizeValidator;
import com.flomio.test.validation.ValidatorHelper;
import com.flomio.test.view.TaskFragment;

public class UserInfoActivity extends BaseActivity implements TaskCallback<Location> {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TextInputEditText mNameEditText;
    private TextInputEditText mZipCodeEditText;

    private UserInfoController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        this.mController = new UserInfoController(UserInfoActivity.this);

        String[] values = mController.validatePreferences();
        // if values is not null then you should show the Weather Info Activity instance of User Info Activity
        if (values != null) {
            mController.navigate(values[0], values[1], new Location(values[2], values[3]));
            finish();
        } else {
            // find if any async task is mRunning
            this.mFragmentManager = getSupportFragmentManager();
            this.mTaskFragment = (TaskFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

            this.mNameEditText = (TextInputEditText) findViewById(R.id.nameEditText);
            this.mZipCodeEditText = (TextInputEditText) findViewById(R.id.zipCodeEditText);

            mValidatorHelper = new ValidatorHelper()
                    .addValidation(mNameEditText, new StringSizeValidator(3), "Your name size must be 3 letters or more")
                    .addValidation(mZipCodeEditText, new StringSizeValidator(3), "Your zip code size must be 3 numbers or more");

            Button mSavePreferencesButton = (Button) findViewById(R.id.savePreferencesButton);
            if (mSavePreferencesButton != null) {
                mSavePreferencesButton.setOnClickListener(saveOnClick);
            }

            // check internet connection
            checkConnection();
        }
    }

    /**
     * Check if any connection to internet is available
     */
    private void checkConnection() {
        if (!ConnectivityHelper.isConnected(UserInfoActivity.this)) {
            DialogHelper.showNoConnectionDialog(UserInfoActivity.this);
        }
    }

    private final View.OnClickListener saveOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard(UserInfoActivity.this);
            if (mValidatorHelper.isValid()) {
                String zipCode = mZipCodeEditText.getText().toString();
                findLocationByZipCode(zipCode);
            }
        }
    };

    private void findLocationByZipCode(String zipCode) {
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null || !mTaskFragment.isRunning()) {
            mTaskFragment = new TaskFragment<String, Void, Location>()
                    .addConfiguration(new LoadLocationTask(), new String[]{zipCode});
            mFragmentManager.beginTransaction().replace(R.id.container, mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    @Override
    public void onSuccess(final Location location) {
        releaseFragment();

        final String name = mNameEditText.getText().toString();
        final String zipCode = mZipCodeEditText.getText().toString();

        // save the user data
        mController.saveUserPreferences(name, zipCode, location);

        String info = getString(R.string.message_locate, location.getCity(), location.getState());
        DialogHelper.showInfoDialog(UserInfoActivity.this, info, new DialogHelper.ActionCallback() {
            @Override
            public void execute() {
                UserInfoActivity.this.finish();

                // navigate to the weather info activity
                mController.navigate(name, zipCode, location);
            }
        });
    }

    @Override
    public void onCancelled() {
        releaseFragment();
    }

    @Override
    public void onError(Throwable e) {
        releaseFragment();

        if (e instanceof RequestExceededException) {
            DialogHelper.showErrorDialog(UserInfoActivity.this, R.string.error_get_request_exceeded);
        } else {
            DialogHelper.showErrorDialog(UserInfoActivity.this, R.string.error_get_location);
        }
    }

    protected void releaseFragment() {
        synchronized (this) {

            if (mTaskFragment != null) {
                mTaskFragment.stopRunning();
                mTaskFragment = null;
            }
        }
    }
}
