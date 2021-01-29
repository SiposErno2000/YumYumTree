package com.example.yumyumtree.UI.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumyumtree.R;
import com.example.yumyumtree.UI.Register.RegisterFragment;

public class LoginFragment extends Fragment {

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_login, container, false);
        result.findViewById(R.id.signup_button).setOnClickListener(signUpButtonClickListener);
        return result;
    }

    private View.OnClickListener signUpButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.loginactivity, registerFragment).commit();
        }
    };
}