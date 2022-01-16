package com.example.as3_happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.as3_happymeals.adapter.CampaignAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CampaignActivity extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private CampaignAdapter mCampaignAdapter;
    private TabLayout mTabLayout;
    private ImageButton backToMapBtn;
    final int[] ICONS = new int[]{
            R.drawable.add_pac,
            R.drawable.comment_list,
            R.drawable.report
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);


        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout.addTab(mTabLayout.newTab().setText("Package"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Comment"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Report"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mCampaignAdapter = new CampaignAdapter(this);
        mViewPager.setAdapter(mCampaignAdapter);

        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("Package");
                        tab.setIcon(ICONS[0]);
                        break;
                    case 1:
                        tab.setText("Comment");
                        tab.setIcon(ICONS[1]);
                        break;
                    case 2:
                        tab.setText("Report");
                        tab.setIcon(ICONS[2]);
                        break;
                }
            }
        }).attach();

        backToMapBtn = findViewById(R.id.campaignBackToMap);
        backToMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                finish();
            }
        });
    }
}