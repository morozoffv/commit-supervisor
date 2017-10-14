package com.example.vlad.commitsupervisor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad on 14.10.2017.
 */

public class CommitCommentEvent extends Event {

    private String repoName;
    private String commentUrl;
    private String createdAt;
    private String comment;

    public String getRepoName() {
        return repoName;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public void setEventData(JSONObject rawEvent) {
        try {
            JSONObject repo = rawEvent.getJSONObject("repo");
            repoName = repo.getString("name");

            JSONObject payload = rawEvent.getJSONObject("payload");
            JSONObject comment = payload.getJSONObject("comment");

            commentUrl = comment.getString("html_url");

            this.comment = comment.getString("body");

            createdAt = rawEvent.getString("created_at");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
