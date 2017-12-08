package com.example.vlad.commitsupervisor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.NotImplementedError;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
import static android.text.format.DateUtils.HOUR_IN_MILLIS;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;


/**
 * Created by vlad on 05/10/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable {

    private List<Event> eventsList;
    private Context context;

    private OnItemClickListener itemClickListener;

    private boolean isTodayEventExistsCreate = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView header;
        private TextView description;
        private TextView subDescription;
        private TextView time;
        private ImageView icon;

        public ViewHolder(View v) {
            super(v); //itemView = v;
            cardView = (CardView) v.findViewById(R.id.event_item_card_view);
            header = (TextView) v.findViewById(R.id.header_text);
            description = (TextView) v.findViewById(R.id.description_text);
            subDescription = (TextView) v.findViewById(R.id.subdescription_text);
            time = (TextView) v.findViewById(R.id.time_text);
            icon = (ImageView) v.findViewById(R.id.icon);

        }
    }

    public static class SecondaryViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public SecondaryViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.today_events_check);
        }
    }

    public EventsAdapter(List<Event> dataSet, Context context) {
        eventsList = dataSet;
        List<Event> todayEvents = getTodayCheckedEvents(eventsList);
        if (!todayEvents.isEmpty()) {
            eventsList = todayEvents;
        }
        this.context = context;

    }

    private List<Event> getTodayCheckedEvents(List<Event> events) {     //TODO: right now i run through whole list, make a smart check
        List<Event> todayEvents = new ArrayList<>();
        for (Event event: events)
        {
            SimpleDateFormat created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat dayWithoutTime = new SimpleDateFormat("yyyy-MM-dd");
            Date createdDate = null;
            Date dayWithoutTimeDate = null;

            try {
                createdDate = created.parse(event.getCreatedAt());
                dayWithoutTimeDate = dayWithoutTime.parse(dayWithoutTime.format(new Date()));

            } catch (ParseException e) {
                e.printStackTrace();
                //add date stubs
            }

            if (dayWithoutTimeDate.after(createdDate)) {
                todayEvents.add(event);
            }
        }
        if (!todayEvents.isEmpty()) {
            isTodayEventExists = true;
        }
        return todayEvents;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isTodayEventExists) {
            isTodayEventExists = false;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_events_today_check, parent, false);
            return new SecondaryViewHolder(v);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        Event event = eventsList.get(position);

        ((ViewHolder) holder).cardView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, ((ViewHolder) holder).getAdapterPosition());
                }
            }
        });

        if (event instanceof PushEvent) {
            bindEvent(((ViewHolder) holder), (PushEvent) event);
            return;
        }
        if (event instanceof CommentEvent) {
            bindEvent(((ViewHolder) holder), (CommentEvent) event);
            return;
        }
        throw new NotImplementedError();

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }


    private void bindEvent(final ViewHolder holder, PushEvent pushEvent) {
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

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }





}
