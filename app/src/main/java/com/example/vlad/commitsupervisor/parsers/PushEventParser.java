package com.example.vlad.commitsupervisor.parsers;

import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.events.PushEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 17/10/2017.
 */

public class PushEventParser {

    @Nullable
    public static PushEvent parse(final JSONObject rawEvent) {
        try {
            final PushEvent pushEvent = new PushEvent();

            final JSONObject repo = rawEvent.getJSONObject("repo");
            pushEvent.setRepoName(repo.getString("name"));

            final JSONObject payload = rawEvent.getJSONObject("payload");
            pushEvent.setBranch(payload.getString("ref"));

            final JSONArray commits = payload.getJSONArray("commits");
            pushEvent.setCommitNumber(commits.length());

            pushEvent.setCreatedAt(rawEvent.getString("created_at"));

            return pushEvent;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
