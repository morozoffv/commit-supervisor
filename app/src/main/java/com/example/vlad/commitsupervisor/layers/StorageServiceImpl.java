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
import java.util.LinkedHashSet;
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
    private Context context;
    final private int capacity = 5;

    public StorageServiceImpl(Context context) {
        this.context = context;
    }

    /*format:
        "login1&&&profileurl1&&&avatarurl1<>login2&&&profileur21&&&avatarurl2<>..."
     */

    /* parse() and unparse() for separated users */
    /* userToString and stringToUser() for data fields of User object */

    @Override
    public List<User> getStoredUsers() {
        String arr[] = parse();
        List<User> storedUsers = new ArrayList<>();
        for (String s : arr) {
            User user = stringToUser(s);
            if (user != null) {
                storedUsers.add(user);
            }
        }
        //Set<User> usersSet = new ArraySet<>(storedUsers);
        return storedUsers;
    }

    @Override
    public void saveUser(User user) {
        String rawUsers = getRawUsers();
        String parsedArr[] = parse();
        String userString = userToString(user);
        for (String s: parsedArr) {
            if (s.equals(userString)) return;                           //does it possible that profile and avatar url will be changed and same user will be saved both times?
        }
        SharedPreferences.Editor editor = searchHistory.edit();

        if (parsedArr.length < capacity) {
            rawUsers = userString + "<>" + rawUsers;
        }
        else {
            String temp[] = new String[capacity + 1];       //arrays length of capacity + 1 for rearrangement
            String arr[] = new String[capacity + 1];
            for (int i = 0; i < capacity; i++) {
                temp[i] = parsedArr[i];
            }
            //rawUsers = userString + "<>" + rawUsers.substring(rawUsers.index(">") + 1);
            for (int i = 1; i < capacity + 1; i++) {
                arr[i] = temp[i - 1];
            }

            arr[0] = userString;
            arr[capacity] = "";

            rawUsers = unparse(arr);
        }
        editor.putString("users", rawUsers);
        editor.apply();

    }

    private String[] parse() {
        return getRawUsers().split("<>");
    }

    private String unparse(String[] arr) {
        String rawUsers = "";
        for (String s: arr) {
            if (!s.equals("") || s.equals("null"))
            rawUsers = rawUsers + s + "<>";
        }
        return rawUsers;
    }

    private String userToString(User user) {
        return user.getLogin() + "&&&" + user.getProfileUrl() + "&&&" + user.getAvatarUrl();
    }

    @Nullable
    private User stringToUser(String userString) {
        User user = new User();
        String arr[] = userString.split("&&&");
        if (arr.length < 3) {
            return null;
        }
        user.setLogin(arr[0]);
        user.setProfileUrl(arr[1]);
        user.setAvatarUrl(arr[2]);
        return user;
    }

    private String getRawUsers() {
        searchHistory = context.getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE);
        //noinspection unchecked
        return searchHistory.getString("users", "");
    }



}
