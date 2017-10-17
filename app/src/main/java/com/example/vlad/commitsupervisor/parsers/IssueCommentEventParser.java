package com.example.vlad.commitsupervisor.parsers;

import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.events.CommitCommentEvent;
import com.example.vlad.commitsupervisor.events.IssueCommentEvent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 17/10/2017.
 */

public class IssueCommentEventParser {


    @Nullable
    public static IssueCommentEvent parse(final JSONObject rawEvent) {
        try {
            final IssueCommentEvent issueCommentEvent = new IssueCommentEvent();

            final JSONObject repo = rawEvent.getJSONObject("repo");
            issueCommentEvent.setRepoName(repo.getString("name"));

            final JSONObject payload = rawEvent.getJSONObject("payload");
            final JSONObject issue = payload.getJSONObject("issue");
            final JSONObject comment = payload.getJSONObject("comment");

            issueCommentEvent.setCommentUrl(comment.getString("html_url"));

            issueCommentEvent.setTitle(issue.getString("title"));

            issueCommentEvent.setComment(comment.getString("body"));

            issueCommentEvent.setCreatedAt(rawEvent.getString("created_at"));

            return issueCommentEvent;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
