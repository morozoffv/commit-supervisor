package com.example.vlad.commitsupervisor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 13/10/2017.
 */

public class PushEvent extends Event {

    private int commitNumber;
    private String branch;
    private String repoName;
    private String createdAt;

    public int getCommitNumber() {
        return commitNumber;
    }

    public void setCommitNumber(int commitNumber) {
        this.commitNumber = commitNumber;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setEventData(JSONObject rawEvent) {  //TODO: remove setters? //TODO: should i return PushEvent object?
        try {
//            JSONObject repo = rawEvent.getJSONObject("repo");
//            this.setRepoName(repo.getString("name"));
//
//            JSONObject payload = rawEvent.getJSONObject("payload");
//            this.setBranch(payload.getString("ref"));
//
//            JSONArray commits = payload.getJSONArray("commits");
//            this.setCommitNumber(commits.length());
//
//            this.setCreatedAt(rawEvent.getString("created_at"));

            JSONObject repo = rawEvent.getJSONObject("repo");
            repoName = repo.getString("name");

            JSONObject payload = rawEvent.getJSONObject("payload");
            branch = payload.getString("ref");

            JSONArray commits = payload.getJSONArray("commits");
            commitNumber = commits.length();

            createdAt = rawEvent.getString("created_at");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
