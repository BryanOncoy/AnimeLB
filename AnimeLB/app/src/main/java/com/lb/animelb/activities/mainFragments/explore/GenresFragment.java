package com.lb.animelb.activities.mainFragments.explore;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lb.animelb.R;
import com.lb.animelb.adapters.GenreAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenresFragment extends Fragment {
    private RecyclerView genresRecyclerView;
    private GenreAdapter genreAdapter = null;
    private ArrayList<String> mGenres = new ArrayList<>();
    private ArrayList<String> mGenreKeys = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenresFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenerosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenresFragment newInstance(String param1, String param2) {
        GenresFragment fragment = new GenresFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        // Hooks
        genresRecyclerView = view.findViewById(R.id.genresRecyclerView);

        if (genreAdapter == null) {
            String sCategoryKey = "categoryValue", sCategory = "category", categoryKey, categoryName;
            for (int i = 0; i <= 18; i++) {
                categoryName = getResources().getString(getResources().getIdentifier(sCategory + i, "string", getContext().getPackageName()));
                mGenres.add(categoryName);

                categoryKey = getResources().getString(getResources().getIdentifier(sCategoryKey + i, "string", getContext().getPackageName()));
                mGenreKeys.add(categoryKey);
            }
            genreAdapter = new GenreAdapter(getContext(), mGenres, mGenreKeys);
        }

        genresRecyclerView.setAdapter(genreAdapter);
        genresRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));

        return view;
    }
}