package com.lb.animelb.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lb.animelb.activities.mainFragments.explore.GenresActivity;
import com.lb.animelb.R;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> mGenres;
    private ArrayList<String> mGenreKeys;

    public GenreAdapter(Context mContext, ArrayList<String> mGenres, ArrayList<String> mGenreKeys) {
        this.mContext = mContext;
        this.mGenres = mGenres;
        this.mGenreKeys = mGenreKeys;
    }

    @NonNull
    @Override
    public GenreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.my_genre, parent, false);
        return new GenreAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.category.setText(mGenres.get(position));

        holder.genreCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GenresActivity.class);
                intent.putExtra("genre", mGenreKeys.get(position));
                intent.putExtra("genreName", mGenres.get(position));
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mGenres.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView category;
        private CardView genreCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
            genreCardView = itemView.findViewById(R.id.genreCardView);

        }
    }
}
