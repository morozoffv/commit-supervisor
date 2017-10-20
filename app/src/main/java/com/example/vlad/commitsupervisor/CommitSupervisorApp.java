package com.example.vlad.commitsupervisor;

import android.app.Application;
import android.content.Intent;
import android.net.Network;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.layers.ApiEventsImpl;
import com.example.vlad.commitsupervisor.layers.ApiRepositoriesImpl;
import com.example.vlad.commitsupervisor.layers.ApiUsersImpl;
import com.example.vlad.commitsupervisor.layers.ApplicationCore;
import com.example.vlad.commitsupervisor.layers.SearchService;
import com.example.vlad.commitsupervisor.layers.SearchServiceImpl;

/**
 * Created by vlad on 03/10/2017.
 */

public class CommitSupervisorApp extends Application implements ApplicationCore {

    @Nullable private SearchResult result;

    BroadcastSender broadcastSender = new BroadcastSender() {
        @Override
        public void sendBroadcast(Intent intent) {
            CommitSupervisorApp.this.sendBroadcast(intent);
        }
    };

    

    ApiEventsImpl apiEvents = new ApiEventsImpl();
    ApiRepositoriesImpl apiRepositories = new ApiRepositoriesImpl();
    ApiUsersImpl apiUsers = new ApiUsersImpl();

    SearchService searchService = new SearchServiceImpl(apiEvents, apiRepositories, apiUsers, broadcastSender);


    final public static String ACTION_SEARCH_COMPLETED = "commitsupervisor.SEARCH_COMPLETED";
    final public static String ACTION_SEARCH_ERROR = "commitsupervisor.SEARCH_ERROR";


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @NonNull
    @Override
    public SearchService getSearchService() {

        return searchService;
    }
}
