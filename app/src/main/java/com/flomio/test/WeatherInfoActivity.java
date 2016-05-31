package com.flomio.test;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.flomio.test.view.WeatherInfoFragment;


public class WeatherInfoActivity extends BaseActivity {

    private static final String TAG_TASK_FRAGMENT = "forecast_async_task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        WeatherInfoFragment fragment = (WeatherInfoFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

        if (fragment == null) {
            fragment = new WeatherInfoFragment();
            fragment.setArguments(getIntent().getExtras());
            mFragmentManager.beginTransaction().add(R.id.container, fragment, TAG_TASK_FRAGMENT).commit();
        }
    }

}
