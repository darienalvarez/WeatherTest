package com.flomio.test.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flomio.test.BundleConstant;
import com.flomio.test.R;
import com.flomio.test.WeatherInfoActivity;
import com.flomio.test.async.AbstractTask;
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
import com.flomio.test.view.adapter.ForecastAdapter;

import java.util.ArrayList;

/**
 * Created by darien
 * on 5/23/16.
 */
public class WeatherInfoFragment extends Fragment implements TaskCallback<Weather> {

    private TextView mLocationTextView;
    private TextInputEditText mZipCodeEditText;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private WeatherInfoController mController;

    private Weather mWeather;
    private String mName;
    private String mZipCode;

    private boolean mRunning;
    private AbstractTask mTask;
    private ValidatorHelper mValidatorHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_info, container, false);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mLocationTextView = (TextView) getActivity().findViewById(R.id.locationTextView);
        this.mZipCodeEditText = (TextInputEditText) getActivity().findViewById(R.id.zipCodeEditText);
        this.mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.weatherRecyclerView);
        this.mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        TextView weatherInfoTextView = (TextView) getActivity().findViewById(R.id.weatherInfoTextView);
        Button updateBtn = (Button) getActivity().findViewById(R.id.updateBtn);

        this.mController = new WeatherInfoController((WeatherInfoActivity) getActivity());

        if (savedInstanceState == null) {
            // default initialization
            mName = getActivity().getIntent().getStringExtra(BundleConstant.NAME);
            mZipCode = getActivity().getIntent().getStringExtra(BundleConstant.ZIP_CODE);

            Location location = getActivity().getIntent().getParcelableExtra(BundleConstant.LOCATION);
            updateHeaderViews(location);
            mWeather = new Weather(location, new ArrayList<Forecast>());

            findForecastByZipCode(mZipCode);
        } else {
            // Saved state
            mName = savedInstanceState.getString(BundleConstant.NAME);
            mZipCode = savedInstanceState.getString(BundleConstant.ZIP_CODE);
            mWeather = savedInstanceState.getParcelable(BundleConstant.WEATHER);

            if (mWeather != null) {
                updateHeaderViews(mWeather.getLocation());
                updateRecyclerView(mWeather);
            }

            if (mRunning) {
                showProgress();
            }
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BundleConstant.WEATHER, mWeather);
        outState.putString(BundleConstant.NAME, mName);
        outState.putString(BundleConstant.ZIP_CODE, mZipCode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgress();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset_default_zip) {
            mController.resetDefaultInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(Weather result) {
        mRunning = false;
        hideProgress();

        mWeather = result;
        updateHeaderViews(mWeather.getLocation());
        updateRecyclerView(result);
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
            DialogHelper.showErrorDialog(getActivity(), R.string.error_get_forecast);
        }
    }

    private void findForecastByZipCode(String zipCode) {
        if (!mRunning) {
            // set task parameters
            this.mTask = new LoadForecastTask(this);
            // Execute task
            //noinspection unchecked
            this.mTask.execute(new String[]{zipCode});
            this.mRunning = true;
            showProgress();
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

    private final View.OnClickListener updateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard(getActivity());

            if (mValidatorHelper.isValid()) {
                mZipCode = mZipCodeEditText.getText().toString();
                findForecastByZipCode(mZipCode);
            }
        }
    };

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

    protected void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
