package com.example.vlad.commitsupervisor.layers;

import android.content.Context;

import com.example.vlad.commitsupervisor.User;

import java.util.List;

/**
 * Created by vlad on 31/10/2017.
 */

public interface StorageService {

    List<User> getStoredUsers();

    void saveUser(User user);

}
