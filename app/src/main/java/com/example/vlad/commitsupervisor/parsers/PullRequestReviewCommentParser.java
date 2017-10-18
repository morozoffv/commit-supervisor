package com.example.vlad.commitsupervisor.parsers;
import com.example.vlad.commitsupervisor.events.CommentEvent;
import com.example.vlad.commitsupervisor.events.Event;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 17/10/2017.
 */

public class PullRequestReviewCommentParser {

    public static CommentEvent parse(final JSONObject rawEvent) {
        try {
            CommentEvent pullRequestReviewCommentEvent = new CommentEvent(EventTypes.PullRequestReviewCommentEvent);

            final JSONObject repo = rawEvent.getJSONObject("repo");
            pullRequestReviewCommentEvent.setRepoName(repo.getString("name"));

            final JSONObject payload = rawEvent.getJSONObject("payload");
            final JSONObject comment = payload.getJSONObject("comment");

            pullRequestReviewCommentEvent.setCommentUrl(comment.getString("html_url"));

            pullRequestReviewCommentEvent.setComment(comment.getString("body"));

            pullRequestReviewCommentEvent.setCreatedAt(rawEvent.getString("created_at"));  //get created_at from "comment" or from "root"

            final JSONObject pullRequest = payload.getJSONObject("pull_request");
            pullRequestReviewCommentEvent.setTitle(pullRequest.getString("title"));

            return pullRequestReviewCommentEvent;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
