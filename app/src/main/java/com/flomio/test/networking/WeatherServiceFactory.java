package com.flomio.test.networking;

import com.flomio.test.networking.wu.WeatherServiceImpl;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Service factory
 */
public class WeatherServiceFactory {

    /**
     * use get service to get an Instance of the correspondent interface implementation
     */
    public WeatherService getService(String service) {
        if (service == null) {
            return null;
        }

        if (service.equalsIgnoreCase("WU")) {
            return new WeatherServiceImpl();
        }

        return null;
    }
}
