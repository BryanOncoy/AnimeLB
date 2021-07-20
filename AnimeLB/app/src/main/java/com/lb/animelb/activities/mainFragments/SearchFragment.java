package com.lb.animelb.activities.mainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lb.animelb.R;
import com.lb.animelb.adapters.AnimeInfoAdapter;
import com.lb.animelb.clases.AnimeInfo;

import com.lb.animelb.webScraping.AnimeScraper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private int pageNumber = 1;
    private ArrayList<AnimeInfo> animeInfoList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageView imageView;
    private ArrayList<AnimeInfo> additionalAnimeInfo = new ArrayList<>();
    private AnimeInfoAdapter animeInfoAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ANIME_QUERY = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mQuery;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param query  Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String query, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ANIME_QUERY, query);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString(ANIME_QUERY);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        inflater.inflate(R.layout.fragment_search, container, false);

        // Hooks
        recyclerView = view.findViewById(R.id.recyclerViewSearch);
        imageView = view.findViewById(R.id.imageView2);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    showMoreAnimes();
                }
            }
        });

        if (mQuery == null || mQuery.equals("")) {
            animeInfoList.clear();
            showEmptyRecyclerView(0);
        } else {
            showAnimes();
            showEmptyRecyclerView(1);
        }


        return view;
    }
    public void showEmptyRecyclerView(int itemCount){
        if (itemCount == 0) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private void showAnimes() {
        loadAnimesInfo();
        animeInfoAdapter = new AnimeInfoAdapter(getContext(), animeInfoList);
        recyclerView.setAdapter(animeInfoAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false));
    }

    private void showMoreAnimes() {
        int positionStart = animeInfoList.size();
        loadAnimesInfo();
        int itemCount = animeInfoList.size();
        animeInfoAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    private void loadAnimesInfo() {
        additionalAnimeInfo = AnimeScraper.getAnimesSearch(pageNumber++, mQuery);
        animeInfoList.addAll(additionalAnimeInfo);
    }


}