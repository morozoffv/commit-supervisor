package com.example.vlad.commitsupervisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.vlad.commitsupervisor.events.PushEvent;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private List<PushEvent> events;
    private String[] stringArray;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = getCommitSupervisorApp().getResult().getPushEventsList();

        //stringArray = events.toString();


        setContentView(R.layout.activity_log);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //separators
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL); //need getOrientation()
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new JSONAdapter(events);
        recyclerView.setAdapter(adapter);



//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
//        setListAdapter(arrayAdapter);
        //ListView listView = (ListView) findViewById(R.id.listView);

    }

    private CommitSupervisorApp getCommitSupervisorApp() {
        return (CommitSupervisorApp) getApplication();
    }

//    //should i pass to this activity just a string array and make cast to string array from jsonarray in other place?
//    private String[] JSONArrayToStringArray(JSONArray jsonArray) {  //how to change var name in one single method?
//        String[] stringArray = new String[jsonArray.length()];
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                stringArray[i] = jsonArray.getJSONObject(i).toString();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return stringArray;
//    }



}
