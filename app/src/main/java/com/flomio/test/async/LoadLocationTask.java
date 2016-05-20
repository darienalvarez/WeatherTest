package com.flomio.test.async;

import com.flomio.test.exception.WeatherException;
import com.flomio.test.networking.WeatherService;
import com.flomio.test.networking.WeatherServiceFactory;
import com.flomio.test.networking.dto.Location;

/**
 * Created by Darien
 * on 5/19/16.
 *
 * Execute a service request to get a Location
 */
public class LoadLocationTask extends AbstractTask<String, Void, Location> {

    @Override
    protected Location doInBackground(String... param) {

        try {
            WeatherService service = new WeatherServiceFactory().getService("WU");
            if (service != null) {
                return service.getLocationByZipCode(param[0]);
            }
        } catch (WeatherException e) {
            mException = e;
        }

        return null;
    }


}
