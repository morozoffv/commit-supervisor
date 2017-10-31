package com.example.vlad.commitsupervisor.layers;

import com.example.vlad.commitsupervisor.User;

import java.util.List;

/**
 * Created by vlad on 31/10/2017.
 */

public interface StorageService {

    List<String> getUsernames();

    List<User> getStoredUsers();

    void saveUser(User user);

}
