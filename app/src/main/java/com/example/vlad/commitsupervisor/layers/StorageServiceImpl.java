package com.example.vlad.commitsupervisor.layers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
import android.support.v4.util.CircularArray;

import com.example.vlad.commitsupervisor.CommitSupervisorApp;
import com.example.vlad.commitsupervisor.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vlad on 09/11/2017.
 */

public class StorageServiceImpl implements StorageService {

    private static final String SEARCH_HISTORY_PREFERENCES = "search_history";

    private SharedPreferences searchHistory;

    @Override
    public List<User> getStoredUsers(Context context) {
        searchHistory = context.getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE);
        //noinspection unchecked
        Set<String> users = searchHistory.getStringSet("users", Collections.EMPTY_SET);
        List<String> usersList = new ArrayList<>(users);
        List<User> storedUsers = new ArrayList<>();
        for (String s : usersList) {
            User user = stringToUser(s);
            if (user != null) {
                storedUsers.add(user);
            }
        }
        return storedUsers;
    }

    @Override
    public void saveUser(User user) {
        SharedPreferences.Editor editor = searchHistory.edit();
        Set<String> usersSet = new ArraySet<>();
        usersSet.add(userToString(user));
        editor.putStringSet("users", usersSet);
        editor.apply();
    }

    private String userToString(User user) {
        return user.getLogin() + "&&&&" + user.getProfileUrl() + "&&&&" + user.getAvatarUrl();
    }

    @Nullable
    private User stringToUser(String userString) {
        User user = new User();
        String arr[] = userString.split("&&&&");
        if (arr.length < 3) {
            return null;
        }
        user.setLogin(arr[0]);
        user.setProfileUrl(arr[1]);
        user.setAvatarUrl(arr[2]);
        return user;
    }
}
