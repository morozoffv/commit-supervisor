package com.example.vlad.commitsupervisor.parsers;

import com.example.vlad.commitsupervisor.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 20/10/2017.
 */

public class UserParser {

    public static User parse(JSONObject rawUser) {
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

    public static ArrayList<User> searchParse(JSONObject rawUsers, int quantity) { //for autocompletions
        JSONArray jsonSearchUsers = null;
        User user = new User();
        ArrayList<User> searchUsers = new ArrayList<>();
        try {
            jsonSearchUsers = rawUsers.getJSONArray("items");

            for (int i = 0; i < quantity; i++) {
                searchUsers.add(parse(jsonSearchUsers.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchUsers;


    }


//    private static User parseAttributes(User user, JSONObject rawUser) {
//        try {
//            user.setLogin(rawUser.getString("login"));
//            user.setAvatarUrl(rawUser.getString("avatar_url"));
//            user.setProfileUrl(rawUser.getString("html_url"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}