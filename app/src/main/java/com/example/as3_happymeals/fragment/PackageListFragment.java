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

import com.example.as3_happymeals.MapsActivity;
import com.example.as3_happymeals.R;
import com.example.as3_happymeals.adapter.PackageAdapter;
import com.example.as3_happymeals.model.Package;
import com.example.as3_happymeals.model.Site;
import com.example.as3_happymeals.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageListFragment extends Fragment {
    private ListView listView;
    private List<Package> arrayList = new ArrayList<>();
    private PackageAdapter adapter;
    private Button addPackButton, savePackageBtn, cancelButton;
    private EditText packageName, packageQuantity, packageDescription;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Site site = MapsActivity.site;
    private User user = MapsActivity.user;
    private String role = MapsActivity.role;
    private boolean isValid = true;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.list_package_fragment, container, false);
        listView = root.findViewById(R.id.list);


        db.collection("sites").document(site.getLeaderUid())
                .collection("packages").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Package packageData = documentSnapshot.toObject(Package.class);
                                arrayList.add(packageData);
                            }
                            adapter = new PackageAdapter(getActivity(), R.layout.item_layout, arrayList);
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error getting packages",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//        arrayList.add(new Package("Rau ma", "32 Kg", "moi nguoiu nhan 0.5 kg" ));
//        arrayList.add(new Package("Rau muong","40 cọng", " very good"));
//
//        adapter = new PackageAdapter(getActivity(), R.layout.item_layout, arrayList);
//        listView.setAdapter(adapter);


        addPackButton = root.findViewById(R.id.addPackBtn);

        if (role.equals("0") || role.equals("1")) {
            addPackButton.setVisibility(View.GONE);
        }

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
                        checkField(packageName);
                        checkField(packageQuantity);
                        checkField(packageDescription);
                        if(isValid) {
                            String name = packageName.getText().toString();
                            String quantity = packageQuantity.getText().toString();
                            String note = packageDescription.getText().toString();
                            DocumentReference df = db.collection("sites").document(site.getLeaderUid())
                                    .collection("packages").document(name);
                            Map<String, Object> packageData = new HashMap<>();
                            packageData.put("packageName", name);
                            packageData.put("quantity", quantity);
                            packageData.put("description", note);
                            df.set(packageData);


                            arrayList.add(new Package(name, quantity, note));
                            adapter = new PackageAdapter(getActivity(), R.layout.item_layout, arrayList);
                            listView.setAdapter(adapter);
                            Toast.makeText(getActivity(),"Package saved",Toast.LENGTH_SHORT).show();

                            packageName.getText().clear();
                            packageQuantity.getText().clear();
                            packageDescription.getText().clear();
                        }

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

    // Check if users have input the data
    private boolean checkField (EditText editText) {
        if(editText.getText().toString().isEmpty()) {
            editText.setError("Required");
            isValid = false;
        }

        return isValid;
    }
}

