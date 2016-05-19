package com.flomio.weathertest.networking;

import com.flomio.weathertest.async.dto.Location;
import com.flomio.weathertest.exception.NetworkException;

/**
 * Created by darien on 5/19/16.
 */
public interface WeatherService {

    Location getLocationByZipCode(String zipCode) throws NetworkException;

}
