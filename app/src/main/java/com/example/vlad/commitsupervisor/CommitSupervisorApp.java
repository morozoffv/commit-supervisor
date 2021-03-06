package com.example.vlad.commitsupervisor;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.layers.ApiEventsImpl;
import com.example.vlad.commitsupervisor.layers.ApiRepositoriesImpl;
import com.example.vlad.commitsupervisor.layers.ApiUsersImpl;
import com.example.vlad.commitsupervisor.layers.ApplicationCore;
import com.example.vlad.commitsupervisor.layers.Network;
import com.example.vlad.commitsupervisor.layers.NetworkImpl;
import com.example.vlad.commitsupervisor.layers.SearchService;
import com.example.vlad.commitsupervisor.layers.SearchServiceImpl;
import com.example.vlad.commitsupervisor.layers.StorageService;
import com.example.vlad.commitsupervisor.layers.StorageServiceImpl;

import java.util.Locale;

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

    private ApiEventsImpl apiEvents;
    private ApiRepositoriesImpl apiRepositories;
    private ApiUsersImpl apiUsers;

    private SearchService searchService;
    private StorageService storageService;


    final public static String ACTION_SEARCH_COMPLETED = "commitsupervisor.SEARCH_COMPLETED";
    final public static String ACTION_SEARCH_ERROR = "commitsupervisor.SEARCH_ERROR";
    final public static String ACTION_USERS_RECEIVED = "commitsupervisor.USERS_RECEIVED";
    final public static String ACTION_BLANK_SEARCH = "commitsupervisor.ACTION_BLANK_SEARCH";


    @Override
    public void onCreate() {
        super.onCreate();
        Network network = new NetworkImpl(getAssets());
        apiEvents = new ApiEventsImpl(network);
        apiRepositories = new ApiRepositoriesImpl(network);
        apiUsers = new ApiUsersImpl(network);
        storageService = new StorageServiceImpl(getApplicationContext());
        searchService = new SearchServiceImpl(apiEvents, apiRepositories, apiUsers, broadcastSender, storageService);

//        Configuration configuration = new Configuration(Resources.getSystem().getConfiguration());
//        configuration.setLocale(Locale.US);
//        Resources.getSystem().updateConfiguration(configuration, null);
    }

    @NonNull
    @Override
    public SearchService getSearchService() {
        return searchService;
    }

    @NonNull
    @Override
    public StorageService getStorageService() {
        return storageService;
    }
}
