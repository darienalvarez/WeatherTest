package com.flomio.test;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flomio.test.async.LoadForecastTask;
import com.flomio.test.async.TaskCallback;
import com.flomio.test.controller.WeatherInfoController;
import com.flomio.test.exception.RequestExceededException;
import com.flomio.test.networking.dto.Forecast;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.networking.dto.Weather;
import com.flomio.test.util.DialogHelper;
import com.flomio.test.validation.StringSizeValidator;
import com.flomio.test.validation.ValidatorHelper;
import com.flomio.test.view.TaskFragment;
import com.flomio.test.view.adapter.ForecastAdapter;

import java.util.ArrayList;


public class WeatherInfoActivity extends BaseActivity implements TaskCallback<Weather> {

    private static final String TAG_TASK_FRAGMENT = "forecast_async_task";

    private TextView mLocationTextView;
    private TextInputEditText mZipCodeEditText;
    private RecyclerView mRecyclerView;

    private WeatherInfoController mController;

    private Weather mWeather;
    private String mName;
    private String mZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        if (savedInstanceState != null) {
            // Saved state
            mName = savedInstanceState.getString(BundleConstant.NAME);
            mZipCode = savedInstanceState.getString(BundleConstant.ZIP_CODE);
            mWeather = savedInstanceState.getParcelable(BundleConstant.WEATHER);
        } else {
            // default initialization
            mName = getIntent().getStringExtra(BundleConstant.NAME);
            mZipCode = getIntent().getStringExtra(BundleConstant.ZIP_CODE);

            Location location = getIntent().getParcelableExtra(BundleConstant.LOCATION);
            mWeather = new Weather(location, new ArrayList<Forecast>());
        }

        TextView weatherInfoTextView = (TextView) findViewById(R.id.weatherInfoTextView);
        if (weatherInfoTextView != null) {
            weatherInfoTextView.setText(getString(R.string.weather_info, mName));
        }

        this.mLocationTextView = (TextView) findViewById(R.id.locationTextView);

        this.mZipCodeEditText = (TextInputEditText) findViewById(R.id.zipCodeEditText);
        if (this.mZipCodeEditText != null) {
            this.mZipCodeEditText.setText(mZipCode);
        }
        mValidatorHelper = new ValidatorHelper()
                .addValidation(mZipCodeEditText, new StringSizeValidator(3), "Your zip code size must be 3 numbers or more");

        Button updateBtn = (Button) findViewById(R.id.updateBtn);
        if (updateBtn != null) {
            updateBtn.setOnClickListener(updateClick);
        }

        this.mRecyclerView = (RecyclerView) findViewById(R.id.weatherRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        this.mController = new WeatherInfoController(WeatherInfoActivity.this);

        this.mFragmentManager = getSupportFragmentManager();
        this.mTaskFragment = (TaskFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

        updateRecyclerView(mWeather);

        // run just on first creation
        if (savedInstanceState == null) {
            findForecastByZipCode(mZipCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset_default_zip) {
            mController.resetDefaultInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BundleConstant.WEATHER, mWeather);
        outState.putString(BundleConstant.NAME, mName);
        outState.putString(BundleConstant.ZIP_CODE, mZipCode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(Weather result) {
        releaseFragment();

        updateRecyclerView(result);
    }

    @Override
    public void onCancelled() {
        releaseFragment();
    }

    @Override
    public void onError(Throwable e) {
        releaseFragment();

        if (e instanceof RequestExceededException) {
            DialogHelper.showErrorDialog(WeatherInfoActivity.this, R.string.error_get_request_exceeded);
        } else {
            DialogHelper.showErrorDialog(WeatherInfoActivity.this, R.string.error_get_forecast);
        }
    }

    private final View.OnClickListener updateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard(WeatherInfoActivity.this);

            if (mValidatorHelper.isValid()) {
                mZipCode = mZipCodeEditText.getText().toString();
                findForecastByZipCode(mZipCode);
            }
        }
    };

    private void findForecastByZipCode(String zipCode) {
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null || !mTaskFragment.isRunning()) {

            mTaskFragment = new TaskFragment<String, Void, Weather>()
                    .addConfiguration(new LoadForecastTask(), new String[]{zipCode});
            mFragmentManager.beginTransaction().replace(R.id.container, mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    private void updateRecyclerView(Weather data) {
        this.mWeather = data;

        ForecastAdapter adapter = new ForecastAdapter(mWeather.getForecastList());
        this.mRecyclerView.setAdapter(adapter);

        if (mLocationTextView != null && mWeather.getLocation() != null) {
            mLocationTextView.setText(
                    getString(R.string.message_locate,
                            mWeather.getLocation().getCity(),
                            mWeather.getLocation().getState()));
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
