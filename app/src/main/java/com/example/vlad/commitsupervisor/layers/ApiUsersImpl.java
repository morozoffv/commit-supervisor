package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.User;
import com.example.vlad.commitsupervisor.parsers.UserParser;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by vlad on 20/10/2017.
 */

public class ApiUsersImpl implements ApiUsers {

    Network network = new NetworkImpl();

    @NonNull
    @Override
    public Network getNetwork() {
        return network;
    }

    @NonNull
    @Override
    public List<String> findUsersByName(String name) {
        return null;
    }

    @Nullable
    @Override
    public User getUser(String username) {
        try {
            URL url = new URL("https://api.github.com/users/" + username.trim());

            JSONObject rawJsonUser = network.getObjectFromUrl(url);
            return UserParser.parser(rawJsonUser);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
