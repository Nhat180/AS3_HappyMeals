package com.example.as3_happymeals.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.R;

public class ReportFragment extends Fragment {
    private TextView textView;
    private ListView listView;
    private Button viewBtn, cancelButton;
    private String name[] = {"Ta Quoc Thang", "Nguyen Van TOng", "Ho Duy Hoang", "Pham Thanh Dat", "Nguyen Minh Nhat", "Ngoc Hue", "Bill Hoang", "Trung Le", "Quang hai", "Hai Que", "Long Ngo", "Huy Ngo", "Man Lan", "Hoang Yen", "Johnsom", "Thong Nguyen"};
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.report_fragment, container, false);
        viewBtn = root.findViewById(R.id.viewMemBtn);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                View form = LayoutInflater.from(getActivity()).inflate(R.layout.list_member_dialog, null);
                builder.setView(form);
                listView = form.findViewById(R.id.list);
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, name);
                listView.setAdapter(arrayAdapter);
                cancelButton = form.findViewById(R.id.cancelPackageBtn);

//                savePackageBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Toast.makeText(getActivity(),"Package saved",Toast.LENGTH_SHORT).show();
//                    }
//                });
//
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });

        return root;
    }


}