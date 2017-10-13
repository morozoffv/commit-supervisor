package com.example.vlad.commitsupervisor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by vlad on 05/10/2017.
 */

public class JSONAdapter extends RecyclerView.Adapter<JSONAdapter.ViewHolder> {

    private ArrayList<PushEvent> pushEventsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ViewHolder(TextView v) {
            super(v); //itemView = v;
            textView = v;
        }
    }

    public JSONAdapter(ArrayList<PushEvent> dataSet) {
        pushEventsList = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //later, try to understand ViewGroup purpose
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commit, parent, false);
        ViewHolder viewHolder = new ViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { //if RecyclerView freezes during scrolling, that is because this method works slow
        holder.textView.setText(pushEventsList.get(position).getRepoName()/*.substring(0, 200)*/);
    }

    @Override
    public int getItemCount() {
        return pushEventsList.size();
    }




}
