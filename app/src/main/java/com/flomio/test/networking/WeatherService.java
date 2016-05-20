package com.flomio.test.networking;

import com.flomio.test.exception.NetworkException;
import com.flomio.test.networking.dto.Forecast;
import com.flomio.test.networking.dto.Location;

import java.util.List;

/**
 * Created by darien
 * on 5/19/16.
 *
 * Define weather service methods to use
 */
public interface WeatherService {

    Location getLocationByZipCode(String zipCode) throws NetworkException;

    List<Forecast> getForecastByZipCode(String zipCode) throws NetworkException;

}
