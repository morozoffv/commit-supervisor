package com.example.vlad.commitsupervisor.layers;

import android.support.annotation.NonNull;

import com.example.vlad.commitsupervisor.User;
import com.example.vlad.commitsupervisor.events.Event;

import java.util.List;

/**
 * Created by taraskreknin on 18/10/2017.
 */

interface ApiEvents extends Api {

    @NonNull
    List<Event> getUserEvents(@NonNull final User user);

}
