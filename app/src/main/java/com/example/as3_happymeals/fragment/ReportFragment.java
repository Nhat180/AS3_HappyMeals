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

import com.example.as3_happymeals.MapsActivity;
import com.example.as3_happymeals.R;
import com.example.as3_happymeals.model.Site;
import com.example.as3_happymeals.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {
    private TextView siteName, leaderName, totalMember, totalComment;
    private ListView listView;
    private Button viewBtn, cancelButton;
    private List<String> members = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Site site = MapsActivity.site;
    private User user = MapsActivity.user;
    private String role = MapsActivity.role;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.report_fragment, container, false);

        siteName = root.findViewById(R.id.report_sitename);
        leaderName = root.findViewById(R.id.report_leadername);
        totalMember = root.findViewById(R.id.report_totalpeople);
        totalComment = root.findViewById(R.id.report_totalcomment);
        viewBtn = root.findViewById(R.id.viewMemBtn);

        siteName.setText(site.getName());
        leaderName.setText("Leader: " + site.getLeaderName());
        totalMember.setText("Total of Registered People: " + site.getTotalPeople());
        totalComment.setText("Total of Comments: " + site.getTotalComment());

        if (role.equals("0")) {
            viewBtn.setVisibility(View.GONE);
        }

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
                View form = LayoutInflater.from(getActivity()).inflate(R.layout.list_member_dialog, null);
                builder.setView(form);

                db.collection("sites").document(site.getLeaderUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                members = (List<String>) documentSnapshot.get("userRegistered");
                                listView = form.findViewById(R.id.list);
                                ArrayAdapter arrayAdapter =
                                        new ArrayAdapter(getActivity(),
                                                android.R.layout.simple_list_item_1, members);
                                listView.setAdapter(arrayAdapter);
                            }
                        });

                cancelButton = form.findViewById(R.id.cancelPackageBtn);


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