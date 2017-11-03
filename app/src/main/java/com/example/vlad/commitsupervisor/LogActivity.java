package com.example.vlad.commitsupervisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.events.PushEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogActivity extends AppCompatActivity {

    private List<Event> events;
    private String[] stringArray;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    final private SearchBroadcastReceiver searchBroadcastReceiver = new SearchBroadcastReceiver() {
        @Override
        public void onCompleted(Bundle bundle) {
            Toast.makeText(LogActivity.this, "Success, loaded " + bundle.get("eventsCount") + " events", Toast.LENGTH_SHORT).show();
            events = getCommitSupervisorApp().getSearchService().getSearchResult().getEvents();
            adapter = new JSONAdapter(events);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onError() {
            Toast.makeText(LogActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchBroadcastReceiver.onRegister(this);

        preFetchUserActivity();

        setContentView(R.layout.activity_log);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //separators
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


    }

    private CommitSupervisorApp getCommitSupervisorApp() {
        return (CommitSupervisorApp) getApplication();
    }

    private void preFetchUserActivity() {
        //get user activity

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);
        Date date = new Date();
        getCommitSupervisorApp().getSearchService().fetchUserActivity(getIntent().getStringExtra("username"), date); //TODO: redo username type and date filtration

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchBroadcastReceiver.onUnregister(this);

    }
}
