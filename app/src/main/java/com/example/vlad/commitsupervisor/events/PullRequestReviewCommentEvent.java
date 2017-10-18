package com.example.vlad.commitsupervisor.events;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad on 14.10.2017.
 */

public class PullRequestReviewCommentEvent extends CommentEvent {

    private String commentUrl;
    private String comment;
    private String title;

    public PullRequestReviewCommentEvent() {
        super(Event.PULL_REQUEST_COMMENT);
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public String getComment() {
        return comment;
    }

    public String getTitle() {
        return title;
    }

//    @Override
//    public void setEventData(JSONObject rawEvent) {
//        try {
//            JSONObject repo = rawEvent.getJSONObject("repo");
//            repoName = repo.getString("name");
//
//            JSONObject payload = rawEvent.getJSONObject("payload");
//            JSONObject comment = payload.getJSONObject("comment");
//
//            commentUrl = comment.getString("html_url");
//
//            this.comment = comment.getString("body");
//
//            createdAt = rawEvent.getString("created_at");  //get created_at from "comment" or from "root"
//
//            JSONObject pullRequest = payload.getJSONObject("pull_request");
//            title = pullRequest.getString("title");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
