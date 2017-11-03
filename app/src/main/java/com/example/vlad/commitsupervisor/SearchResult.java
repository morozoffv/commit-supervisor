package com.example.vlad.commitsupervisor;

import android.support.annotation.NonNull;

import com.example.vlad.commitsupervisor.events.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResult {

    //private JSONArray events;
//    private int responseCode;
    private boolean isSuccessful;

    private User user;

    private List<Commit> commitsList = new ArrayList<>();
//    private List<PushEvent> pushEventsList = new ArrayList<>();
//    private List<IssueCommentEvent> issueCommentEventsList = new ArrayList<>();
//    private List<PullRequestReviewCommentEvent> pullRequestReviewCommentEventsList = new ArrayList<>();  //beautiful name
//    private List<CommitCommentEvent> commitCommentEventsList = new ArrayList<>();

    private List<Event> events = new ArrayList<>();

//    private String username;
//    private String repoName;
//    private String avatarUrl;
//    private String eventTime;
//    private String branch;
//    private String message;
//    private String profileUrl;



//    public int getResponseCode() {
//        return responseCode;
//    }
//
//    public void setResponseCode(int responseCode) {
//        this.responseCode = responseCode;
//    }

//    public JSONArray getEvents() {
//        return events;
//    }
//
//    public void setEvents(JSONArray events) {
//        this.events = events;
//    }

    public void addToEvents(Event event) {
        events.add(event);
    }

    @NonNull
    public List<Event> getEvents() {
        if (events == null) {
            return Collections.emptyList();
        }
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

//    public int getEventsCount() { return events.length(); }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Commit> getCommitsList() {
        return commitsList;
    }

    public void setCommitsList(List<Commit> commitsList) {
        this.commitsList = commitsList;
    }

//    public List<PushEvent> getPushEventsList() {
//        return pushEventsList;
//    }
//
//    public void setPushEventsList(List<PushEvent> pushEventsList) {
//        this.pushEventsList = pushEventsList;
//    }
//
//    public List<IssueCommentEvent> getIssueCommentEventsList() {
//        return issueCommentEventsList;
//    }
//
//    public void setIssueCommentEventsList(List<IssueCommentEvent> issueCommentEventsList) {
//        this.issueCommentEventsList = issueCommentEventsList;
//    }
//
//    public List<PullRequestReviewCommentEvent> getPullRequestReviewCommentEventsList() {
//        return pullRequestReviewCommentEventsList;
//    }
//
//    public void setPullRequestReviewCommentEventsList(List<PullRequestReviewCommentEvent> pullRequestReviewCommentEventsList) {
//        this.pullRequestReviewCommentEventsList = pullRequestReviewCommentEventsList;
//    }
//
//    public List<CommitCommentEvent> getCommitCommentEventsList() {
//        return commitCommentEventsList;
//    }
//
//    public void setCommitCommentEventsList(List<CommitCommentEvent> commitCommentEventsList) {
//        this.commitCommentEventsList = commitCommentEventsList;
//    }
}
