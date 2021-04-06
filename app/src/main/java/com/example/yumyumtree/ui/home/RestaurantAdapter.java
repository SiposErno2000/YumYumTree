package com.example.yumyumtree.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yumyumtree.R;
import com.example.yumyumtree.ui.detail.DetailFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.yumyumtree.ui.detail.DetailFragment.EXTRA_ID;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> implements Filterable {

    private final List<Restaurant> restaurantListAll;
    private final Context context;
    private List<Restaurant> restaurantList;

    public RestaurantAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
        restaurantListAll = new ArrayList<>(restaurantList);
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_layout_restaurants, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
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

    @Override
    public Filter getFilter() {
        return restaurantFilter;
    }

    private final Filter restaurantFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Restaurant> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(restaurantListAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Restaurant rest : restaurantListAll) {
                    if (rest.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(rest);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurantList = (List<Restaurant>) results.values;
            notifyDataSetChanged();
        }
    };

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView restName, restId, restArea, restCity;
        ImageView restImage;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            restImage = itemView.findViewById(R.id.restaurantImage);
            restName = itemView.findViewById(R.id.restaurantName);
            restId = itemView.findViewById(R.id.restaurantId);
            restArea = itemView.findViewById(R.id.restaurantArea);
            restCity = itemView.findViewById(R.id.restaurantCity);
        }
    }
}
