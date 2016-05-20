package com.flomio.test.networking.dto;

/**
 * Created by darien
 * on 5/19/16.
 *
 * Represent a forecast
 */
public class Forecast {

    public String day;
    public String forecast;

    public Forecast(String day, String forecast) {
        this.day = day;
        this.forecast = forecast;
    }
}
