package com.example.as3_happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.as3_happymeals.model.Site;
import com.example.as3_happymeals.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private User user = new User();
    private Site site = new Site(); // Site information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        currentUser = firebaseAuth.getCurrentUser(); // Get the current user login

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setSelectedItemId(R.id.action_info);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_map:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.action_info:
                        return true;
                    case R.id.action_list:
                        startActivity(new Intent(getApplicationContext(), ListActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.action_logout:
                        AlertDialog.Builder logoutAlert = new AlertDialog.Builder(InfoActivity.this);
                        logoutAlert.setTitle("Logout Confirmation!").
                                setMessage("Do you want to logout?");
                        // back to login screen
                        logoutAlert.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(InfoActivity.this, HomePageActivity.class));
                                        finish();
                                    }
                                });

                        // Cancel
                        logoutAlert.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertLogOutDialog = logoutAlert.create();
                        alertLogOutDialog.show();
                        return false;
                }
                return false;
            }
        });
    }
}