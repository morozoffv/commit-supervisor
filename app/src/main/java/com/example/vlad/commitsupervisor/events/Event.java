package com.example.vlad.commitsupervisor.events;

import org.json.JSONObject;

/**
 * Created by vlad on 13/10/2017.
 */

public abstract class Event {

    private String repoName;
    private String createdAt;

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

    //abstract public void setEventData(JSONObject rawEvent);
}
