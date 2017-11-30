package com.example.vlad.commitsupervisor.events;

import com.example.vlad.commitsupervisor.parsers.EventTypes;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by vlad on 13/10/2017.
 */

public class Event implements Serializable {

    private String repoName;
    private String createdAt;
    private EventTypes type;

    public Event(EventTypes type) {
        this.type = type;
    }

    public EventTypes getType() {
        return type;
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

    //abstract public void setEventData(JSONObject rawEvent);
}
