package com.example.vlad.commitsupervisor.parsers;

import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.events.Event;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 17/10/2017.
 */

public class EventParser {

    @Nullable
    public static Event parse(JSONObject rawEvent)  {

        try {
            switch (EventTypes.valueOf(rawEvent.getString("type"))) {
                case PushEvent:
                    return PushEventParser.parse(rawEvent);
                case CommitCommentEvent:
                    return CommitCommentParser.parse(rawEvent);
                case IssueCommentEvent:
                    return IssueCommentEventParser.parse(rawEvent);
                case PullRequestReviewCommentEvent:
                    return PullRequestReviewCommentParser.parse(rawEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) { //TODO: redo
            e.printStackTrace();
            return null;
        }


        return null;
    }
}
