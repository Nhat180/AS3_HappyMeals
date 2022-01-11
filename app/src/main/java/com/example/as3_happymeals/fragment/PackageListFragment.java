package com.example.as3_happymeals.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.as3_happymeals.R;
import com.example.as3_happymeals.adapter.PackageAdapter;
import com.example.as3_happymeals.model.Package;

import java.util.ArrayList;
import java.util.List;

public class PackageListFragment extends Fragment {


    private ListView listView;
    private List<Package> arrayList;
    private PackageAdapter adapter;
    private Button addPackButton, savePackageBtn, cancelButton;
    private EditText packageName, packageQuantity, packageDescription;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.list_package_fragment, container, false);
        listView = root.findViewById(R.id.list);

        arrayList = new ArrayList<>();
        arrayList.add(new Package(1, "Rau ma", "32 Kg", "moi nguoiu nhan 0.5 kg" ));
        arrayList.add(new Package(2,"Rau muong","40 cọng", " very good"));

        adapter = new PackageAdapter(getActivity(), R.layout.item_layout, arrayList);
        listView.setAdapter(adapter);


        addPackButton = root.findViewById(R.id.addPackBtn);
        addPackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                View form = LayoutInflater.from(getActivity()).inflate(R.layout.add_dialog_layout, null);
                builder.setView(form);

                packageName = form.findViewById(R.id.packageName);
                packageQuantity = form.findViewById(R.id.packageQuantity);
                packageDescription = form.findViewById(R.id.packageDescription);
                savePackageBtn = form.findViewById(R.id.savePackageBtn);
                cancelButton = form.findViewById(R.id.cancelPackageBtn);

                savePackageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getActivity(),"Package saved",Toast.LENGTH_SHORT).show();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"cancel",Toast.LENGTH_SHORT).show();
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });

        return root;
    }
}

