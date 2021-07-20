package com.lb.animelb.activities.mainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lb.animelb.activities.mainFragments.explore.GenresFragment;
import com.lb.animelb.R;
import com.lb.animelb.activities.mainFragments.explore.ShowAllFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
    private ShowAllFragment showAllFragment;
    private GenresFragment genresFragment;
    private BottomNavigationView explorerNavView;
    private static int currentItem;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExplorarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        //hook
        genresFragment = new GenresFragment();
        showAllFragment = new ShowAllFragment();
        explorerNavView = view.findViewById(R.id.explorerNaviView);


        MenuItem selectedItem;
        if (currentItem==R.id.nav_generos) {
            selectedItem  = explorerNavView.getMenu().getItem(1);
            setFragment(genresFragment);
        }else {
            selectedItem = explorerNavView.getMenu().getItem(0);
            setFragment(showAllFragment);
        }
        selectedItem.setChecked(true);


        explorerNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_generos:
                        currentItem= R.id.nav_generos;
                        setFragment(genresFragment);
                        return true;
                    case R.id.nav_todos:
                        currentItem= R.id.nav_todos;
                        setFragment(showAllFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        return view;
    }


    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_explorer_frame, fragment);
        fragmentTransaction.commit();
    }
}