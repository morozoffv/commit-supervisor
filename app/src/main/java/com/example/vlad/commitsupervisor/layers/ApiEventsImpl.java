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

    final private Network network;

    public ApiEventsImpl(Network network) {
        this.network = network;
    }

    @NonNull
    @Override
    public Network getNetwork() { //TODO: ?
        return network;
    }

    @NonNull
    @Override
    public List<Event> getUserEvents(@NonNull User user) {

        List<Event> events = new ArrayList<>();
        JSONArray rawJson;
        for (int i = 1; i < 4; i++) { //1-3 pages
            try {
                URL url = new URL("https://api.github.com/users/" + user.getLogin().trim() + "/events?page=" + i + "&per_page=100");
                rawJson = network.getArrayFromUrl(url);
                if (rawJson == null) {
                    return events;
                }
                for (int j = 0; j < rawJson.length(); j++) {    //TODO: github api has a limit for requests (60 per hour), authentificated users have a limit up to 5k requests per hour. (add auth)
                    final Event event = EventParser.parse(rawJson.getJSONObject(j));
                    if (event != null) {
                        events.add(event);
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return events;
    }

}
