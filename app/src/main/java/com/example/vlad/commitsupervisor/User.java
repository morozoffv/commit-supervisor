package com.example.vlad.commitsupervisor;

import java.io.Serializable;

/**
 * Created by vlad on 11/10/2017.
 */

public class User implements Serializable {

    private String login;
    private String avatarUrl;
    private String profileUrl;

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }


}
