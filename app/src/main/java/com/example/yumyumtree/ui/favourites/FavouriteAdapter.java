package com.example.yumyumtree.ui.favourites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yumyumtree.R;
import com.example.yumyumtree.ui.detail.DetailFragment;
import com.example.yumyumtree.ui.home.Restaurant;

import java.util.List;

import static com.example.yumyumtree.ui.detail.DetailFragment.EXTRA_ID;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private final Context context;
    private final List<Restaurant> restaurantList;

    public FavouriteAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_layout_favourite, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        final Restaurant restaurant = restaurantList.get(position);
        holder.restName.setText(restaurant.getName());
        holder.restId.setText(String.valueOf(restaurant.getId()));
        holder.restArea.setText(restaurant.getArea());
        holder.restCity.setText(restaurant.getCity());

        Glide.with(context).load(restaurant.getImage_url()).into(holder.restImage);

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ID, holder.restId.getText().toString());
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.loginactivity, detailFragment).commit();
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        TextView restName, restId, restArea, restCity;
        ImageView restImage;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);

            restImage = itemView.findViewById(R.id.restaurantImageFav);
            restName = itemView.findViewById(R.id.restaurantNameFav);
            restId = itemView.findViewById(R.id.restaurantIdFav);
            restArea = itemView.findViewById(R.id.restaurantAreaFav);
            restCity = itemView.findViewById(R.id.restaurantCityFav);
        }
    }
}
