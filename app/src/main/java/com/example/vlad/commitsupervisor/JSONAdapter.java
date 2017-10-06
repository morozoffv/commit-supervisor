package com.example.vlad.commitsupervisor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by vlad on 05/10/2017.
 */

public class JSONAdapter extends RecyclerView.Adapter<JSONAdapter.ViewHolder> {

    private String[] stringArray;

    public JSONAdapter(String[] dataSet) {
        stringArray = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commit, parent, false);
        ViewHolder viewHolder = new ViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(stringArray[position]);
    }

    @Override
    public int getItemCount() {
        return stringArray.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }


}
