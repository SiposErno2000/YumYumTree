package com.example.yumyumtree.data.api;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.yumyumtree.ui.home.Restaurant;
import com.example.yumyumtree.ui.home.RestaurantResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yumyumtree.data.api.UserProfileHandler.CHILD;
import static com.example.yumyumtree.ui.login.LoginFragment.CURRENT_NAME;

public class RestaurantsCache {

    private final static String TAG = RestaurantsCache.class.getSimpleName();
    private static RestaurantsCache restaurantsCache;
    private List<Restaurant> restaurantList;

    private RestaurantsCache() {}

    public synchronized static RestaurantsCache getInstance() {
        if (restaurantsCache == null) {
            restaurantsCache = new RestaurantsCache();
        }
        return restaurantsCache;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public void getRestaurants() {
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
                        String reserve_url = restList.get(i).getReserve_url();

                        restaurantList.add(new Restaurant(id, name, address, city, area, phone, image_url, reserve_url));
                    }
                    Constants constants = Constants.getInstance();
                    constants.setDataLoading(true);
                    setRestaurantList(restaurantList);
                } else {
                    Log.d(TAG,"response is not successful for getRestaurants");
                }
            }

            @Override
            public void onFailure(@Nullable Call<RestaurantResponse> call, @NonNull Throwable t) {
                Log.d(TAG,"No data to load!");
            }
        });
    }

    public Restaurant getRestaurant(String id) {
        for (Restaurant restaurant : restaurantList) {
            if (String.valueOf(restaurant.getId()).equals(id)) return restaurant;
        }
        return null;
    }
}
