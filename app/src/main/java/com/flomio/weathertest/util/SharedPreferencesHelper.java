package com.flomio.weathertest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Darien
 * on 5/18/16.
 * <p>
 * Shared Preferences Manager
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper INSTANCE = null;

    private final SharedPreferences sharedPreferences;

    public static final String PREFERENCE_NAME = "name_preference";
    public static final String PREFERENCE_ZIP_CODE = "zip_code_preference";
    public static final String PREFERENCE_COUNTRY = "country_preference";
    public static final String PREFERENCE_STATE = "state_preference";
    public static final String PREFERENCE_CITY = "city_preference";

    private SharedPreferencesHelper(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Build a single instance for SharedPreferenceManager
     *
     * @param context Application Context
     * @return SharedPreferenceManager instance
     */
    public static SharedPreferencesHelper getInstance(Context context) {
        if (SharedPreferencesHelper.INSTANCE == null) {
            SharedPreferencesHelper.INSTANCE = new SharedPreferencesHelper(context);
        }

        return INSTANCE;
    }

    /**
     * Gets and SharedPreferences.Editor for write values
     * Remember use apply() method before
     *
     * @return Editor instance
     */
    public SharedPreferences.Editor getWriter() {
        return sharedPreferences.edit();
    }

    /**
     * Gets a SharedPreference instance for read values
     *
     * @return SharedPreference instance
     */
    public SharedPreferences getReader() {
        return sharedPreferences;
    }

}
