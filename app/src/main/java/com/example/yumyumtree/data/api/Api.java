package com.example.yumyumtree.data.api;

import com.example.yumyumtree.ui.home.RestaurantResponse;


import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://ratpark-api.imok.space";

    @GET("/restaurants")
    Call<RestaurantResponse> getRestaurants();

}
