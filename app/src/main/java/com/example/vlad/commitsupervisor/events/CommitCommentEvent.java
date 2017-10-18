package com.example.vlad.commitsupervisor.events;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vlad on 14.10.2017.
 */

public class CommitCommentEvent extends CommentEvent {

    //TODO: add title
    private String title;
    private String commentUrl;
    private String comment;

    public CommitCommentEvent() {
        super(Event.COMMIT_COMMENT);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public String getComment() {
        return comment;
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
//            createdAt = rawEvent.getString("created_at");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
