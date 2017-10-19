package com.example.vlad.commitsupervisor.parsers;

import com.example.vlad.commitsupervisor.Commit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 19/10/2017.
 */

public class CommitParser {

    public static Commit parse(final JSONObject rawJson, String repoName) {
        Commit commit = new Commit();

        try {


            JSONObject commitJson = rawJson.getJSONObject("commit");

            JSONObject committer = commitJson.getJSONObject("committer");
            commit.setCommitterName(committer.getString("name"));
            commit.setCommitDate(committer.getString("date"));

            commit.setMessage(commitJson.getString("message"));

            commit.setCommitUrl(rawJson.getString("html_url"));

            commit.setRepoName(repoName);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return commit;

    }
}

