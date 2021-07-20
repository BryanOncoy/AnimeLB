package com.lb.animelb.activities.mainFragments.friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lb.animelb.AfterComplete;
import com.lb.animelb.R;
import com.lb.animelb.adapters.AnimeInfoAdapter;
import com.lb.animelb.clases.AnimeInfo;
import com.lb.animelb.activities.mainFragments.FriendsFragment;
import com.lb.animelb.dbManagement.Firebase;
import com.lb.animelb.dbManagement.UserFB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.lb.animelb.clases.User.currentUser;

public class UserDetailActivity extends AppCompatActivity {
    public static final String USER_PHOTO = "com.lb.animelb.USER_PHOTO";
    public static final String USER_NAME = "com.lb.animelb.USER_NAME";
    public static final String USER_POSITION = "com.lb.animelb.USER_POSITION";
    public static final String USER_ITEM_COUNT = "com.lb.animelb.USER_ITEM_COUNT";

    private String sName;
    private int sPosition;

    private ImageView photo;
    private TextView name;
    private Button statusBt;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView message;

    private AnimeInfoAdapter animeInfoAdapter;
    private ArrayList<AnimeInfo> animeInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        animeInfoList = new ArrayList<>();


        // Hooks
        recyclerView = findViewById(R.id.recyclerViewUserDetail);
        name = findViewById(R.id.udName);
        photo = findViewById(R.id.udPhoto);
        statusBt = findViewById(R.id.statusBt);
        imageView = findViewById(R.id.angry);
        message = findViewById(R.id.userDetailMs);

        sName = getIntent().getStringExtra(USER_NAME);
        sPosition = getIntent().getIntExtra(USER_POSITION, 0);

        name.setText(sName);


        Uri photoUri = Uri.parse(getIntent().getStringExtra(USER_PHOTO));
        Picasso.get().load(photoUri).into(photo);
        statusBt.setBackgroundColor(this.getResources().getColor(R.color.orange));

        statusBt.setOnClickListener(v -> {
            if (statusBt.getText().equals("Siguiendo")) {
                unfollow();
                statusBt.setText("Seguir");
                statusBt.setBackgroundColor(this.getResources().getColor(R.color.transparent));
            } else if (statusBt.getText().equals("Seguir")) {
                follow();
                statusBt.setText("Siguiendo");
                statusBt.setBackgroundColor(this.getResources().getColor(R.color.orange));
            }
        });

        showAnimes();


    }

    private void follow() {
        UserFB.addUser(sName, this, new AfterComplete() {
            @Override
            public void doAfterSuccess() {
                FriendsFragment.profileAdapter.notifyItemInserted(currentUser.friends.size());
            }
        });
    }

    private void unfollow() {
        int oldSize = currentUser.friends.size();
        sPosition = currentUser.friends.indexOf(sName);
        UserFB.unfollow(sName, new AfterComplete() {
            @Override
            public void doAfterSuccess() {
                FriendsFragment.profileAdapter.notifyItemRemoved(sPosition);
                FriendsFragment.profileAdapter.notifyItemRangeChanged(sPosition, oldSize - sPosition);
            }
        });
    }

    public void showEmptyRecyclerView(int itemCount) {
        if (itemCount == 0) {
            message.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private void showAnimes() {
        sName = getIntent().getStringExtra(USER_NAME);
        Firebase.usersFB.document(sName).collection("animes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            //TODO mostrar mensage de aviso
                        } else {
                            animeInfoList.clear();
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                AnimeInfo animeInfo = doc.toObject(AnimeInfo.class);
                                animeInfoList.add(animeInfo);
                            }

                            animeInfoAdapter = new AnimeInfoAdapter(UserDetailActivity.this, animeInfoList);
                            recyclerView.setAdapter(animeInfoAdapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(UserDetailActivity.this, 2, RecyclerView.VERTICAL, false));

                            int numItems = animeInfoAdapter.getItemCount();
                            showEmptyRecyclerView(numItems);
                        }
                    }
                });

    }
}