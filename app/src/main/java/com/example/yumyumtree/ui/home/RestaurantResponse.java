package com.example.yumyumtree.ui.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantResponse {

    @SerializedName("restaurants")
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
