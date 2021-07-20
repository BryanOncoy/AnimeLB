package com.lb.animelb.activities.mainFragments.explore;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lb.animelb.R;
import com.lb.animelb.adapters.AnimeInfoAdapter;
import com.lb.animelb.clases.AnimeInfo;

import com.lb.animelb.webScraping.AnimeScraper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowAllFragment extends Fragment {
    private static int pageNumber = 1;
    private static ArrayList<AnimeInfo> animeInfoList = new ArrayList<>();

    private RecyclerView recyclerView;
    ArrayList<AnimeInfo> additionalAnimeInfo = new ArrayList<>();
    AnimeInfoAdapter animeInfoAdapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowAllFragment newInstance(String param1, String param2) {
        ShowAllFragment fragment = new ShowAllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_all, container, false);

        // Hooks
        recyclerView = view.findViewById(R.id.recyclerViewShowAll);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    showMoreAnimes();
                }
            }
        });

        showAnimes();

        return view;
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
        additionalAnimeInfo = AnimeScraper.getAllAnimes(pageNumber++);
        animeInfoList.addAll(additionalAnimeInfo);
    }
}