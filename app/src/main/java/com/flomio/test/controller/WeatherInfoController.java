package com.flomio.test.controller;

import android.content.Intent;
import android.content.SharedPreferences;

import com.flomio.test.UserInfoActivity;
import com.flomio.test.WeatherInfoActivity;
import com.flomio.test.util.SharedPreferencesHelper;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Weather info activity controller
 */
public class WeatherInfoController {

    private final WeatherInfoActivity context;

    public WeatherInfoController(WeatherInfoActivity context) {
        this.context = context;
    }

    public void resetDefaultInfo() {
        // reset stored preferences
        SharedPreferences.Editor editor = SharedPreferencesHelper
                .getInstance(context.getApplicationContext()).getWriter();

        editor.putString(SharedPreferencesHelper.PREFERENCE_NAME, "");
        editor.putString(SharedPreferencesHelper.PREFERENCE_ZIP_CODE, "");
        editor.putString(SharedPreferencesHelper.PREFERENCE_STATE, "");
        editor.putString(SharedPreferencesHelper.PREFERENCE_CITY, "");

        editor.apply();

        // Start User Info activity
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
        context.finish();
    }
}
