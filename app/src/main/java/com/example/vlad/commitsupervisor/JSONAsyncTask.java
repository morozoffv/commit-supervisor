package com.example.vlad.commitsupervisor;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

interface AsyncResponce {
    void processFinish(JSONArray j);
    void cancelAsyncTask();
}

public class JSONAsyncTask extends AsyncTask <String, Void, JSONArray> {

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    private AsyncResponce delegate = null;

    public JSONAsyncTask(AsyncResponce delegate) {
        this.delegate = delegate;
    }

    @Override
    protected JSONArray doInBackground(String... params) {

        String s;
        String json = "";

        try {

            URL url = new URL("https://api.github.com/users/" + params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.connect();

            if (connection.getResponseCode() == 404) {
                publishProgress();  //work with ui in onProgressUpdate, if user is not found
                cancel(false);     //TODO: understand what this parameter means

            }

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);
            }
            bufferedReader.close();
            streamReader.close();

            json = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray commits = null;
        try {

            commits = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commits;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        delegate.processFinish(jsonArray);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        delegate.cancelAsyncTask();
    }
}
