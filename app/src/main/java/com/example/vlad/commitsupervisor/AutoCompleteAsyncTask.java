package com.example.vlad.commitsupervisor;

import android.os.AsyncTask;

/**
 * Created by vlad on 30/10/2017.
 */

abstract public class AutoCompleteAsyncTask extends AsyncTask<String, Void, Void> {

    @Override
    abstract protected void onPostExecute(Void aVoid);

    @Override
    abstract protected Void doInBackground(String... params);

}
