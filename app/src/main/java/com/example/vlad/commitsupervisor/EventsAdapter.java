package com.example.vlad.commitsupervisor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import kotlin.NotImplementedError;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
import static android.text.format.DateUtils.HOUR_IN_MILLIS;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;


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
            icon = (ImageView) v.findViewById(R.id.icon);

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
        holder.icon.setImageDrawable(context.getDrawable(R.drawable.icon_commit));

        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        long milliseconds = 0;
        try {
            milliseconds = dateParser.parse(pushEvent.getCreatedAt()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //long posix = milliseconds / 1000;
        CharSequence temp = DateUtils.getRelativeTimeSpanString(milliseconds,System.currentTimeMillis(), DAY_IN_MILLIS, FORMAT_ABBREV_RELATIVE);
        holder.time.setText(temp);
    }

    private void bindEvent(ViewHolder holder, CommentEvent commentEvent) {
        holder.header.setText(commentEvent.getRepoName());
        holder.description.setText(commentEvent.getComment());
        holder.subDescription.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        switch (commentEvent.getType()) {
            case CommitCommentEvent:
                holder.subDescription.setText(R.string.commit_comment);
                break;
            case IssueCommentEvent:
                holder.subDescription.setText(R.string.issue_comment);
                break;
            case PullRequestReviewCommentEvent:
                holder.subDescription.setText(R.string.pull_request_comment);
                break;
        }
        holder.icon.setImageDrawable(context.getDrawable(R.drawable.ic_icon_comment));

        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        long milliseconds = 0;
        try {
            milliseconds = dateParser.parse(commentEvent.getCreatedAt()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //long posix = milliseconds / 1000;
        CharSequence temp = DateUtils.getRelativeTimeSpanString(milliseconds,System.currentTimeMillis(), DAY_IN_MILLIS, FORMAT_ABBREV_RELATIVE);

        holder.time.setText(temp);


    }





}
