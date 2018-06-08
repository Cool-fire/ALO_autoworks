package com.aloautoworks.alo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aloautoworks.alo.QuoteMainActivity;
import com.aloautoworks.alo.R;
import com.aloautoworks.alo.RegistrationActivity;
import com.aloautoworks.alo.models.mainlistfeed;

import java.util.List;

public class MainlistAdapter extends RecyclerView.Adapter<MainlistAdapter.ViewHolder> {
    private final List<mainlistfeed> FeedList;
    private Context context;

    @NonNull
    @Override
    public MainlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_row, parent, false);

        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainlistAdapter.ViewHolder holder, final int position) {
        mainlistfeed feeditem = FeedList.get(position);
        holder.title.setText(feeditem.getTitle());
        holder.subtitle.setText(feeditem.getSubtitle());
        int draw = feeditem.getDrawable();
        holder.drawable.setImageResource(draw);


    }

    public MainlistAdapter(List<mainlistfeed> FeedList) {
        this.FeedList = FeedList;
    }

    @Override
    public int getItemCount() {
        return FeedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView drawable;
        private final TextView title;
        private final TextView subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            drawable = (ImageView)itemView.findViewById(R.id.image);
            title = (TextView)itemView.findViewById(R.id.title);
            subtitle = (TextView)itemView.findViewById(R.id.subtitle);
        }
        
    }
}
