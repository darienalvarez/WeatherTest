package com.flomio.test.networking.wu;

import com.flomio.test.exception.InvalidRequestException;
import com.flomio.test.exception.InvalidResponseException;
import com.flomio.test.exception.NetworkException;
import com.flomio.test.networking.HttpRequest;
import com.flomio.test.networking.WeatherService;
import com.flomio.test.networking.dto.Forecast;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.networking.dto.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darien
 * on 5/19/16.
 *
 * Implement Weather service for Wunderground
 */
public class WeatherServiceImpl implements WeatherService {

    private static final String BASE_PATH = "http://api.wunderground.com/api/";
    private static final String API_KEY = "e3c2ee7d70ae07ff";

    @Override
    public Location getLocationByZipCode(String zipCode) throws NetworkException {

        try {
            String url = BASE_PATH + API_KEY + "/geolookup/q/" + URLEncoder.encode(zipCode, "UTF-8") + ".json";

            JSONObject response = HttpRequest.getInstance().makeRequest(url);
            return processLocationResponse(response);
        } catch (JSONException e) {
            throw new InvalidResponseException();
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException();
        }
    }

    public Weather getForecastByZipCode(String zipCode) throws NetworkException {

        try {
            String url = BASE_PATH + API_KEY + "/geolookup/conditions/forecast/q/" + URLEncoder.encode(zipCode, "UTF-8") + ".json";

            JSONObject response = HttpRequest.getInstance().makeRequest(url);
            return processWeatherResponse(response);
        } catch (JSONException e) {
            throw new InvalidResponseException();
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException();
        }
    }

    private Location processLocationResponse(JSONObject jsonObject) throws JSONException {
        JSONObject location = jsonObject.optJSONObject("location");
        if (location != null) {
            String state = location.optString("state");
            String city = location.optString("city");

            return new Location(state, city);
        }
        throw new JSONException("invalid json");
    }

    private List<Forecast> processForecastResponse(JSONObject jsonObject) throws JSONException {
        JSONObject forecast = jsonObject.getJSONObject("forecast");

        List<Forecast> forecasts = new ArrayList<>();

        JSONObject forecastDay = forecast.getJSONObject("txt_forecast");
        JSONArray forecastDayList = forecastDay.getJSONArray("forecastday");
        for (int i = 0; i < forecastDayList.length(); i++) {
            JSONObject currentForecast = forecastDayList.getJSONObject(i);
            forecasts.add(new Forecast(currentForecast.getString("title"),
                    currentForecast.getString("fcttext")));
        }
        return forecasts;
    }

    private Weather processWeatherResponse(JSONObject jsonObject) throws JSONException {
        Location location = processLocationResponse(jsonObject);
        List<Forecast> forecastList = processForecastResponse(jsonObject);

        return new Weather(location, forecastList);
    }
}
