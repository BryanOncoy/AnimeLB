package com.lb.animelb.activities.mainFragments.explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.lb.animelb.R;
import com.lb.animelb.adapters.AnimeInfoAdapter;
import com.lb.animelb.clases.AnimeInfo;
import com.lb.animelb.webScraping.AnimeScraper;

import java.util.ArrayList;

public class GenresActivity extends AppCompatActivity {
    String genre = "";
    private RecyclerView genresRecyclerView;
    private int pageNumber = 1;
    ArrayList<AnimeInfo> additionalAnimeInfo = new ArrayList<>();
    AnimeInfoAdapter animeInfoAdapter;
    private ArrayList<AnimeInfo> animeInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        // Hooks
        genresRecyclerView = findViewById(R.id.genresRecyclerView);
        Toolbar toolbar = findViewById(R.id.genresToolbar);
        toolbar.setTitle(getIntent().getStringExtra("genreName"));
        setSupportActionBar(toolbar);

        genre = getIntent().getStringExtra("genre");
        genresRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    showMoreAnimes();
                }
            }
        });

        showAnimes();
    }

    private void showAnimes() {
        loadAnimesInfo();
        animeInfoAdapter = new AnimeInfoAdapter(this, animeInfoList);
        genresRecyclerView.setAdapter(animeInfoAdapter);
        genresRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
    }

    private void showMoreAnimes() {
        int positionStart = animeInfoList.size();
        loadAnimesInfo();
        int itemCount = animeInfoList.size();
        animeInfoAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    private void loadAnimesInfo() {
        additionalAnimeInfo = AnimeScraper.getAnimesByGenre(genre, pageNumber++);
        animeInfoList.addAll(additionalAnimeInfo);
    }
}