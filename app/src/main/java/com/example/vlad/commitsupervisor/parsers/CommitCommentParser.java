package com.example.vlad.commitsupervisor.parsers;

import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.events.CommentEvent;
import com.example.vlad.commitsupervisor.events.CommitCommentEvent;
import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.events.PushEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 17/10/2017.
 */

public class CommitCommentParser {

    @Nullable
    public static CommentEvent parse(final JSONObject rawEvent) {
        try {
            final CommentEvent commitCommentEvent = new CommentEvent(Event.COMMIT_COMMENT);

            final JSONObject repo = rawEvent.getJSONObject("repo");
            commitCommentEvent.setRepoName(repo.getString("name"));

            final JSONObject payload = rawEvent.getJSONObject("payload");
            final JSONObject comment = payload.getJSONObject("comment");

            commitCommentEvent.setCommentUrl(comment.getString("html_url"));

            commitCommentEvent.setComment(comment.getString("body"));

            commitCommentEvent.setCreatedAt(rawEvent.getString("created_at"));

            return commitCommentEvent;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
