package com.example.vlad.commitsupervisor;

import android.content.Intent;

/**
 * Created by vlad on 20/10/2017.
 */

/*
* Allow me to use sendBroadcast from initial Application class
* */

public interface BroadcastSender {

    void sendBroadcast(Intent intent);
}
