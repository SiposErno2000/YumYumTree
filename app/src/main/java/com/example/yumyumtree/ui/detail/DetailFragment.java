package com.example.yumyumtree.ui.detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumyumtree.R;

public class DetailFragment extends Fragment {
    public final static String EXTRA_ID = "id";

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle bundle = this.getArguments();
        String id = bundle.getString(EXTRA_ID);
        Log.d("Sipos", id);
        return view;
    }
}