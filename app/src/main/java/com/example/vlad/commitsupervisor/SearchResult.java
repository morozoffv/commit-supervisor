package com.example.vlad.commitsupervisor;

import java.util.ArrayList;

class SearchResult {

    //private JSONArray events;
    private int responseCode;
    private boolean isSuccessful;

    private User user;

    private ArrayList<Commit> commitsList = new ArrayList<>();
    private ArrayList<PushEvent> pushEventsList = new ArrayList<>();
    private ArrayList<IssueCommentEvent> issueCommentEventsList = new ArrayList<>();
    private ArrayList<PullRequestReviewCommentEvent> pullRequestReviewCommentEventsList = new ArrayList<>();  //beautiful name
    private ArrayList<CommitCommentEvent> commitCommentEventsList = new ArrayList<>();

//    private String username;
//    private String repoName;
//    private String avatarUrl;
//    private String eventTime;
//    private String branch;
//    private String message;
//    private String profileUrl;



    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

//    public JSONArray getEvents() {
//        return events;
//    }
//
//    public void setEvents(JSONArray events) {
//        this.events = events;
//    }

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

    public ArrayList<Commit> getCommitsList() {
        return commitsList;
    }

    public void setCommitsList(ArrayList<Commit> commitsList) {
        this.commitsList = commitsList;
    }

    public ArrayList<PushEvent> getPushEventsList() {
        return pushEventsList;
    }

    public void setPushEventsList(ArrayList<PushEvent> pushEventsList) {
        this.pushEventsList = pushEventsList;
    }

    public ArrayList<IssueCommentEvent> getIssueCommentEventsList() {
        return issueCommentEventsList;
    }

    public void setIssueCommentEventsList(ArrayList<IssueCommentEvent> issueCommentEventsList) {
        this.issueCommentEventsList = issueCommentEventsList;
    }

    public ArrayList<PullRequestReviewCommentEvent> getPullRequestReviewCommentEventsList() {
        return pullRequestReviewCommentEventsList;
    }

    public void setPullRequestReviewCommentEventsList(ArrayList<PullRequestReviewCommentEvent> pullRequestReviewCommentEventsList) {
        this.pullRequestReviewCommentEventsList = pullRequestReviewCommentEventsList;
    }

    public ArrayList<CommitCommentEvent> getCommitCommentEventsList() {
        return commitCommentEventsList;
    }

    public void setCommitCommentEventsList(ArrayList<CommitCommentEvent> commitCommentEventsList) {
        this.commitCommentEventsList = commitCommentEventsList;
    }
}
