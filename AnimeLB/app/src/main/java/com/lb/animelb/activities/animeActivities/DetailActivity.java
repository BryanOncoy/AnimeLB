package com.lb.animelb.activities.animeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lb.animelb.R;
import com.lb.animelb.adapters.EpisodeAdapter;
import com.lb.animelb.webScraping.AnimeScraper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    public static final String TITLE = "com.lb.animelb.TITLE";
    public static final String IMAGE_URL = "com.lb.animelb.IMAGE_URL";
    public static final String ANIME_URL = "com.lb.animelb.ANIME_URL";

    private RecyclerView episodeRecyclerView;
    private TextView animeTitle;
    private TextView description;
    private ImageView animeImage;
    private TextView contDescription;

    private String mAnimeUrl;
    private String mTitle;
    private String mImage;
    private String mDescrip;
    private ArrayList<String> mEpisodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Hooks
        episodeRecyclerView = findViewById(R.id.recyclerViewEpisodes);
        animeTitle = findViewById(R.id.animeTitle);
        animeImage = findViewById(R.id.animeImage);
        description = findViewById(R.id.animeDescription);
        contDescription = findViewById(R.id.textView7);

        mAnimeUrl = getIntent().getStringExtra(ANIME_URL);

        mTitle = getIntent().getStringExtra(TITLE);
        mImage = getIntent().getStringExtra(IMAGE_URL);
        mDescrip = AnimeScraper.getEpisodeDescription(mAnimeUrl);
        mEpisodes = AnimeScraper.getEpisodesUrl(mAnimeUrl);

        contDescription.setOnClickListener(v -> {
            Intent intent = new Intent(this, DesciptionActivity.class);
            intent.putExtra(DesciptionActivity.DESCRIPTION, mDescrip);
            startActivity(intent);
        });

        animeTitle.setText(mTitle);
        description.setText(AnimeScraper.getEpisodeDescription(mAnimeUrl));
        Picasso.get().load(mImage).into(animeImage);

        EpisodeAdapter episodeAdapter = new EpisodeAdapter(this, mEpisodes);
        episodeRecyclerView.setAdapter(episodeAdapter);
        episodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}