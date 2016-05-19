package com.flomio.weathertest.networking.wu;

import com.flomio.weathertest.async.dto.Location;
import com.flomio.weathertest.exception.InvalidRequestException;
import com.flomio.weathertest.exception.InvalidResponseException;
import com.flomio.weathertest.exception.NetworkException;
import com.flomio.weathertest.networking.WeatherService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setUseCaches(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String response = "";
                BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String s;
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

                return processResponse(response);
            } else {
                throw new InvalidRequestException();
            }
        } catch (IOException e) {
            throw new NetworkException();
        } catch (JSONException e) {
            throw new InvalidResponseException();
        }
    }

    private Location processResponse(String response) throws JSONException {
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
}
