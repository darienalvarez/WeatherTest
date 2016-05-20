package com.flomio.test.async;

import com.flomio.test.exception.WeatherException;
import com.flomio.test.networking.WeatherService;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.networking.wu.WeatherServiceImpl;

/**
 * Created by Darien
 * on 5/19/16.
 */
public class LoadLocationTask<P extends String, G, R extends Location> extends AbstractTask<P, G, R> {

    @Override
    protected Location doInBackground(String... param) {

        try {
            WeatherService service = new WeatherServiceImpl();
            return service.getLocationByZipCode(param[0]);

        } catch (WeatherException e) {
            mException = e;
        }

        return null;
    }


}
