package com.flomio.weathertest.exception;

/**
 * Created by darien
 * on 5/19/16.
 * <p>
 * Represent a network exception
 */
public class NetworkException extends WeatherException {

    public NetworkException() {
    }

    public NetworkException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetworkException(Throwable throwable) {
        super(throwable);
    }
}
