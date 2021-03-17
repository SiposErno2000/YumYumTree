package com.example.yumyumtree.ui.favourites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumyumtree.R;
import com.example.yumyumtree.ui.home.HomeFragment;

public class FavouritesFragment extends Fragment {

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
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