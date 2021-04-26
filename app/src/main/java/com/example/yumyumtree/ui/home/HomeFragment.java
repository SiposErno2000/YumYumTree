package com.example.yumyumtree.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.yumyumtree.R;
import com.example.yumyumtree.data.api.RestaurantsCache;
import com.example.yumyumtree.data.api.UserProfileHandler;
import com.example.yumyumtree.ui.favourites.FavouritesFragment;
import com.example.yumyumtree.ui.login.LoginFragment;
import com.example.yumyumtree.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private List<Restaurant> restaurantList;
    private RestaurantAdapter adapter;
    private DrawerLayout drawerLayout;
    private SearchView searchView;
    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getRestaurants();
        return view;
    }

    private void createUIElements() {
        drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RestaurantAdapter(restaurantList, getContext());
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    return false;
                } else {
                    adapter.getFilter().filter(query);
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    return false;
                } else {
                    adapter.getFilter().filter(newText);
                    return true;
                }
            }
        });

        UserProfileHandler userProfileHandler = UserProfileHandler.getInstance();
        userProfileHandler.getUserData();
    }

    private void getRestaurants() {
        RestaurantsCache restaurantsCache = RestaurantsCache.getInstance();
        restaurantList = new ArrayList<>();
        if (restaurantsCache.getRestaurantList() != null) {
            restaurantList = restaurantsCache.getRestaurantList();
            createUIElements();
        } else {
            createUIElements();
            searchView.clearFocus();
            searchView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No data to load!", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.alert_message_home);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.positive_alert_button_home, (dialog, which) -> System.exit(0));
            builder.setNegativeButton(R.string.negative_alert_button_home, (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                LoginFragment loginFragment = new LoginFragment();
                loadFragments(loginFragment);
                break;
            case R.id.nav_favourites:
                FavouritesFragment favouritesFragment = new FavouritesFragment();
                loadFragments(favouritesFragment);
                break;
            case R.id.nav_profile:
                ProfileFragment profileFragment = new ProfileFragment();
                loadFragments(profileFragment);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.loginactivity, fragment).commit();
    }
}