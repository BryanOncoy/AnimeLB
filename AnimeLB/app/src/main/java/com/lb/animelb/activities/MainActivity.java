package com.lb.animelb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.lb.animelb.clases.User;
import com.lb.animelb.R;
import com.lb.animelb.activities.mainFragments.ExploreFragment;
import com.lb.animelb.activities.mainFragments.FavoritesFragment;
import com.lb.animelb.activities.mainFragments.FriendsFragment;
import com.lb.animelb.activities.mainFragments.ProfileFragment;
import com.lb.animelb.activities.mainFragments.SearchFragment;


public class MainActivity extends AppCompatActivity {
    public static User user;

    private Toolbar toolbar;
    private BottomNavigationView navigationView;
    private SearchView searchView;

    private FavoritesFragment favoritesFragment;
    private ExploreFragment exploreFragment;
    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;
    private FriendsFragment friendFragment;


    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hooks
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.main_nav);

        favoritesFragment = new FavoritesFragment();
        exploreFragment = new ExploreFragment();
        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();
        friendFragment = new FriendsFragment();


        // ExplorerFragment by default
        navigationView.setSelectedItemId(R.id.nav_explorar);
        setFragment(exploreFragment);
        toolbar.setTitle("Explorar");
        setSupportActionBar(toolbar);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_fav:
                        toolbar.setTitle("Favoritos");
                        toolbar.getMenu().getItem(0).setVisible(false);
                        setFragment(favoritesFragment);
                        return true;
                    case R.id.nav_explorar:
                        toolbar.setTitle("Explorar");
                        toolbar.getMenu().getItem(0).setVisible(false);
                        setFragment(exploreFragment);
                        return true;
                    case R.id.nav_search:
                        toolbar.setTitle("Buscar");
                        toolbar.getMenu().getItem(0).setVisible(true);
                        setFragment(searchFragment);
                        return true;
                    case R.id.nav_amigos:
                        toolbar.setTitle("Amigos");
                        toolbar.getMenu().getItem(0).setVisible(false);
                        setFragment(friendFragment);
                        return true;
                    case R.id.nav_perfil:
                        toolbar.setTitle("Perfil");
                        toolbar.getMenu().getItem(0).setVisible(false);
                        setFragment(profileFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Handler handler = new Handler();
                final String[] mQuery = new String[1];
                navigationView.setVisibility(View.GONE);
                setFragment(searchFragment);

                searchView = (SearchView) item.getActionView();

                searchView.setQueryHint("Buscar...");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchFragment = SearchFragment.newInstance(query, "0");
                        setFragment(searchFragment);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mQuery[0] = newText;
                        handler.removeCallbacksAndMessages(null);
                        int delayMillis=0;
                        if (!newText.equals("")){
                           delayMillis=500;
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                searchFragment = SearchFragment.newInstance(mQuery[0], "0");
                                setFragment(searchFragment);
                            }
                        }, delayMillis);
                        return true;
                    }
                });

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                navigationView.setVisibility(View.VISIBLE);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}