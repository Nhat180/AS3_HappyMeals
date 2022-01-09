package com.example.as3_happymeals.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.R;

public class ReportFragment extends Fragment {
    private TextView textView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.report_fragment, container, false);
        textView = root.findViewById(R.id.report);


        return root;
    }
}