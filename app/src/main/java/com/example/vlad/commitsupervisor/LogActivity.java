package com.example.vlad.commitsupervisor;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LogActivity extends AppCompatActivity {

    private JSONArray events = null;
    private String[] stringArray;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = getCommitSupervisorApp().getResult().getEvents();

        stringArray = new String[events.length()];

        for (int i = 0; i < events.length(); i++) {
            try {
                stringArray[i] = events.getJSONObject(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setContentView(R.layout.activity_log);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //separators
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL); //need getOrientation()
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new JSONAdapter(stringArray);
        recyclerView.setAdapter(adapter);



//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
//        setListAdapter(arrayAdapter);
        //ListView listView = (ListView) findViewById(R.id.listView);

    }

    private CommitSupervisorApp getCommitSupervisorApp() {
        return (CommitSupervisorApp) getApplication();
    }



}
