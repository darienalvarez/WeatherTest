package com.flomio.test.networking.dto;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent a location
 */
public class Location {

    public String country;
    public String state;
    public String city;

    public Location(String country, String state, String city) {
        this.country = country;
        this.state = state;
        this.city = city;
    }
}
