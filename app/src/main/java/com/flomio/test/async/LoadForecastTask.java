package com.flomio.test.async;

import com.flomio.test.exception.WeatherException;
import com.flomio.test.networking.WeatherService;
import com.flomio.test.networking.WeatherServiceFactory;
import com.flomio.test.networking.dto.Weather;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Execute a service request to get a List of Forecast
 */
public class LoadForecastTask extends AbstractTask<String, Void, Weather> {

    public LoadForecastTask(TaskCallback<Weather> callback) {
        super(callback);
    }

    @Override
    protected Weather doInBackground(String... param) {

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
