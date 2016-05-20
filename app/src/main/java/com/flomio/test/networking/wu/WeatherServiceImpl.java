package com.flomio.test.networking.wu;

import com.flomio.test.exception.InvalidRequestException;
import com.flomio.test.exception.InvalidResponseException;
import com.flomio.test.exception.NetworkException;
import com.flomio.test.networking.HttpRequest;
import com.flomio.test.networking.WeatherService;
import com.flomio.test.networking.dto.Forecast;
import com.flomio.test.networking.dto.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darien
 * on 5/19/16.
 */
public class WeatherServiceImpl implements WeatherService {

    private static final String BASE_PATH = "http://api.wunderground.com/api/";
    private static final String API_KEY = "e3c2ee7d70ae07ff";

    @Override
    public Location getLocationByZipCode(String zipCode) throws NetworkException {

        try {
            String url = BASE_PATH + API_KEY + "/geolookup/q/" + URLEncoder.encode(zipCode, "UTF-8") + ".json";

            String response = HttpRequest.getInstance().makeRequest(url);
            return processLocationResponse(response);
        }  catch (JSONException e) {
            throw new InvalidResponseException();
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException();
        }
    }

    public List<Forecast> getForecastByZipCode(String zipCode) throws NetworkException {

        try {
            String url = BASE_PATH + API_KEY + "/geolookup/conditions/forecast/q/" + URLEncoder.encode(zipCode, "UTF-8") + ".json";

            String response = HttpRequest.getInstance().makeRequest(url);
            return processForecastResponse(response);
        } catch (JSONException e) {
            throw new InvalidResponseException();
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException();
        }
    }

    private Location processLocationResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);

        JSONObject location = jsonObject.optJSONObject("location");
        if (location != null) {
            String country = location.optString("country");
            String state = location.optString("state");
            String city = location.optString("city");

            return new Location(country, state, city);
        }
        return null;
    }

    private List<Forecast> processForecastResponse(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);

        JSONObject forecast = jsonObject.getJSONObject("forecast");

        List<Forecast> forecasts = new ArrayList<>();
        if (forecast != null) {

            JSONObject forecastDay = forecast.getJSONObject("forecastday");
            JSONArray forecastDayList = forecastDay.getJSONArray("forecastday");
            for (int i = 0; i < forecastDayList.length(); i++) {
                JSONObject currentForecast = forecastDayList.getJSONObject(i);
                forecasts.add(new Forecast(currentForecast.getString("title"),
                        currentForecast.getString("fcttext")));
            }
        }
        return forecasts;
    }
}
