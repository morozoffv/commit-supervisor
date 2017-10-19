package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.BadConnectionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vlad on 19/10/2017.
 */

public class NetworkImpl implements Network {

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    @Nullable
    @Override
    public JSONObject getObjectFromUrl(@NonNull URL url) {
        try {
            return new JSONObject(getStringFromUrl(url));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public JSONArray getArrayFromUrl(@NonNull URL url) {
        try {
            return new JSONArray(getStringFromUrl(url));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getStringFromUrl(URL url) {

        try {
            HttpURLConnection   connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic bW9yb3pvZmZ2OjNrdTV4cWR1");

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            String s;
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            bufferedReader.close();
            streamReader.close();

            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
