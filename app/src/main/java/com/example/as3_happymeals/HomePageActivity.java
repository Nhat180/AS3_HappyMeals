package com.example.as3_happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {
    private Button gotoMap, gotoSignIn;
    private TextView gotoSignUp;
//    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


//        currentUser = firebaseAuth.getCurrentUser();

        gotoMap = findViewById(R.id.welcomemap);
        gotoSignIn = findViewById(R.id.welcomesignin);

//        if (currentUser != null) {
//            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
//
//        }

        gotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));

            }
        });

        gotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }
}