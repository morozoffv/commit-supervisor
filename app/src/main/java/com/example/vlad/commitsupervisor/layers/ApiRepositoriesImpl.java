package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;

import com.example.vlad.commitsupervisor.Commit;
import com.example.vlad.commitsupervisor.User;
import com.example.vlad.commitsupervisor.parsers.CommitParser;

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

public class ApiRepositoriesImpl implements ApiRepositories {
    @NonNull
    @Override
    public Network getNetwork() {
        return null;
    }

    @NonNull
    @Override
    public List<String> getUserRepositories(@NonNull User user) {

        Network network = new NetworkImpl();
        List<String> reposName = new ArrayList<>();
        try {
            URL url = new URL("https://api.github.com/users/" + user.getLogin().trim() + "/repos");
            JSONArray rawJson = network.getArrayFromUrl(url);
            for (int i = 0; i < rawJson.length(); i++) {
                reposName.add(rawJson.getJSONObject(i).getString("name"));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return reposName;
    }

    @NonNull
    @Override
    public List<Commit> getCommits(@NonNull String repoName, @NonNull User user) {
        Network network = new NetworkImpl();
        List<Commit> commits = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);
        Date date = new Date();
        try {
            URL url = new URL("https://api.github.com/repos/" + user.getLogin().trim() + "/" + repoName + "/commits?since=" + dateFormat.format(date) + "00:00:00Z");
            JSONArray rawCommits = new JSONArray(network.getArrayFromUrl(url));
            for (int i = 0; i < rawCommits.length(); i++) {
                final Commit commit = CommitParser.parse(rawCommits.getJSONObject(i), repoName);
                if (commit == null) {
                    commits.add(commit);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return commits;
    }

}