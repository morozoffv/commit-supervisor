package com.example.vlad.commitsupervisor;

import org.json.JSONArray;

class SearchResult {

    private JSONArray events;
    private int responseCode;
    private boolean isSuccessful;

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

    public JSONArray getEvents() {
        return events;
    }

    public void setEvents(JSONArray events) {
        this.events = events;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public int getEventsCount() { return events.length(); }





}
