package com.flomio.weathertest.exception;

/**
 * Created by darien
 * on 5/19/16.
 * <p>
 * Generic exception for application
 */
public class WeatherException extends Exception {

    public WeatherException() {
    }

    public WeatherException(String detailMessage) {
        super(detailMessage);
    }

    public WeatherException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WeatherException(Throwable throwable) {
        super(throwable);
    }
}
