package com.example.yumyumtree.ui.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yumyumtree.R;
import com.example.yumyumtree.data.api.Api;
import com.example.yumyumtree.data.api.RetrofitClient;
import com.example.yumyumtree.ui.favourites.FavouritesFragment;
import com.example.yumyumtree.ui.login.LoginFragment;
import com.example.yumyumtree.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    public final static String POSITIVE_BUTTON_TEXT = "Exit";
    public final static String NEGATIVE_BUTTON_TEXT = "No";
    public final static String ALERT_MESSAGE = "Are you sure you want to exit?";
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
    }

    private void getRestaurants() {
        restaurantList = new ArrayList<>();
        Call<RestaurantResponse> call = RetrofitClient.getInstance().getMyApi().getRestaurants();
        call.enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(@Nullable Call<RestaurantResponse> call, @Nullable Response<RestaurantResponse> response) {
                if (response != null && response.isSuccessful()) {
                    RestaurantResponse restaurantResponse = response.body();
                    List<Restaurant> restList = restaurantResponse.getRestaurants();

                    for (int i = 0; i < restList.size(); i++) {
                        String name = restList.get(i).getName();
                        int id = restList.get(i).getId();
                        String city = restList.get(i).getCity();
                        String address = restList.get(i).getAddress();
                        String area = restList.get(i).getArea();
                        String phone = restList.get(i).getPhone();
                        String image_url = restList.get(i).getImage_url();

                        restaurantList.add(new Restaurant(id, name, address, city, area, phone, image_url));
                    }
                    createUIElements();
                }
            }

            @Override
            public void onFailure(@Nullable Call<RestaurantResponse> call, @NonNull Throwable t) {
                createUIElements();
                searchView.clearFocus();
                searchView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No data to load!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public boolean onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(ALERT_MESSAGE);
            builder.setCancelable(true);
            builder.setPositiveButton(POSITIVE_BUTTON_TEXT, (dialog, which) -> System.exit(0));
            builder.setNegativeButton(NEGATIVE_BUTTON_TEXT, (dialog, which) -> dialog.cancel());
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