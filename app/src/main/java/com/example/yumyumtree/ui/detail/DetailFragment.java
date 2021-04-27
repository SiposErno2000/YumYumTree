package com.example.yumyumtree.ui.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yumyumtree.R;
import com.example.yumyumtree.data.api.RestaurantsCache;
import com.example.yumyumtree.data.api.UserProfileHandler;
import com.example.yumyumtree.ui.home.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static com.example.yumyumtree.R.drawable.redheart;
import static com.example.yumyumtree.data.api.UserProfileHandler.CHILD;
import static com.example.yumyumtree.ui.login.LoginFragment.CURRENT_NAME;
import static com.example.yumyumtree.ui.login.LoginFragment.USERS;

public class DetailFragment extends Fragment {

    public final static String EXTRA_ID = "id";
    private final UserProfileHandler userProfileHandler = UserProfileHandler.getInstance();
    private View view;
    private String name;
    private String id;
    private String phone;
    private String area;
    private String city;
    private String image_url;
    private String address;
    private String reserve_url;
    private ImageView favouriteImage;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        createUIElements();
        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void createUIElements() {
        TextView restId, restArea, restCity, restAddress, restPhone, restName;
        ImageView restImage;

        RestaurantsCache restaurantsCache = RestaurantsCache.getInstance();

        restName = view.findViewById(R.id.restName);
        restId = view.findViewById(R.id.restId);
        restAddress = view.findViewById(R.id.restAdress);
        restArea = view.findViewById(R.id.restArea);
        restCity = view.findViewById(R.id.restCity);
        restPhone = view.findViewById(R.id.restPhone);
        restImage = view.findViewById(R.id.restImage);
        favouriteImage = view.findViewById(R.id.favImage);
        view.findViewById(R.id.readMoreButton).setOnClickListener(readMoreButtonClickListener);
        view.findViewById(R.id.favImage).setOnClickListener(favButtonClickListener);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString(EXTRA_ID);
            if (favouriteCheck(id)) {
                Glide.with(getContext()).load(redheart).into(favouriteImage);
            }
            Restaurant restaurant = restaurantsCache.getRestaurant(id);
            name = restaurant.getName();
            phone = restaurant.getPhone();
            city = restaurant.getCity();
            area = restaurant.getArea();
            address = restaurant.getAddress();
            image_url = restaurant.getImage_url();
            reserve_url = restaurant.getReserve_url();
        }

        restName.setText(name);
        restId.setText(id);
        restArea.setText(area);
        restCity.setText(city);
        restAddress.setText(address);
        restPhone.setText(phone);
        Glide.with(Objects.requireNonNull(getContext())).load(image_url).into(restImage);
    }

    private final View.OnClickListener readMoreButtonClickListener = v -> {
        Uri uri = Uri.parse(reserve_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    };

    private final View.OnClickListener favButtonClickListener = v -> {
        DatabaseReference rootRef;
        if (favouriteCheck(id)) {
            userProfileHandler.removeItemFromList(id);
            rootRef = FirebaseDatabase.getInstance().getReference(USERS);
            rootRef.child(CURRENT_NAME).child(CHILD).child(id).removeValue();
        } else {
            userProfileHandler.addItemtoList(id);
            rootRef = FirebaseDatabase.getInstance().getReference(USERS);
            rootRef.child(CURRENT_NAME).child(CHILD).child(id).setValue(id);
            Glide.with(getContext()).load(redheart).into(favouriteImage);
        }
    };

    private boolean favouriteCheck(String id) {
        for (int i=0; i<userProfileHandler.getFavouriteList().size(); i++) {
            if (userProfileHandler.getFavouriteList().get(i).equals(id)) {
                return true;
            }
        }
        return false;
    }
}