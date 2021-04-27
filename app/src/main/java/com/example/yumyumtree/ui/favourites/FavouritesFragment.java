package com.example.yumyumtree.ui.favourites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yumyumtree.R;
import com.example.yumyumtree.data.api.RestaurantsCache;
import com.example.yumyumtree.data.api.UserProfileHandler;
import com.example.yumyumtree.ui.home.HomeFragment;
import com.example.yumyumtree.ui.home.Restaurant;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.yumyumtree.data.api.UserProfileHandler.CHILD;
import static com.example.yumyumtree.ui.login.LoginFragment.CURRENT_NAME;
import static com.example.yumyumtree.ui.login.LoginFragment.USERS;

public class FavouritesFragment extends Fragment {

    private DatabaseReference rootRef;
    private View view;
    private List<Restaurant> favouriteList;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourites, container, false);
        getFavouriteRestaurants();
        return view;
    }

    private void createUIElements() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FavouriteAdapter favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
        recyclerView.setAdapter(favouriteAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Restaurant deleteRestaurant = favouriteList.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                String id = String.valueOf(deleteRestaurant.getId());
                favouriteList.remove(viewHolder.getAdapterPosition());
                favouriteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                rootRef = FirebaseDatabase.getInstance().getReference(USERS);
                rootRef.child(CURRENT_NAME).child(CHILD).child(id).removeValue();

                Snackbar.make(recyclerView, deleteRestaurant.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        favouriteList.add(position, deleteRestaurant);
                        favouriteAdapter.notifyItemInserted(position);
                        rootRef = FirebaseDatabase.getInstance().getReference(USERS);
                        rootRef.child(CURRENT_NAME).child(CHILD).child(id).setValue(id);
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void getFavouriteRestaurants() {
        RestaurantsCache restaurantsCache = RestaurantsCache.getInstance();
        UserProfileHandler userProfileHandler = UserProfileHandler.getInstance();
        List<String> favIdList = userProfileHandler.getFavouriteList();
        List<Restaurant> restaurantList;
        favouriteList = new ArrayList<>();

        if (restaurantsCache.getRestaurantList() != null) {
            restaurantList = restaurantsCache.getRestaurantList();
            for (Restaurant restaurant: restaurantList) {
                for (String str: favIdList) {
                    if (String.valueOf(restaurant.getId()).equals(str)) {
                        favouriteList.add(restaurant);
                    }
                }
            }
            createUIElements();
        } else {
            Toast.makeText(getActivity(), "No data to load!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.loginactivity, fragment).commit();
    }

    public boolean onBackPressed() {
        HomeFragment homeFragment = new HomeFragment();
        loadFragments(homeFragment);
        return true;
    }
}