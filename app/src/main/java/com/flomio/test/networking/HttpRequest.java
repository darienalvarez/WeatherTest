package com.flomio.test.networking;

import com.flomio.test.exception.InvalidRequestException;
import com.flomio.test.exception.NetworkException;
import com.flomio.test.exception.RequestExcededException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Created by darien
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

    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }

    public String makeRequest(String url) throws NetworkException {
        try {
            if (!canRequestService()) {
                throw new RequestExcededException();
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

                return response;
            } else {
                throw new InvalidRequestException();
            }
        } catch (IOException e) {
            throw new NetworkException();
        }
    }

    private boolean canRequestService() {
        if (differenceInMinutes() == 0) {
            countDownLatch.countDown();
        } else {
            countDownLatch = new CountDownLatch(10);
        }

        return countDownLatch.getCount() > 0;
    }

    private long differenceInMinutes() {
        synchronized (this) {
            return (System.currentTimeMillis() - previousRequestTime) / (60 * 1000);
        }
    }
}
