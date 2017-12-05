package com.example.vlad.commitsupervisor.layers;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private StorageService storageService;


    private ArrayList<User> userList = new ArrayList<>();

    public SearchServiceImpl(ApiEvents apiEvents, ApiRepositories apiRepositories, ApiUsers apiUsers, BroadcastSender broadcastSender, StorageService storageService) {
        this.apiEvents = apiEvents;
        this.apiRepositories = apiRepositories;
        this.apiUsers = apiUsers;
        this.broadcastSender = broadcastSender;
        this.storageService = storageService;
    }


    @Override
    public void fetchUserActivity(final String username, final Date date) {

        @SuppressLint("StaticFieldLeak") JSONAsyncTask asyncTask = new JSONAsyncTask() {
            @Override
            protected Void doInBackground(String... params) {

                result = new SearchResult();

                final User user = apiUsers.getUser(username);

                storageService.saveUser(user);

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

        @SuppressLint("StaticFieldLeak") AutoCompleteAsyncTask autoCompleteAsyncTask = new AutoCompleteAsyncTask() {
            @Override
            protected void onPostExecute(Void aVoid) {
                Intent intentUser = new Intent(CommitSupervisorApp.ACTION_USERS_RECEIVED);
                intentUser.putExtra("users", userList);
                intentUser.putExtra("input", username);
                broadcastSender.sendBroadcast(intentUser);
            }

            @Override
            protected Void doInBackground(String... params) {
                List<User> userList = apiUsers.getSearchUsers(username, 10);
                SearchServiceImpl.this.userList.clear();
                SearchServiceImpl.this.userList.addAll(userList);
                return null;
            }


        };
        autoCompleteAsyncTask.execute();
    }
}
