package com.example.vlad.commitsupervisor.parsers;

import com.example.vlad.commitsupervisor.events.CommitCommentEvent;
import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.events.IssueCommentEvent;
import com.example.vlad.commitsupervisor.events.PullRequestReviewCommentEvent;
import com.example.vlad.commitsupervisor.events.PushEvent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 17/10/2017.
 */

public class EventParser {

    public static Event parse(JSONObject rawEvent)  {

        try {
            switch (rawEvent.getString("type")) {
                case "PushEvent":
                    return PushEventParser.parse(rawEvent);
                case "CommitCommentEvent":
                    CommitCommentEvent commitCommentEvent = CommitCommentParser.parse(rawEvent);
                    if (commitCommentEvent != null) {
                        commitCommentEventsList.add(commitCommentEvent);
                    }
                    break;
                case "IssueCommentEvent":
                    IssueCommentEvent issueCommentEvent = IssueCommentEventParser.parse(rawEvent);
                    if (issueCommentEvent != null) {
                        issueCommentEventsList.add(issueCommentEvent);
                    }
                    break;
                case "PullRequestReviewCommentEvent":
                    PullRequestReviewCommentEvent pullRequestReviewCommentEvent = PullRequestReviewCommentParser.parse(rawEvent);
                    if (pullRequestReviewCommentEvent != null) {
                        pullRequestReviewCommentEventsList.add(pullRequestReviewCommentEvent);
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
