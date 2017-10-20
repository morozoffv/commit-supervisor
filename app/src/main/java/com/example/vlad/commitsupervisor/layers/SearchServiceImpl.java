package com.example.vlad.commitsupervisor.layers;

import android.app.Application;
import android.content.Intent;

import com.example.vlad.commitsupervisor.BadConnectionException;
import com.example.vlad.commitsupervisor.Commit;
import com.example.vlad.commitsupervisor.JSONAsyncTask;
import com.example.vlad.commitsupervisor.SearchResult;
import com.example.vlad.commitsupervisor.User;
import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.layers.SearchService;
import com.example.vlad.commitsupervisor.parsers.EventParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.vlad.commitsupervisor.CommitSupervisorApp.ACTION_SEARCH_COMPLETED;
import static com.example.vlad.commitsupervisor.CommitSupervisorApp.ACTION_SEARCH_ERROR;

/**
 * Created by vlad on 19/10/2017.
 */

public class SearchServiceImpl extends Application implements SearchService {


    private ApiEvents apiEvents = new ApiEventsImpl();
    private ApiRepositories apiRepositories = new ApiRepositoriesImpl();
    private ApiUsers apiUsers = new ApiUsersImpl();
    private SearchResult result;


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
                sendBroadcast(broadcastIntent);

            }

        };

        asyncTask.execute();
    }

    @Override
    public SearchResult getSearchResult() {
        return result;
    }

    @Override
    public void loadAutocompletionsForUsername(String username) {

    }
}
