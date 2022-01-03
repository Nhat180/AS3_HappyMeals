package com.example.as3_happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    private ImageView bg;
    private TextView txtHappy, txtMeal, slogan, description;
    private LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bg = findViewById(R.id.background);
        txtHappy = findViewById(R.id.txtHappy);
        txtMeal = findViewById(R.id.txtMeal);
        lottieAnimationView = findViewById(R.id.lottie);
        slogan = findViewById(R.id.slogan);
        description = findViewById(R.id.description);
        bg.animate().translationY(-2500).setDuration(1000).setStartDelay(5000);
        txtHappy.animate().translationY(-2000).setDuration(1000).setStartDelay(5000);
        txtMeal.animate().translationY(-2000).setDuration(1000).setStartDelay(5000);
        slogan.animate().translationY(1500).setDuration(1000).setStartDelay(5000);
        lottieAnimationView.animate().translationY(1500).setDuration(1000).setStartDelay(5000);
        description.animate().translationY(1500).setDuration(1000).setStartDelay(5000);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(new Intent(SplashActivity.this, HomePageActivity.class));
                finish();
            }
        }, 6000);
    }


}