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


    @Override
    protected SearchResult doInBackground(String... params) {

        String s;
        String json = null;
        JSONArray rawJson = new JSONArray();
        //JSONArray pushEventArray = new JSONArray();

        ArrayList<Commit> commitsList = new ArrayList<>();
        ArrayList<PushEvent> pushEventsList = new ArrayList<>();
        ArrayList<IssueCommentEvent> issueCommentEventsList = new ArrayList<>();
        ArrayList<PullRequestReviewCommentEvent> pullRequestReviewCommentEventsList = new ArrayList<>();  //beautiful name
        ArrayList<CommitCommentEvent> commitCommentEventsList = new ArrayList<>();
        User user = new User();

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
                //TODO: should i begin from 1? (not from 0)
                for (int j = 0; j < rawJson.length(); j++) {
                    JSONObject rawEvent = rawJson.getJSONObject(j);
                    switch (rawEvent.getString("type")) {
                        case "PushEvent":
                            PushEvent pushEvent = new PushEvent();
                            pushEvent.setEventData(rawEvent);
                            pushEventsList.add(pushEvent);

                            //pushEventsList.add(new PushEvent().setEventData(rawEvent));

                            break;
                        case "CommitCommentEvent":
                            CommitCommentEvent commitCommentEvent = new CommitCommentEvent();
                            commitCommentEvent.setEventData(rawEvent);
                            commitCommentEventsList.add(commitCommentEvent);
                            break;
                        case "IssueCommentEvent":
                            IssueCommentEvent issueCommentEvent = new IssueCommentEvent();
                            issueCommentEvent.setEventData(rawEvent);
                            issueCommentEventsList.add(issueCommentEvent);
                            break;
                        case "PullRequestReviewCommentEvent":
                            PullRequestReviewCommentEvent pullRequestReviewCommentEvent = new PullRequestReviewCommentEvent();
                            pullRequestReviewCommentEvent.setEventData(rawEvent);
                            pullRequestReviewCommentEventsList.add(pullRequestReviewCommentEvent);
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
        searchResult.setCommitCommentEventsList(commitCommentEventsList);
        searchResult.setIssueCommentEventsList(issueCommentEventsList);
        searchResult.setPullRequestReviewCommentEventsList(pullRequestReviewCommentEventsList);

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

        try {
            URL url = new URL("https://api.github.com/users/" + params[0].trim());
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
            JSONObject rawJsonUser = new JSONObject(json);
            user.setUsername(rawJsonUser.getString("login"));
            user.setAvatarUrl(rawJsonUser.getString("avatar_url"));
            user.setProfileUrl(rawJsonUser.getString("html_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        searchResult.setUser(user);


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
