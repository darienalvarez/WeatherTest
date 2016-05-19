package com.flomio.weathertest;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.flomio.weathertest.async.LoadLocationTask;
import com.flomio.weathertest.async.TaskCallback;
import com.flomio.weathertest.async.dto.Location;
import com.flomio.weathertest.controller.UserInfoController;
import com.flomio.weathertest.util.ConnectivityHelper;
import com.flomio.weathertest.util.DialogHelper;
import com.flomio.weathertest.validation.StringSizeValidator;
import com.flomio.weathertest.validation.ValidatorHelper;
import com.flomio.weathertest.view.TaskFragment;

public class UserInfoActivity extends AppCompatActivity implements TaskCallback<Location> {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TextInputEditText mNameEditText;
    private TextInputEditText mZipCodeEditText;
    private ProgressBar mProgressBar;

    private FragmentManager mFragmentManager;
    private TaskFragment mTaskFragment;

    private UserInfoController mController;
    private ValidatorHelper mValidatorHelper;
    private boolean mRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        this.mController = new UserInfoController(UserInfoActivity.this);

        String[] values = mController.validatePreferences();
        // if values is not null then you should show the Weather Info Activity instance of User Info Activity
        if (values != null) {
            mController.navigate(values[0], values[1], new Location(values[2], values[3], values[4]));
            finish();
        } else {
            // find if any async task is mRunning
            this.mFragmentManager = getSupportFragmentManager();
            this.mTaskFragment = (TaskFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

            this.mNameEditText = (TextInputEditText) findViewById(R.id.nameEditText);
            this.mZipCodeEditText = (TextInputEditText) findViewById(R.id.zipCodeEditText);

            this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

            mValidatorHelper = new ValidatorHelper()
                    .addValidation(mNameEditText, new StringSizeValidator(3), "Your name size must be 3 letters or more")
                    .addValidation(mZipCodeEditText, new StringSizeValidator(3), "Your zip code size must be 3 numbers or more");

            Button mSavePreferencesButton = (Button) findViewById(R.id.savePreferencesButton);
            mSavePreferencesButton.setOnClickListener(saveOnClick);

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
            if (mValidatorHelper.isValid()) {
                String zipCode = mZipCodeEditText.getText().toString();
                findLocationByZipCode(zipCode);
            }
        }
    };

    private void findLocationByZipCode(String zipCode) {
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (!mRunning) {
            this.mRunning = true;
            mTaskFragment = new TaskFragment<String, Void, Location>()
                    .addConfiguration(new LoadLocationTask(), new String[]{zipCode}, UserInfoActivity.this);
            mFragmentManager.beginTransaction().replace(R.id.container, mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    @Override
    public void onSuccess(final Location location) {
        this.mRunning = false;
        final String name = mNameEditText.getText().toString();
        final String zipCode = mZipCodeEditText.getText().toString();

        if (location != null) {
            // save the user data
            mController.saveUserPreferences(name, zipCode, location);

            String info = "Your are located at: " + location.city + ", " + location.state + ". " + location.country;
            DialogHelper.showInfoDialog(UserInfoActivity.this, info, new DialogHelper.ActionCallback() {
                @Override
                public void execute() {
                    UserInfoActivity.this.finish();

                    // navigate to the weather info activity
                    mController.navigate(name, zipCode, location);
                }
            });
        }
    }

    @Override
    public void onCancelled() {
        this.mRunning = false;
    }

    @Override
    public void onError(Throwable e) {
        this.mRunning = false;
        DialogHelper.showErrorDialog(UserInfoActivity.this, R.string.error_get_location);
    }

    @Override
    public ProgressBar findProgressBar() {
        return mProgressBar;
    }
}
