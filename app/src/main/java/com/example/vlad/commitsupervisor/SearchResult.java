package com.example.vlad.commitsupervisor;

import org.json.JSONArray;

class SearchResult {

    private JSONArray events;
    private int responceCode;
    private boolean isSuccessful;

    public int getResponceCode() {
        return responceCode;
    }

    public void setResponceCode(int responceCode) {
        this.responceCode = responceCode;
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
}
