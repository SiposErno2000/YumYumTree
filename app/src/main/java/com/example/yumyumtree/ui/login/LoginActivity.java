package com.example.yumyumtree.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.yumyumtree.R;
import com.example.yumyumtree.ui.favourites.FavouritesFragment;
import com.example.yumyumtree.ui.home.HomeFragment;
import com.example.yumyumtree.ui.profile.ProfileFragment;
import com.example.yumyumtree.ui.register.RegisterFragment;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.loginactivity, loginFragment).commit();
    }

    @Override
    public void onBackPressed() {
        List fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for (Object f: fragmentList) {
            if (f instanceof HomeFragment) {
                handled = ((HomeFragment)f).onBackPressed();
                if (handled) {
                    break;
                }
            }
            else if (f instanceof ProfileFragment) {
                handled = ((ProfileFragment)f).onBackPressed();
                if (handled) {
                    break;
                }
            }
            else if (f instanceof FavouritesFragment) {
                handled = ((FavouritesFragment)f).onBackPressed();
                if (handled) {
                    break;
                }
            }
            else if (f instanceof RegisterFragment) {
                handled = ((RegisterFragment)f).onBackPressed();
                if (handled) {
                    break;
                }
            }
            else if (f instanceof LoginFragment) {
                handled = ((LoginFragment)f).onBackPressed();
                if (handled) {
                    break;
                }
            }
        }
        if (!handled) {
            super.onBackPressed();
        }
    }
}