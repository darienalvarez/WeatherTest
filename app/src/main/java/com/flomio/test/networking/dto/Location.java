package com.flomio.test.networking.dto;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent a location
 */
public class Location {

    private String mCountry;
    private String mState;
    private String mCity;

    public Location(String country, String state, String city) {
        this.mCountry = country;
        this.mState = state;
        this.mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getState() {
        return mState;
    }

    public String getCity() {
        return mCity;
    }
}
