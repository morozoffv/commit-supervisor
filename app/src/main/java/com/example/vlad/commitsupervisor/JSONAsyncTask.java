package com.example.vlad.commitsupervisor;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONAsyncTask extends AsyncTask <String, Void, SearchResult> {

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int responceCode = 200;


    @Override
    protected SearchResult doInBackground(String... params) {

        String s;
        String json = null;
        JSONArray rawJson = new JSONArray();
        //JSONArray pushEventArray = new JSONArray();
        ArrayList<Commit> commitsList = new ArrayList<>();
        ArrayList<PushEvent> pushEventsList = new ArrayList<>();
        SearchResult searchResult = new SearchResult();

        ArrayList<String> reposName = new ArrayList<>();    //TODO: different class with reposName and pushed_date for data filtration


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

            try {
                rawJson = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
                searchResult.setSuccessful(false);
                return searchResult;
            }

            try {
                //should i begin from 1? (not from 0)
                for (int j = 0; j < rawJson.length(); j++) {
                    //TODO: comment events
                    switch (rawJson.getJSONObject(j).getString("type")) {
                        case "PushEvent":
                            //pushEventArray.put(rawJson.getJSONObject(j));
                            PushEvent pushEvent = new PushEvent();

                            JSONObject rawPushEvent = rawJson.getJSONObject(j);
                            JSONObject repo = rawPushEvent.getJSONObject("repo");
                            pushEvent.setRepoName(repo.getString("name"));

                            JSONObject payload = rawPushEvent.getJSONObject("payload");
                            pushEvent.setBranch(payload.getString("ref"));

                            JSONArray commits = payload.getJSONArray("commits");
                            pushEvent.setCommitNumber(commits.length());

                            pushEvent.setCreationTime(rawPushEvent.getString("created_at"));
                            pushEventsList.add(pushEvent);
                            break;
                        case "CommitCommentEvent":
                            break;
                        case "IssueCommentEvent":
                            break;
                        case "PullRequestReviewCommentEvent":
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                searchResult.setSuccessful(false);
                return searchResult;
            }

        }

        searchResult.setPushEventsList(pushEventsList);

        searchResult.setSuccessful(true);

        try {
            URL url = new URL("https://api.github.com/users/" + params[0].trim() + "/repos");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.connect();

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

        try {
            rawJson = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            for (int i = 0; i < rawJson.length(); i++) {
                reposName.add(rawJson.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            searchResult.setSuccessful(false);
            return searchResult;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);  //if date will change during processing, can it cause the problems?
        Date date = new Date();

        for (String repname: reposName) {
            try {

                URL url = new URL("https://api.github.com/repos/" + params[0].trim() + "/" + repname + "/commits?since=" + dateFormat.format(date) + "00:00:00Z");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);

                connection.connect();

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

            try {
                rawJson = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                for (int i = 0; i < rawJson.length(); i++) {

                    Commit commit = new Commit();



                    JSONObject commitJson = new JSONObject(rawJson.getJSONObject(i).getString("commit"));

                    JSONObject committer = commitJson.getJSONObject("committer");
                    commit.setCommitterName(committer.getString("name"));
                    commit.setCommitDate(committer.getString("date"));

                    commit.setMessage(commitJson.getString("message"));

                    commit.setCommitUrl(rawJson.getJSONObject(i).getString("html_url"));

                    commit.setRepoName(repname);

                    commitsList.add(commit);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                searchResult.setSuccessful(false);
                return searchResult;
            }
            searchResult.setCommitsList(commitsList);


        }


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
