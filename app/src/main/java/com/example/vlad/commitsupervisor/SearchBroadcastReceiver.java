package com.example.vlad.commitsupervisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Created by vlad on 05/10/2017.
 */

public abstract class SearchBroadcastReceiver extends BroadcastReceiver {


    public void onRegister(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommitSupervisorApp.ACTION_SEARCH_COMPLETED);
        intentFilter.addAction(CommitSupervisorApp.ACTION_SEARCH_ERROR);
        context.registerReceiver(this, intentFilter);
    }

    public void onUnregister(Context context) {
        context.unregisterReceiver(this);
    }

    public abstract void onCompleted(Bundle bundle);
    public abstract void onError();

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {

            case CommitSupervisorApp.ACTION_SEARCH_COMPLETED:
                onCompleted(intent.getExtras());
                break;

            case CommitSupervisorApp.ACTION_SEARCH_ERROR:
                onError();
                return;

            default:
        }
    }
}
