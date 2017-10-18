package com.example.vlad.commitsupervisor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.events.PushEvent;

import java.util.List;


/**
 * Created by vlad on 05/10/2017.
 */

public class JSONAdapter extends RecyclerView.Adapter<JSONAdapter.ViewHolder> {

    private List<Event> eventsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ViewHolder(TextView v) {
            super(v); //itemView = v;
            textView = v;
        }
    }

    public JSONAdapter(List<Event> dataSet) {
        eventsList = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //later, try to understand ViewGroup purpose
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commit, parent, false);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { //if RecyclerView freezes during scrolling, that is because this method works slow
        holder.textView.setText(eventsList.get(position).getType()/*.substring(0, 200)*/);
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }




}
