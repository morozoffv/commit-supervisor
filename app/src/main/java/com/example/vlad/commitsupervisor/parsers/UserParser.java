package com.example.vlad.commitsupervisor.parsers;

import com.example.vlad.commitsupervisor.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vlad on 20/10/2017.
 */

public class UserParser {

    public static User parser(JSONObject rawUser) {
        User user = new User();
        try {
            user.setLogin(rawUser.getString("login"));
            user.setAvatarUrl(rawUser.getString("avatar_url"));
            user.setProfileUrl(rawUser.getString("html_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

}
