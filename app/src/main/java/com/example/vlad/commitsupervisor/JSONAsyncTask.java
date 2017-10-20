package com.example.vlad.commitsupervisor;

import android.os.AsyncTask;

import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.parsers.EventParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import java.util.Locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class JSONAsyncTask extends AsyncTask <String, Void, /*SearchResult*/ Void> {

//    private static final String REQUEST_METHOD = "GET";
//    private static final int READ_TIMEOUT = 15000;
//    private static final int CONNECTION_TIMEOUT = 15000;
//
//    private List<Commit> commitsList = new ArrayList<>();
////    private List<PushEvent> pushEventsList = new ArrayList<>();
////    private List<IssueCommentEvent> issueCommentEventsList = new ArrayList<>();
////    private List<PullRequestReviewCommentEvent> pullRequestReviewCommentEventsList = new ArrayList<>();  //beautiful name
////    private List<CommitCommentEvent> commitCommentEventsList = new ArrayList<>();
//    private User user = new User();
//
//    private SearchResult searchResult = new SearchResult();

    @Override
    protected abstract Void doInBackground(String... params);

    @Override
    protected abstract void onPostExecute(Void aVoid);

    //        JSONArray rawJson;
//
//        ArrayList<String> reposName = new ArrayList<>();    //TODO: create different class with reposName and pushed_date for data filtration

        //TODO: github api has a limit for requests (60 per hour), authentificated users have a limit up to 5k requests per hour. (add auth)
//        for (int i = 0; i < 3; i++) {   //github api allows to get only 300 events (3 x 100)
//            try {
//                URL url = new URL("https://api.github.com/users/" + params[0].trim() + "/events?page=" + i + "&per_page=100");
//                rawJson = new JSONArray(getJSONStringFromURL(url));
//                for (int j = 0; j < rawJson.length(); j++) {
////                    setEvents(rawJson.getJSONObject(j)); //JSONObject rawEvent
//                    final Event event = EventParser.parse(rawJson.getJSONObject(j));
//                    if (event != null) {
//                        searchResult.addToEvents(event);
//                    }
//
//                }
//                searchResult.setPushEventsList(pushEventsList);  //into searchResult
//                searchResult.setCommitCommentEventsList(commitCommentEventsList);
//                searchResult.setIssueCommentEventsList(issueCommentEventsList);
//                searchResult.setPullRequestReviewCommentEventsList(pullRequestReviewCommentEventsList);

//            } catch (JSONException e) {
//                e.printStackTrace();
//                searchResult.setSuccessful(false);
//                return searchResult;
//            } catch(IOException e) {
//                e.printStackTrace();
//                searchResult.setSuccessful(false);
//                return searchResult;
//            } catch (BadConnectionException e) {
//                e.printStackTrace();
//                searchResult.setSuccessful(false);
//                return searchResult;
//            }
//        }
//
//
//        try {
//            URL url = new URL("https://api.github.com/users/" + params[0].trim() + "/repos");
//            rawJson = new JSONArray(getJSONStringFromURL(url));
//        } catch (JSONException e) {
//            e.printStackTrace();
//            searchResult.setSuccessful(false);
//            return searchResult;
//        } catch (IOException e) {
//            e.printStackTrace();
//            searchResult.setSuccessful(false);
//            return searchResult;
//        } catch (BadConnectionException e) {
//            e.printStackTrace();
//            searchResult.setSuccessful(false);
//            return searchResult;
//        }
//
//        try {
//            for (int i = 0; i < rawJson.length(); i++) {
//                reposName.add(rawJson.getJSONObject(i).getString("name"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            searchResult.setSuccessful(false);
//            return searchResult;
//        }
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);  //if date will change during processing, can it cause the problems?
//        Date date = new Date();
//
//        for (String repname: reposName) {
//            try {
//                URL url = new URL("https://api.github.com/repos/" + params[0].trim() + "/" + repname + "/commits?since=" + dateFormat.format(date) + "00:00:00Z");
//                JSONArray rawCommits = new JSONArray(getJSONStringFromURL(url));
//                setCommits(rawCommits, repname);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                searchResult.setSuccessful(false);
//                return searchResult;
//            } catch (IOException e) {
//                e.printStackTrace();
//                searchResult.setSuccessful(false);
//                return searchResult;
//            } catch (BadConnectionException e) {
//                e.printStackTrace();
//                searchResult.setSuccessful(false);
//                return searchResult;
//            }
//        }
//
//        try {
//            URL url = new URL("https://api.github.com/users/" + params[0].trim());
//            JSONObject rawJsonUser = new JSONObject(getJSONStringFromURL(url));
//            setUser(rawJsonUser);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            searchResult.setSuccessful(false);
//            return searchResult;
//        } catch (IOException e) {
//            e.printStackTrace();
//            searchResult.setSuccessful(false);
//            return searchResult;
//        } catch (BadConnectionException e) {
//            e.printStackTrace();
//            searchResult.setSuccessful(false);
//            return searchResult;
//        }

//        searchResult.setSuccessful(true);
//        return searchResult;
//    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
////    @Override
////    protected void onPostExecute(SearchResult searchResult) {
////    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {
//        //delegate.cancelAsyncTask();
//    }

//    private String getJSONStringFromURL(URL url) throws IOException, BadConnectionException { //i can't return JSONArray or JSONObject because i don't know which of them will be returned
//
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//        connection.setRequestProperty("Authorization", "Basic bW9yb3pvZmZ2OjNrdTV4cWR1");
//
//        connection.setRequestMethod(REQUEST_METHOD);
//        connection.setConnectTimeout(CONNECTION_TIMEOUT);
//        connection.setReadTimeout(READ_TIMEOUT);
//
//        connection.connect();
//        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//            throw new BadConnectionException("Bad connection, response code: " + connection.getResponseCode());
//        }
//
//        String s;
//        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
//        BufferedReader bufferedReader = new BufferedReader(streamReader);
//        StringBuilder stringBuilder = new StringBuilder();
//        while ((s = bufferedReader.readLine()) != null) {
//            stringBuilder.append(s);
//        }
//        bufferedReader.close();
//        streamReader.close();
//
//        return stringBuilder.toString();
//    }
//
//    private void setCommits(JSONArray rawJson, String repname) throws JSONException { //remove repname
//        for (int i = 0; i < rawJson.length(); i++) {
//
//            Commit commit = new Commit();
//
//            JSONObject commitJson = new JSONObject(rawJson.getJSONObject(i).getString("commit"));
//
//            JSONObject committer = commitJson.getJSONObject("committer");
//            commit.setCommitterName(committer.getString("name"));
//            commit.setCommitDate(committer.getString("date"));
//
//            commit.setMessage(commitJson.getString("message"));
//
//            commit.setCommitUrl(rawJson.getJSONObject(i).getString("html_url"));
//
//            commit.setRepoName(repname);
//
//            commitsList.add(commit);
//
//        }
//
//        searchResult.setCommitsList(commitsList);
//    }
//
//    private void setUser(JSONObject rawJsonUser) throws JSONException {
//        user.setLogin(rawJsonUser.getString("login"));
//        user.setAvatarUrl(rawJsonUser.getString("avatar_url"));
//        user.setProfileUrl(rawJsonUser.getString("html_url"));
//
//        searchResult.setUser(user);
//    }
}
