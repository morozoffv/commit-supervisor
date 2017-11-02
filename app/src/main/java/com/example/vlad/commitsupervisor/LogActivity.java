package com.example.vlad.commitsupervisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.events.PushEvent;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private List<Event> events;
    private String[] stringArray;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = getCommitSupervisorApp().getSearchService().getSearchResult().getEvents();

        setContentView(R.layout.activity_log);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //separators
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new JSONAdapter(events);
        recyclerView.setAdapter(adapter);
    }

    private CommitSupervisorApp getCommitSupervisorApp() {
        return (CommitSupervisorApp) getApplication();
    }

    private void preFetchUserActivity() {
        //get user activity
    }



}
