package com.example.vlad.commitsupervisor;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 09/11/2017.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>{

    private List<User> storedUsers = new ArrayList<>();
    OnItemClickListener itemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView textView;


        public ViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            textView = (TextView) linearLayout.findViewById(R.id.search_history_text);
        }
    }
    public SearchHistoryAdapter(List<User> storedUsers) {   this.storedUsers = storedUsers;  }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v =  (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textView.setText(storedUsers.get(position).getLogin());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });

    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return storedUsers.size();
    }










}
