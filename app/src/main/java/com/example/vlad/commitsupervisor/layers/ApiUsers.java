package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taraskreknin on 18/10/2017.
 */

interface ApiUsers extends Api {

    @NonNull
    List<String> findUsersByName(final String name);

    @Nullable
    User getUser(final String username);

    @NonNull
    List<User> getSearchUsers(final String username, final int quantity);

}
