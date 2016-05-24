package com.flomio.test;

import android.os.Bundle;
import android.os.Handler;
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

        this.mLocationTextView = (TextView) findViewById(R.id.locationTextView);
        this.mZipCodeEditText = (TextInputEditText) findViewById(R.id.zipCodeEditText);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.weatherRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        TextView weatherInfoTextView = (TextView) findViewById(R.id.weatherInfoTextView);
        Button updateBtn = (Button) findViewById(R.id.updateBtn);

        this.mFragmentManager = getSupportFragmentManager();
        this.mTaskFragment = (TaskFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

        this.mController = new WeatherInfoController(WeatherInfoActivity.this);

        if (savedInstanceState != null) {
            // Saved state
            mName = savedInstanceState.getString(BundleConstant.NAME);
            mZipCode = savedInstanceState.getString(BundleConstant.ZIP_CODE);
            mWeather = savedInstanceState.getParcelable(BundleConstant.WEATHER);

            if (mWeather != null) {
                updateHeaderViews(mWeather.getLocation());
                updateRecyclerView(mWeather);
            }
        } else {
            // default initialization
            mName = getIntent().getStringExtra(BundleConstant.NAME);
            mZipCode = getIntent().getStringExtra(BundleConstant.ZIP_CODE);

            Location location = getIntent().getParcelableExtra(BundleConstant.LOCATION);
            updateHeaderViews(location);
            mWeather = new Weather(location, new ArrayList<Forecast>());

            findForecastByZipCode(mZipCode);
        }

        if (weatherInfoTextView != null) {
            weatherInfoTextView.setText(getString(R.string.weather_info, mName));
        }

        if (this.mZipCodeEditText != null) {
            this.mZipCodeEditText.setText(mZipCode);
            mValidatorHelper = new ValidatorHelper()
                    .addValidation(mZipCodeEditText, new StringSizeValidator(3), "Your zip code size must be 3 numbers or more");
        }

        if (updateBtn != null) {
            updateBtn.setOnClickListener(updateClick);
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
        mWeather = result;
        updateHeaderViews(mWeather.getLocation());
        updateRecyclerView(result);
        releaseFragment();
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
                    .addConfiguration(new LoadForecastTask(), new String[]{zipCode}, this);
            mFragmentManager.beginTransaction().replace(R.id.container, mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    private void updateHeaderViews(final Location location) {
        if (mLocationTextView != null && location != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLocationTextView.setText(
                            getString(R.string.message_locate,
                                    location.getCity(),
                                    location.getState()));
                }
            }, 100);

        }
    }

    private void updateRecyclerView(Weather data) {
        ForecastAdapter adapter = new ForecastAdapter(data.getForecastList());
        mRecyclerView.setAdapter(adapter);
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
