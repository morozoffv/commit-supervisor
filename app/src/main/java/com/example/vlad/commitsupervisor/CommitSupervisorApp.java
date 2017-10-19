package com.example.vlad.commitsupervisor;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.layers.ApplicationCore;
import com.example.vlad.commitsupervisor.layers.SearchService;

/**
 * Created by vlad on 03/10/2017.
 */

public class CommitSupervisorApp extends Application implements ApplicationCore {

    @Nullable private SearchResult result;
    SearchService searchService;


    final public static String ACTION_SEARCH_COMPLETED = "commitsupervisor.SEARCH_COMPLETED";
    final public static String ACTION_SEARCH_ERROR = "commitsupervisor.SEARCH_ERROR";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void search(CharSequence username) {

        JSONAsyncTask asyncTask = new JSONAsyncTask() {
            @Override
            protected void onPostExecute(SearchResult searchResult) {
                result = searchResult;

                Intent broadcastIntent;
                if(result.isSuccessful()) {
                    broadcastIntent = new Intent(ACTION_SEARCH_COMPLETED);
                    broadcastIntent.putExtra("eventsCount", result.getEvents().size());
                }
                else {
                    broadcastIntent = new Intent(ACTION_SEARCH_ERROR);

                }
                sendBroadcast(broadcastIntent);

            }
        };
        asyncTask.execute(username.toString());


    }

    @Nullable
    public SearchResult getResult() {
        return result;
    }


    @NonNull
    @Override
    public SearchService getSearchService() {
        return null;
    }
}
