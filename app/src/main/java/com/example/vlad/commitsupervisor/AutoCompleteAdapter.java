package com.example.vlad.commitsupervisor;

import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by vlad on 27/10/2017.
 */

public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder> {

    private List<User> users;
    private OnItemClickListener itemClickListener;
    Drawable avatarDrawable;


    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public FrameLayout avatar;
        public ImageView avatarImage;
        //public ConstraintLayout itemLayout;

        public int position;

        public ViewHolder(ConstraintLayout v) {
            super(v); //itemView = v;
            textView = (TextView) v.findViewById(R.id.autocompletion_text);
            avatar = (FrameLayout) v.findViewById(R.id.autocompletion_avatar);
            avatarImage = (ImageView) v.findViewById(R.id.avatar);
        }
    }


    public AutoCompleteAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_autocompletion, parent, false);
        return new ViewHolder((ConstraintLayout) v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(users.get(position).getLogin());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                avatarDrawable = loadImageFromWebOperations(users.get(position).getAvatarUrl());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                holder.avatarImage.setImageDrawable(avatarDrawable);
            }
        }.execute();
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

    private Drawable loadImageFromWebOperations(String url) {
        Drawable d = null;
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            d = Drawable.createFromStream(is, null);
            return d;
        } catch (Exception e) {
            return d;
        }


    }
}
