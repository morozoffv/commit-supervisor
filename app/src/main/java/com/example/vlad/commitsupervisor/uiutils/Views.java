package com.example.vlad.commitsupervisor.uiutils;

import android.view.View;

/**
 * Created by vlad on 24/10/2017.
 */

public class Views {

    public static void setVisible(View... views) {
        for (View v: views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public static void setInvisible(View... views) {
        for (View v: views) {
            v.setVisibility(View.INVISIBLE);
        }
    }
}
