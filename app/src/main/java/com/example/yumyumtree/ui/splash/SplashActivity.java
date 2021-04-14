package com.example.yumyumtree.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yumyumtree.R;
import com.example.yumyumtree.data.api.Constants;
import com.example.yumyumtree.data.api.RestaurantsCache;
import com.example.yumyumtree.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    Animation topAnimation, bottomAnimation;
    ImageView logo;
    TextView rest_name, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Constants constants = Constants.getInstance();

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo = findViewById(R.id.logo);
        rest_name = findViewById(R.id.rest_name);
        slogan = findViewById(R.id.slogan);

        logo.setAnimation(topAnimation);
        rest_name.setAnimation(bottomAnimation);
        slogan.setAnimation(bottomAnimation);

        bottomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                RestaurantsCache restaurantsCache = RestaurantsCache.getInstance();
                restaurantsCache.getRestaurants();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (constants.getDataLoading()) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    animation.reset();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!constants.getDataLoading()) {
                    animation.reset();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}