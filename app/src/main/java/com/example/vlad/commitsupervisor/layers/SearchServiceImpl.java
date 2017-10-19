package com.example.vlad.commitsupervisor.layers;

import com.example.vlad.commitsupervisor.BadConnectionException;
import com.example.vlad.commitsupervisor.Commit;
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

/**
 * Created by vlad on 19/10/2017.
 */

public class SearchServiceImpl implements SearchService {


    private ApiEvents apiEvents;
    private  ApiRepositories apiRepositories;
    private ApiUsers apiUsers;
    private SearchResult result;


    @Override
    public void fetchUserActivity(String username, Date date) {

        result = new SearchResult();

        final User user = apiUsers.getUser(username);

        if (user == null) {
            result.setSuccessful(false);
            return;
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
    }

    @Override
    public SearchResult getSearchResult() {
        return result;
    }

    @Override
    public void loadAutocompletionsForUsername(String username) {

    }
}