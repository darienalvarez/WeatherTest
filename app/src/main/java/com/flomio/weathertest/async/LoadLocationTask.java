package com.flomio.weathertest.async;

import com.flomio.weathertest.async.dto.Location;
import com.flomio.weathertest.exception.WeatherException;
import com.flomio.weathertest.networking.WeatherService;
import com.flomio.weathertest.networking.wu.WeatherServiceImpl;

/**
 * Created by Darien
 * on 5/19/16.
 */
public class LoadLocationTask<P extends String, G, R extends Location> extends AbstractTask<P, G, R> {

    @Override
    protected Location doInBackground(String... param) {

        try {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            WeatherService service = new WeatherServiceImpl();
            return service.getLocationByZipCode(param[0]);

        } catch (WeatherException e) {
            mException = e;
        }

        return null;
    }


}
