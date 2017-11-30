package com.example.vlad.commitsupervisor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.commitsupervisor.events.CommentEvent;
import com.example.vlad.commitsupervisor.events.Event;
import com.example.vlad.commitsupervisor.events.PushEvent;
import com.example.vlad.commitsupervisor.parsers.EventTypes;

import java.io.Serializable;
import java.util.List;

import kotlin.NotImplementedError;


/**
 * Created by vlad on 05/10/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> implements Serializable {

    private List<Event> eventsList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView header;
        private TextView description;
        private TextView subDescription;
        private TextView time;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v); //itemView = v;
            header = (TextView) v.findViewById(R.id.header_text);
            description = (TextView) v.findViewById(R.id.description_text);
            subDescription = (TextView) v.findViewById(R.id.subdescription_text);
            time = (TextView) v.findViewById(R.id.time_text);

        }
    }

    public EventsAdapter(List<Event> dataSet, Context context) {
        eventsList = dataSet;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { //if RecyclerView freezes during scrolling, that is because this method works slow
        Event event = eventsList.get(position);
        if (event instanceof PushEvent) {
            bindEvent(holder, (PushEvent) event);
            return;
        }
        if (event instanceof CommentEvent) {
            bindEvent(holder, (CommentEvent) event);
            return;
        }
        throw new NotImplementedError();

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }


    private void bindEvent(ViewHolder holder, PushEvent pushEvent) {
        holder.header.setText(pushEvent.getRepoName());
        holder.description.setText("Pushed " + pushEvent.getCommitNumber() + " commit(s)");
        Drawable branchIcon = context.getDrawable(R.drawable.icon_branch);
        holder.subDescription.setCompoundDrawablesWithIntrinsicBounds(branchIcon, null, null, null);
        holder.subDescription.setText(pushEvent.getBranch());
        //holder.icon.setImageDrawable(context.getDrawable(R.drawable.icon_commit));
    }

    private void bindEvent(ViewHolder holder, CommentEvent commentEvent) {
        holder.header.setText(commentEvent.getRepoName());
        holder.description.setText(commentEvent.getComment());
        //holder.subDescription.setText(commentEvent.);

    }





}
