package com.example.as3_happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.as3_happymeals.model.Site;
import com.example.as3_happymeals.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String markerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        currentUser = firebaseAuth.getCurrentUser(); // Get the current user login

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setSelectedItemId(R.id.action_list);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_map:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.action_info:
                        startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.action_list:
                        return true;
                    case R.id.action_logout:
                        AlertDialog.Builder logoutAlert = new AlertDialog.Builder(ListActivity.this);
                        logoutAlert.setTitle("Logout Confirmation!").
                                setMessage("Do you want to logout?");
                        // back to login screen
                        logoutAlert.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(ListActivity.this, HomePageActivity.class));
                                        finish();
                                    }
                                });

                        // Cancel
                        logoutAlert.setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertLogOutDialog = logoutAlert.create();
                        alertLogOutDialog.show();
                        return false;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        new GetSites().execute();
    }

    private class GetSites extends AsyncTask<Void,Void,Void> {
        private String jsonString="";

        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.getRequest(MapsActivity.SITE_API_URL);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<Integer> idList = new ArrayList<Integer>();
            ArrayList<String> idSite = new ArrayList<String>();
            ArrayList<Integer> totalPeople = new ArrayList<Integer>();

            ListView listView = findViewById(R.id.list);

            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i=0; i < jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    idList.add(i + 1);
                    names.add(jsonObject.getString("name"));
                    idSite.add(jsonObject.getString("id"));
                    totalPeople.add(jsonObject.getInt("totalPeople"));
                }

                ArrayAdapter adapter = new ArrayAdapter(
                        ListActivity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        names);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (currentUser == null) {
                            startActivity(new Intent(ListActivity.this, LoginActivity.class));
                        } else {
                            markerID = idSite.get(position);
                            new GetASite().execute();
                            if (MapsActivity.user.getIsAdmin().equals("0")
                                    && !currentUser.getUid().equals(idSite.get(position))) {
                                MapsActivity.role = "0"; // set role (0 is normal user)
                                // Already joining the selected site
                                if (MapsActivity.user.getSiteRegistered().contains(idSite.get(position))) {
                                    Intent intent = new Intent(ListActivity.this,CampaignActivity.class);
                                    startActivity(intent);
                                } else { // First time join the site, register to the site
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                                    builder.create();
                                    builder.setTitle("Do you want to join this site campaign?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Register to the site (WRITE)
                                                    DocumentReference docUser = db.collection("users")
                                                            .document(currentUser.getUid());
                                                    docUser.update("siteRegistered",
                                                            FieldValue.arrayUnion(idSite.get(position)));

                                                    DocumentReference docSite = db.collection("sites")
                                                            .document(idSite.get(position));
                                                    docSite.update("userRegistered",
                                                            FieldValue.arrayUnion(currentUser.getEmail()));

                                                    MapsActivity.site.setTotalPeople(totalPeople.get(position) + 1);
                                                    new PutSite().execute();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .show();
                                }
                            }
                            else { // Leader and Admin access the site
                                if (currentUser.getUid().equals(idSite.get(position))) {
                                    MapsActivity.role = "2"; // Leader
                                } else {
                                    MapsActivity.role = "1"; // Admin
                                }
                                Intent intent = new Intent(ListActivity.this, CampaignActivity.class);
                                startActivity(intent);
                            }

                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Get a specific site
    private class GetASite extends AsyncTask<Void, Void, Void> {
        String jsonString = "";

        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.getRequest(MapsActivity.SITE_API_URL + "/" + markerID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                MapsActivity.site.setName(jsonObject.getString("name"));
                MapsActivity.site.setLeaderUid(jsonObject.getString("id"));
                MapsActivity.site.setLeaderName(jsonObject.getString("leaderName"));
                MapsActivity.site.setLatitude(jsonObject.getDouble("latitude"));
                MapsActivity.site.setLongitude(jsonObject.getDouble("longitude"));
                MapsActivity.site.setTotalPeople(jsonObject.getInt("totalPeople"));
                MapsActivity.site.setTotalComment(jsonObject.getInt("totalComment"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Put method to update the total of volunteers
    private class PutSite extends AsyncTask<Void, Void, Void> {
        private String status = "";
        @Override
        protected Void doInBackground(Void... voids) {
            status = HttpHandler.putRequest(MapsActivity.SITE_API_URL + "/" + MapsActivity.site.getLeaderUid(), MapsActivity.site);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(getApplicationContext(), ListActivity.class));
            finish();
        }
    }
}