package com.flomio.test.networking;

import com.flomio.test.exception.NetworkException;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.networking.dto.Weather;

/**
 * Created by Darien
 * on 5/19/16.
 *
 * Define weather service methods to use
 */
public interface WeatherService {

    Location getLocationByZipCode(String zipCode) throws NetworkException;

    Weather getForecastByZipCode(String zipCode) throws NetworkException;

}
