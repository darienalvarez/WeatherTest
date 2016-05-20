package com.flomio.test.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.flomio.test.BundleConstant;
import com.flomio.test.UserInfoActivity;
import com.flomio.test.WeatherInfoActivity;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.util.SharedPreferencesHelper;

/**
 * Created by Darien
 * on 5/18/16.
 *
 * User Info Activity controller
 */
public class UserInfoController {

    private final UserInfoActivity context;

    public UserInfoController(UserInfoActivity context) {
        this.context = context;
    }

    /**
     * Validate if the name and the zipCode are stored or not
     *
     * @return String array with [name, zipCode] otherwise return null
     */
    public String[] validatePreferences() {
        SharedPreferences sharedPreferences = SharedPreferencesHelper
                .getInstance(context.getApplicationContext()).getReader();

        String name = sharedPreferences.getString(SharedPreferencesHelper.PREFERENCE_NAME, null);
        String zipCode = sharedPreferences.getString(SharedPreferencesHelper.PREFERENCE_ZIP_CODE, null);

        String state = sharedPreferences.getString(SharedPreferencesHelper.PREFERENCE_STATE, null);
        String city = sharedPreferences.getString(SharedPreferencesHelper.PREFERENCE_CITY, null);

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(zipCode)
                && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(city)) {
            return new String[]{name, zipCode, state, city};
        }
        return null;
    }


    /**
     * Save the name of the user and the zip code provided
     * it will be used as default values in weather queries
     *
     * @param name     Name of the user
     * @param zipCode  Zip Code of the user
     * @param location the location of the zip code include country, state, city
     */
    public void saveUserPreferences(String name, String zipCode, Location location) {

        SharedPreferences.Editor editor = SharedPreferencesHelper
                .getInstance(context.getApplicationContext()).getWriter();

        editor.putString(SharedPreferencesHelper.PREFERENCE_NAME, name);
        editor.putString(SharedPreferencesHelper.PREFERENCE_ZIP_CODE, zipCode);

        editor.putString(SharedPreferencesHelper.PREFERENCE_STATE, location.getState());
        editor.putString(SharedPreferencesHelper.PREFERENCE_CITY, location.getCity());

        editor.apply();
    }

    /**
     * Receive the default name and zip code and navigate to Weather Info Activity
     *
     * @param name     Default user name
     * @param zipCode  Default user zip code
     * @param location the location of the zip code include country, state, city
     */
    public void navigate(String name, String zipCode, Location location) {
        Intent intent = new Intent(context, WeatherInfoActivity.class);
        intent.putExtra(BundleConstant.NAME, name);
        intent.putExtra(BundleConstant.ZIP_CODE, zipCode);

        intent.putExtra(BundleConstant.LOCATION, location);

        context.startActivity(intent);
    }

}
