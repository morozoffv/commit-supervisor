package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.User;
import com.example.vlad.commitsupervisor.parsers.UserParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vlad on 20/10/2017.
 */

public class ApiUsersImpl implements ApiUsers {

    final private Network network;

    public ApiUsersImpl(Network network) {
        this.network = network;
    }

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
            if (rawJsonUser == null) {
                return null;
            }
            return UserParser.parse(rawJsonUser);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @NonNull
    @Override
    public List<User> getSearchUsers(String username, int quantity) {
        try {
            URL url = new URL("https://api.github.com/search/users?q=" + username.trim());

            JSONObject rawJsonUsers = network.getObjectFromUrl(url);
            if (rawJsonUsers == null) {
                return Collections.emptyList();
            }
            List<User> users = new ArrayList<>();
            users.addAll(UserParser.searchParse(rawJsonUsers, quantity));
            return users;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
