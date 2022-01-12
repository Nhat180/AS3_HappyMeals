package com.example.as3_happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.as3_happymeals.model.Site;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddLocationActivity extends AppCompatActivity {
    private EditText siteName;
    private EditText siteLat, siteLong;
    private Button confirmBtn;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private Site site = new Site();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        currentUser = firebaseAuth.getCurrentUser(); // Get current user login
        site.setLeaderUid(currentUser.getUid());

        // Get intent
        Intent intent = getIntent();
        site.setLatitude(intent.getDoubleExtra("latitude",0));
        site.setLongitude(intent.getDoubleExtra("longitude",0));

        siteLat = findViewById(R.id.add_location_latitude);
        siteLong = findViewById(R.id.add_location_longitude);
        siteLat.setText(site.getLatitude() + "");
        siteLong.setText(site.getLongitude() + "");

        // Confirm create a new site
        confirmBtn = findViewById(R.id.add_location_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siteName = findViewById(R.id.add_location_sitename);
                site.setName(siteName.getText().toString());
                site.setLeaderName(currentUser.getEmail());
                site.setTotalPeople(0);
                site.setTotalComment(0);
                DocumentReference df = db.collection("sites").document(currentUser.getUid());
                Map<String, Object> siteData = new HashMap<>();
                siteData.put("userRegistered", Collections.singletonList("Leader: " + currentUser.getEmail()));
                df.set(siteData);
                new PostSite().execute();
            }
        });

    }

    private class PostSite extends AsyncTask<Void, Void, Void> {
        private String status = "";
        @Override
        protected Void doInBackground(Void... voids) {
            status = HttpHandler.postRequest(MapsActivity.SITE_API_URL, site);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(AddLocationActivity.this, MapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}