package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by taraskreknin on 17/10/2017.
 */

public interface Network {

    @Nullable
    JSONObject getObjectFromUrl(@NonNull final URL url);

    @Nullable
    JSONArray getArrayFromUrl(@NonNull final URL url);

}
