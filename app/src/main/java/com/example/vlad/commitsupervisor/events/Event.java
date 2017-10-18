package com.example.vlad.commitsupervisor.events;

import org.json.JSONObject;

/**
 * Created by vlad on 13/10/2017.
 */

public class Event {

    private String repoName;
    private String createdAt;
    private String type;

    public static final String COMMIT_COMMENT = "CommitCommentEvent";
    public static final String ISSUE_COMMENT = "IssueCommentEvent";
    public static final String PULL_REQUEST_COMMENT = "PullRequestReviewCommentEvent";
    public static final String PUSH_EVENT = "PushEvent";

    public Event(String type) {
        this.type = type;
    }

    public String getType() {
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
