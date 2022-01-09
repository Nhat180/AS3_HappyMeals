package com.example.as3_happymeals.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.as3_happymeals.fragment.PackageListFragment;
import com.example.as3_happymeals.fragment.CommentFragment;
import com.example.as3_happymeals.fragment.ReportFragment;

public class CampaignAdapter extends FragmentStateAdapter {

    public CampaignAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PackageListFragment();
            case 1:
                return new CommentFragment();
            case 2:
                return new ReportFragment();
            default:
                return new PackageListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}


