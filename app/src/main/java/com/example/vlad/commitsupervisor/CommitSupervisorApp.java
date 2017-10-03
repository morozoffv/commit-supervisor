package com.example.vlad.commitsupervisor;

import android.app.Application;
import android.support.annotation.Nullable;

/**
 * Created by vlad on 03/10/2017.
 */

public class CommitSupervisorApp extends Application {

    @Nullable private SearchResult result;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void search() {
        sendBroadcast();
    }

    @Nullable
    public SearchResult getResult() {
        return result;
    }

}
