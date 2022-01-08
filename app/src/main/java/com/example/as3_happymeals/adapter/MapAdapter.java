package com.example.as3_happymeals.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.as3_happymeals.fragment.InfoFragment;
import com.example.as3_happymeals.fragment.ListFragment;
import com.example.as3_happymeals.fragment.MapsFragment;

public class MapAdapter extends FragmentStateAdapter {

    public MapAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MapsFragment();
            case 1:
                return new InfoFragment();
            case 2:
                return new ListFragment();
            default:
                return new MapsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
