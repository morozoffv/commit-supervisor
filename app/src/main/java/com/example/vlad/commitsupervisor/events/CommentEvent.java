package com.example.vlad.commitsupervisor.events;

/**
 * Created by vlad on 17/10/2017.
 */

public class CommentEvent extends Event {

    private String title;
    private String commentUrl;
    private String comment;

    public CommentEvent(String type) {
        super(type);
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

}
