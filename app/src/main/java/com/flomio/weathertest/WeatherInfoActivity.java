package com.flomio.weathertest;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

public class WeatherInfoActivity extends AppCompatActivity {

    private TextInputEditText zipCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        zipCodeEditText = (TextInputEditText) findViewById(R.id.zipCodeEditText);

        String name = getIntent().getStringExtra(BundleConstant.NAME);
        String zipCode = getIntent().getStringExtra(BundleConstant.ZIP_CODE);

        String country = getIntent().getStringExtra(BundleConstant.COUNTRY);
        String state = getIntent().getStringExtra(BundleConstant.STATE);
        String city = getIntent().getStringExtra(BundleConstant.CITY);

        zipCodeEditText.setText(zipCode);
    }


}
