package com.lb.animelb.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lb.animelb.activities.animeActivities.DetailActivity;
import com.lb.animelb.R;
import com.lb.animelb.clases.AnimeInfo;
import com.lb.animelb.dbManagement.AnimeFB;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import static com.lb.animelb.clases.User.currentUser;

public class AnimeInfoAdapter extends RecyclerView.Adapter<AnimeInfoAdapter.MyViewHolder> {
    public static ArrayList<String> favAnimes = new ArrayList<>();

    private Context mContext;
    private ArrayList<AnimeInfo> mAnimeInfos;

    public AnimeInfoAdapter(Context mContext, ArrayList<AnimeInfo> mAnimeInfos) {
        this.mContext = mContext;
        this.mAnimeInfos = mAnimeInfos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.my_anime_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(mAnimeInfos.get(position).getImageUrl()).into(holder.imageView);
        holder.title.setText(mAnimeInfos.get(position).getTitle());
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(DetailActivity.TITLE, mAnimeInfos.get(position).getTitle());
            intent.putExtra(DetailActivity.IMAGE_URL, mAnimeInfos.get(position).getImageUrl());
            intent.putExtra(DetailActivity.ANIME_URL, mAnimeInfos.get(position).getAnimeUrl());
            mContext.startActivity(intent);
        });

        AnimeInfo currentAnime = mAnimeInfos.get(position);

        if (currentUser.favAnimes.contains(currentAnime)) {
            holder.icFav.setImageResource(R.drawable.ic_favorite_select);
            holder.icFav.setTag(R.drawable.ic_favorite_select);
        } else {
            holder.icFav.setImageResource(R.drawable.ic_favorite);
            holder.icFav.setTag(R.drawable.ic_favorite);
        }

        holder.icFav.setOnClickListener(v -> {
            if (holder.icFav.getTag().equals(R.drawable.ic_favorite)) {
                holder.icFav.setImageResource(R.drawable.ic_favorite_select);
                holder.icFav.setTag(R.drawable.ic_favorite_select);

                // Save the anime in the local list and in FireStore
                AnimeFB.addAnime(currentAnime);

                notifyItemChanged(position);

            } else {
                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.HORIZONTAL);


                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("¿Estás seguro de eliminarlo?");
                builder.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.icFav.setImageResource(R.drawable.ic_favorite);
                        holder.icFav.setTag(R.drawable.ic_favorite);

                        int itemcount = getItemCount();

                        // Delete the anime from the local list and FireStore
                        AnimeFB.removeAnime(currentAnime);

                        //notifyDataSetChanged();
                        //notifyItemRemoved(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, itemcount - position);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setView(layout);
                dialog.show();
                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.red));
                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.orange));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimeInfos.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;
        private ImageView icFav;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageInfo);
            title = itemView.findViewById(R.id.titleInfo);
            icFav = itemView.findViewById(R.id.favInfo);
            cardView = itemView.findViewById(R.id.cardViewInfo);
        }
    }
}