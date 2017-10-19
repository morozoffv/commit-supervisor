package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;

import com.example.vlad.commitsupervisor.BadConnectionException;
import com.example.vlad.commitsupervisor.User;
import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.parsers.EventParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 19/10/2017.
 */

public class ApiEventsImpl implements ApiEvents {
    @NonNull
    @Override
    public Network getNetwork() { //TODO: ?
        return null;
    }

    @NonNull
    @Override
    public List<Event> getUserEvents(@NonNull User user) {
        Network network = new NetworkImpl();
        List<Event> events = new ArrayList<>();
        JSONArray rawJson;
        for (int i = 0; i < 3; i++) {
            try {
                URL url = new URL("https://api.github.com/users/" + user.getLogin().trim() + "/events?page=" + i + "&per_page=100");
                rawJson = new JSONArray(network.getArrayFromUrl(url));
                for (int j = 0; j < rawJson.length(); j++) {
                    final Event event = EventParser.parse(rawJson.getJSONObject(j));
                    if (event != null) {
                        events.add(event);
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return events;
    }

}
