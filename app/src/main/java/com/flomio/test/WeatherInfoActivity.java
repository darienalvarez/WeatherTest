package com.flomio.test;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.flomio.test.controller.WeatherInfoController;

public class WeatherInfoActivity extends AppCompatActivity {

    private TextInputEditText mZipCodeEditText;

    private WeatherInfoController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        mController = new WeatherInfoController(WeatherInfoActivity.this);

        mZipCodeEditText = (TextInputEditText) findViewById(R.id.zipCodeEditText);

        String name = getIntent().getStringExtra(BundleConstant.NAME);
        String zipCode = getIntent().getStringExtra(BundleConstant.ZIP_CODE);

        String country = getIntent().getStringExtra(BundleConstant.COUNTRY);
        String state = getIntent().getStringExtra(BundleConstant.STATE);
        String city = getIntent().getStringExtra(BundleConstant.CITY);

        mZipCodeEditText.setText(zipCode);
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

}
