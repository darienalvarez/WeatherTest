package com.flomio.test.networking.dto;

/**
 * Created by darien
 * on 5/19/16.
 *
 * Represent a forecast
 */
public class Forecast {

    private String mDay;
    private String mForecast;

    public Forecast(String day, String forecast) {
        this.mDay = day;
        this.mForecast = forecast;
    }

    public String getDay() {
        return mDay;
    }

    public String getForecast() {
        return mForecast;
    }
}
