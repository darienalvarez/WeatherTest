package com.flomio.test.networking;

import com.flomio.test.exception.InvalidRequestException;
import com.flomio.test.exception.NetworkException;
import com.flomio.test.exception.RequestExceededException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Darien
 * on 5/19/16.
 *
 * Request helper
 */
public class HttpRequest {

    private static HttpRequest instance = null;

    private long previousRequestTime;
    private CountDownLatch countDownLatch;

    private HttpRequest() {
        previousRequestTime = System.currentTimeMillis();
        countDownLatch = new CountDownLatch(10);
    }

    /**
     * Get a single instance of HttpRequest
     *
     * @return HttpRequest instance
     */
    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }

    /**
     * Make a network request and return the JsonObject result
     *
     * @param url Url to make the request
     * @return JsonObject with the result
     * @throws NetworkException if any problem with the request
     * @throws JSONException    if can not convert the response to json object
     */
    public JSONObject makeRequest(String url) throws NetworkException, JSONException {
        try {
            if (!canRequestService()) {
                throw new RequestExceededException();
            }

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

                return new JSONObject(response);
            } else {
                throw new InvalidRequestException();
            }
        } catch (IOException e) {
            throw new NetworkException();
        }
    }

    /**
     * Compute your request per minute
     *
     * @return true if request by minute < 10
     */
    private boolean canRequestService() {
        long current = System.currentTimeMillis();
        if (differenceInMinutes(current) == 0) {
            countDownLatch.countDown();
        } else {
            previousRequestTime = current;
            countDownLatch = new CountDownLatch(10);
        }

        return countDownLatch.getCount() > 0;
    }

    /**
     * Compute diference in minutes between current and the last stored time
     *
     * @param current current time in millis
     * @return diference in minutes with the last stored time
     */
    private long differenceInMinutes(long current) {
        synchronized (this) {
            return (current - previousRequestTime) / (60 * 1000);
        }
    }
}
