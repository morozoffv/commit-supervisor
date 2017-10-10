package com.example.vlad.commitsupervisor;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;


public class JSONAsyncTask extends AsyncTask <String, Void, SearchResult> {

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int responceCode = 200;


    @Override
    protected SearchResult doInBackground(String... params) {

        String s;
        String json = "";
        JSONArray pushEventArray = new JSONArray();
        SearchResult searchResult = new SearchResult();


        //TODO: github api has a limit for requests (60 per hour), authentificated users have a limit up to 5k requests per hour. (add auth)
        for (int i = 0; i < 3; i++) {   //github api allows to get only 300 events (3 x 100)
            try {
                URL url = new URL("https://api.github.com/users/" + params[0].trim() + "/events?page=" + i + "&per_page=100");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);

                connection.connect();

                searchResult.setResponseCode(connection.getResponseCode());
                if (searchResult.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    searchResult.setSuccessful(false);
                    return searchResult;
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

            } catch(IOException e) {
                e.printStackTrace();
                searchResult.setSuccessful(false);
                return searchResult;
            }

            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
                searchResult.setSuccessful(false);
                return searchResult;
            }

            try {
                for (int j = 0; j < jsonArray.length(); j++) {
                    if (jsonArray.getJSONObject(j).getString("type").equals("PushEvent")) {
                        pushEventArray.put(jsonArray.getJSONObject(j));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                searchResult.setSuccessful(false);
                return searchResult;
            }

            searchResult.setEvents(pushEventArray);
        }
        searchResult.setSuccessful(true);

        

        return searchResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

//    @Override
//    protected void onPostExecute(SearchResult searchResult) {
//    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //delegate.cancelAsyncTask();
    }
}
