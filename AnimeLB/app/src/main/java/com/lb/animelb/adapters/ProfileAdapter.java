package com.lb.animelb.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lb.animelb.R;
import com.lb.animelb.activities.mainFragments.friends.UserDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.lb.animelb.clases.User.currentUser;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> mFriends;

    public ProfileAdapter(Context mContext, ArrayList<String> mFriends) {
        this.mContext = mContext;
        this.mFriends = mFriends;
    }

    @NonNull
    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.my_profile, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.MyViewHolder holder, int position) {
        Uri uri = currentUser.pictures.get(mFriends.get(position));
        Picasso.get().load(uri).into(holder.photo);

        holder.name.setText(mFriends.get(position));

        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UserDetailActivity.class);
            intent.putExtra(UserDetailActivity.USER_NAME, mFriends.get(position));
            intent.putExtra(UserDetailActivity.USER_PHOTO, uri.toString());
            intent.putExtra(UserDetailActivity.USER_POSITION, position);
            intent.putExtra(UserDetailActivity.USER_ITEM_COUNT, getItemCount());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private TextView name;
        private ConstraintLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.uPhoto);
            name = itemView.findViewById(R.id.uName);
            layout = itemView.findViewById(R.id.uLayout);

        }
    }
}
