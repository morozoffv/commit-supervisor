package com.example.vlad.commitsupervisor.layers;

import android.content.Intent;
import android.util.Log;

import com.example.vlad.commitsupervisor.AutoCompleteAsyncTask;
import com.example.vlad.commitsupervisor.BroadcastSender;
import com.example.vlad.commitsupervisor.Commit;
import com.example.vlad.commitsupervisor.CommitSupervisorApp;
import com.example.vlad.commitsupervisor.JSONAsyncTask;
import com.example.vlad.commitsupervisor.SearchResult;
import com.example.vlad.commitsupervisor.User;
import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.parsers.UserParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.vlad.commitsupervisor.CommitSupervisorApp.ACTION_SEARCH_COMPLETED;
import static com.example.vlad.commitsupervisor.CommitSupervisorApp.ACTION_SEARCH_ERROR;

/**
 * Created by vlad on 19/10/2017.
 */

public class SearchServiceImpl implements SearchService {

    private static final String TAG = "SearchServiceImpl";


    private ApiEvents apiEvents;
    private ApiRepositories apiRepositories;
    private ApiUsers apiUsers;
    private SearchResult result;
    private BroadcastSender broadcastSender;

    private ArrayList<User> userList = new ArrayList<>();

    public SearchServiceImpl(ApiEvents apiEvents, ApiRepositories apiRepositories, ApiUsers apiUsers, BroadcastSender broadcastSender) {
        this.apiEvents = apiEvents;
        this.apiRepositories = apiRepositories;
        this.apiUsers = apiUsers;
        this.broadcastSender = broadcastSender;
    }


    @Override
    public void fetchUserActivity(final String username, final Date date) {

        JSONAsyncTask asyncTask = new JSONAsyncTask() {
            @Override
            protected Void doInBackground(String... params) {

                result = new SearchResult();

                final User user = apiUsers.getUser(username);

                if (user == null) {
                    result.setSuccessful(false);
                    return null;
                }

                final List<Event> events = apiEvents.getUserEvents(user);

                final List<String> repos = apiRepositories.getUserRepositories(user);

                final List<Commit> commits = new ArrayList<>();
                for (String repo: repos) {
                    commits.addAll(apiRepositories.getCommits(repo, user));
                }

                result.setUser(user);
                result.setEvents(events);
                result.setCommitsList(commits);

                result.setSuccessful(true);

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {

                Intent broadcastIntent;
                if(result.isSuccessful()) {
                    broadcastIntent = new Intent(ACTION_SEARCH_COMPLETED);
                    broadcastIntent.putExtra("eventsCount", result.getEvents().size());
                }
                else {
                    broadcastIntent = new Intent(ACTION_SEARCH_ERROR);
                }

                broadcastSender.sendBroadcast(broadcastIntent);

            }

        };

        asyncTask.execute();
    }

    @Override
    public SearchResult getSearchResult() {
        return result;
    }

    @Override
    public void loadAutoCompletionsForUsername(final String username) {

        AutoCompleteAsyncTask autoCompleteAsyncTask = new AutoCompleteAsyncTask() {
            @Override
            protected void onPostExecute(Void aVoid) {
                //Log.i(TAG, "onPostExecute: " + username);
                Intent intentUser = new Intent(CommitSupervisorApp.ACTION_USERS_RECEIVED);
                intentUser.putExtra("users", userList);
                intentUser.putExtra("input", username);
                broadcastSender.sendBroadcast(intentUser);


            }

            @Override
            protected Void doInBackground(String... params) {
                List<User> userList = apiUsers.getSearchUsers(username, 1000);
                SearchServiceImpl.this.userList.clear();
                SearchServiceImpl.this.userList.addAll(userList);
                //Log.i(TAG, "doInBackground: " + username);


                return null;
            }
        };

        autoCompleteAsyncTask.execute();

//
//
//        String json = "{\n" +
//                "    \"login\": \"skgwazap\",\n" +
//                "    \"id\": 6984712,\n" +
//                "    \"avatar_url\": \"https://avatars3.githubusercontent.com/u/6984712?v=4\",\n" +
//                "    \"gravatar_id\": \"\",\n" +
//                "    \"url\": \"https://api.github.com/users/skgwazap\",\n" +
//                "    \"html_url\": \"https://github.com/skgwazap\",\n" +
//                "    \"followers_url\": \"https://api.github.com/users/skgwazap/followers\",\n" +
//                "    \"following_url\": \"https://api.github.com/users/skgwazap/following{/other_user}\",\n" +
//                "    \"gists_url\": \"https://api.github.com/users/skgwazap/gists{/gist_id}\",\n" +
//                "    \"starred_url\": \"https://api.github.com/users/skgwazap/starred{/owner}{/repo}\",\n" +
//                "    \"subscriptions_url\": \"https://api.github.com/users/skgwazap/subscriptions\",\n" +
//                "    \"organizations_url\": \"https://api.github.com/users/skgwazap/orgs\",\n" +
//                "    \"repos_url\": \"https://api.github.com/users/skgwazap/repos\",\n" +
//                "    \"events_url\": \"https://api.github.com/users/skgwazap/events{/privacy}\",\n" +
//                "    \"received_events_url\": \"https://api.github.com/users/skgwazap/received_events\",\n" +
//                "    \"type\": \"User\",\n" +
//                "    \"site_admin\": false,\n" +
//                "    \"name\": \"Taras Kreknin\",\n" +
//                "    \"company\": null,\n" +
//                "    \"blog\": \"\",\n" +
//                "    \"location\": null,\n" +
//                "    \"email\": null,\n" +
//                "    \"hireable\": null,\n" +
//                "    \"bio\": null,\n" +
//                "    \"public_repos\": 1,\n" +
//                "    \"public_gists\": 0,\n" +
//                "    \"followers\": 0,\n" +
//                "    \"following\": 0,\n" +
//                "    \"created_at\": \"2014-03-18T08:15:12Z\",\n" +
//                "    \"updated_at\": \"2017-10-16T15:20:20Z\"\n" +
//                "}";
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        User user = UserParser.parse(jsonObject);
//        User user2 = UserParser.parse(jsonObject);
//        user2.setLogin("vlad");
//
//
//        userList.add(user);
//        userList.add(user);
//        userList.add(user);
//        userList.add(user);
//        userList.add(user2);
//
//        Intent intentUser = new Intent(CommitSupervisorApp.ACTION_USERS_RECEIVED);
//        intentUser.putExtra("users", userList);
//        broadcastSender.sendBroadcast(intentUser);


    }
}
