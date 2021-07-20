package com.lb.animelb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lb.animelb.R;
import com.lb.animelb.activities.animeActivities.WatchEpisodeActivity;
import com.lb.animelb.webScraping.AnimeScraper;

import java.util.ArrayList;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> mEpisodes;

    public EpisodeAdapter(Context mContext, ArrayList<String> mEpisodes) {
        this.mContext = mContext;
        this.mEpisodes = mEpisodes;
    }


    @NonNull
    @Override
    public EpisodeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.my_episode, parent, false);
        return new EpisodeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String ep = "Episodio " + (position + 1);
        holder.episodeNum.setText(ep);
        holder.episodeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPlayerUrl = AnimeScraper.getVideoPlayerUrl(mEpisodes.get(position));
                Intent intent = new Intent(mContext, WatchEpisodeActivity.class);
                intent.putExtra("url", videoPlayerUrl);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mEpisodes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView episodeNum;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeNum = itemView.findViewById(R.id.episodeNum);
        }
    }
}