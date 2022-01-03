package com.example.as3_happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;


import com.example.as3_happymeals.adapter.LoginAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private LoginAdapter mLoginAdapter;
    private float i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTabLayout = findViewById(R.id.tab_layout1);
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout.addTab(mTabLayout.newTab().setText("Login"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Signup"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mLoginAdapter = new LoginAdapter(this);
        mViewPager.setAdapter(mLoginAdapter);

        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("Login");
                        break;
                    case 1:
                        tab.setText("Signup");
                        break;
                }
            }
        }).attach();


    }
}