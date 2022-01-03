package com.example.as3_happymeals.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.as3_happymeals.fragment.LoginTabFragment;
import com.example.as3_happymeals.fragment.SignupTabFragment;

public class LoginAdapter extends FragmentStateAdapter {


    public LoginAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LoginTabFragment();
            case 1:
                return new SignupTabFragment();
            default:
                return new SignupTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
