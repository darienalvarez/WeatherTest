package com.flomio.test;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.flomio.test.async.LoadForecastTask;
import com.flomio.test.async.TaskCallback;
import com.flomio.test.controller.WeatherInfoController;
import com.flomio.test.networking.dto.Forecast;
import com.flomio.test.util.DialogHelper;
import com.flomio.test.view.TaskFragment;
import com.flomio.test.view.adapter.ForecastAdapter;

import java.util.List;

public class WeatherInfoActivity extends AppCompatActivity implements TaskCallback<List<Forecast>> {

    private static final String TAG_TASK_FRAGMENT = "forecast_async_task";

    private TextInputEditText mZipCodeEditText;
    private RecyclerView mRecyclerView;

    private ProgressDialog mProgressDialog;
    private TaskFragment mTaskFragment;

    private WeatherInfoController mController;
    private FragmentManager mFragmentManager;
    private ForecastAdapter mAdapter;
    private List<Forecast> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        String name = getIntent().getStringExtra(BundleConstant.NAME);
        String zipCode = getIntent().getStringExtra(BundleConstant.ZIP_CODE);

        String country = getIntent().getStringExtra(BundleConstant.COUNTRY);
        String state = getIntent().getStringExtra(BundleConstant.STATE);
        String city = getIntent().getStringExtra(BundleConstant.CITY);

        this.mZipCodeEditText = (TextInputEditText) findViewById(R.id.zipCodeEditText);
        if (this.mZipCodeEditText != null) {
            this.mZipCodeEditText.setText(zipCode);
        }

        this.mController = new WeatherInfoController(WeatherInfoActivity.this);

        this.mRecyclerView = (RecyclerView) findViewById(R.id.weatherRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        this.mFragmentManager = getSupportFragmentManager();
        this.mTaskFragment = (TaskFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

        mProgressDialog = DialogHelper.showProgressDialog(WeatherInfoActivity.this);
        findForecastByZipCode(zipCode);
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

    private void findForecastByZipCode(String zipCode) {
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null || !mTaskFragment.isRunning()) {
            mTaskFragment = new TaskFragment<String, Void, List<Forecast>>()
                    .addConfiguration(new LoadForecastTask(), new String[]{zipCode});
            mFragmentManager.beginTransaction().replace(R.id.container, mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    @Override
    public ProgressDialog findProgressDialog() {
        return mProgressDialog;
    }

    @Override
    public void onSuccess(List<Forecast> result) {
        if (result != null) {
            this.mAdapter = new ForecastAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
            this.mData = result;
//            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onError(Throwable e) {
        DialogHelper.showErrorDialog(WeatherInfoActivity.this, R.string.error_get_forecast);
    }
}
