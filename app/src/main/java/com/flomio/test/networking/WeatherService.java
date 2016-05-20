package com.flomio.test.networking;

import com.flomio.test.networking.dto.Location;
import com.flomio.test.exception.NetworkException;

/**
 * Created by darien on 5/19/16.
 */
public interface WeatherService {

    Location getLocationByZipCode(String zipCode) throws NetworkException;

}
