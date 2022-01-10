package com.example.as3_happymeals;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.as3_happymeals.model.Package;
import com.google.android.material.button.MaterialButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ViewPackageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_package);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Package> packageList = realm.where(Package.class).sort("createdTime", Sort.DESCENDING).findAll();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(),packageList);
        recyclerView.setAdapter(myAdapter);

        packageList.addChangeListener(new RealmChangeListener<RealmResults<Package>>() {
            @Override
            public void onChange(RealmResults<Package> packages) {
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addPackage(View view){
        // Create add package dialog from builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create file from file builder
        View alert = LayoutInflater.from(this).inflate(R.layout.add_package_dialog, null);

        // Set layout for dialog
        builder.setView(alert);
        EditText packageName = alert.findViewById(R.id.packageName);
        EditText packageQuantity = alert.findViewById(R.id.packageQuantity);
        EditText packageOwner = alert.findViewById(R.id.packageOwner);
        EditText packageDescription = alert.findViewById(R.id.packageDescription);
        MaterialButton savePackageBtn = alert.findViewById(R.id.savePackageBtn);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        savePackageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product = packageName.getText().toString();
                Integer quantity = Integer.parseInt(packageQuantity.getText().toString());
                String owner = packageOwner.getText().toString();
                String description = packageDescription.getText().toString();
                long createdTime = System.currentTimeMillis();

                realm.beginTransaction();
                Package package1 = realm.createObject(Package.class);
                package1.setProductName(product);
                package1.setQuantity(quantity);
                package1.setOwner(owner);
                package1.setDescription(description);
                package1.setCreatedTime(createdTime);

                realm.commitTransaction();
                Toast.makeText(getApplicationContext(),"Package saved",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Create dialog and display
        builder.create().show();
    }
}