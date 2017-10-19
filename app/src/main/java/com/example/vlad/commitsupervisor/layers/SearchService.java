package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.Nullable;

import com.example.vlad.commitsupervisor.SearchResult;

import java.util.Date;

/**
 * Created by taraskreknin on 18/10/2017.
 */

/**
 * Executes all requests in background and fires results to UI via broadcast events
 */
public interface SearchService {

    void fetchUserActivity(final String username, final Date date);

     @Nullable SearchResult getSearchResult();

    void loadAutocompletionsForUsername(final String username);

}
