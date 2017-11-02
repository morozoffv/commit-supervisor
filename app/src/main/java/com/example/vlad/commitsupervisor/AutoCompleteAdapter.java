package com.example.vlad.commitsupervisor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by vlad on 27/10/2017.
 */

public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder> {

    private List<User> users;
    private OnItemClickListener itemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public int position;

        public ViewHolder(TextView v) {
            super(v); //itemView = v;
            textView = v;
        }
    }


    public AutoCompleteAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_autocompletion, parent, false);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(users.get(position).getLogin());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, holder.getAdapterPosition()); //TODO:?
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
