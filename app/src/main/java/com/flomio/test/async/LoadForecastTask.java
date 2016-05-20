package com.flomio.test.async;

/**
 * Created by darien on 5/19/16.
 */

import com.flomio.test.exception.WeatherException;
import com.flomio.test.networking.WeatherService;
import com.flomio.test.networking.WeatherServiceFactory;

import java.util.List;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Execute a service request to get a List of Forecast
 */
public class LoadForecastTask<P extends String, G, R extends List> extends AbstractTask<P, G, R> {

    @Override
    protected List doInBackground(String... param) {

        try {
            WeatherService service = new WeatherServiceFactory().getService("WU");
            if (service != null) {
                return service.getForecastByZipCode(param[0]);
            }
        } catch (WeatherException e) {
            mException = e;
        }

        return null;
    }


}
