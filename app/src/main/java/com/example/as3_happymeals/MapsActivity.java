package com.example.as3_happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.annotation.SuppressLint;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;

import com.example.as3_happymeals.model.Site;
import com.example.as3_happymeals.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.as3_happymeals.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String SITE_API_URL = "http://10.0.2.2:3000/sites";
    private static final int MY_PERMISSION_REQUEST_LOCATION = 99;
    public static String role;
    private String markerID;
    private String querySearch;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private BottomNavigationView bottomNavigationView;
    protected FusedLocationProviderClient client;
    protected LocationRequest mLocationRequest;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    public static User user = new User(); // User information
    public static Site site = new Site(); // Site information

    private FloatingActionButton fab;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the current user login
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class); // Get user detail info
                }
            });
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        https://www.youtube.com/watch?v=ywqCTCR2a0w
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.action_map);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_map:
                        return true;
                    case R.id.action_info:
                        startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.action_list:
                        startActivity(new Intent(getApplicationContext(), ListActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.action_logout:
                        AlertDialog.Builder logoutAlert = new AlertDialog.Builder(MapsActivity.this);
                        logoutAlert.setTitle("Logout Confirmation!").
                                setMessage("Do you want to logout?");
                        // back to login screen
                        logoutAlert.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        firebaseAuth.signOut();
                                        startActivity(new Intent(MapsActivity.this, HomePageActivity.class));
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

        fab = findViewById(R.id.my_position);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        registerService(); // Register broadcast service
    }

    // Enable current location and get all sites on map
    @Override
    protected void onResume() {
        super.onResume();
        enableMyLocation();
        new GetSites().execute();
    }

    // Get current location
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        client.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLatitude(), location.getLongitude());
            }
        });
    }

    // Go to current location
    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mMap.moveCamera(cameraUpdate);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        startLocationUpdate();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (currentUser == null) {
                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                } else {
                    Intent intent = new Intent(MapsActivity.this, AddLocationActivity.class);
                    intent.putExtra("latitude", latLng.latitude);
                    intent.putExtra("longitude", latLng.longitude);
                    startActivity(intent);
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (currentUser == null) {
                    startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                } else {
                    markerID = marker.getTitle();
                    new GetASite().execute();
                    if (user.getIsAdmin().equals("0")
                        && !currentUser.getUid().equals(marker.getTitle())) {
                        role = "0"; // set role (0 is normal user)
                        // Already joining the selected site
                        if (user.getSiteRegistered().contains(marker.getTitle())) {
                            Intent intent = new Intent(MapsActivity.this,CampaignActivity.class);
                            startActivity(intent);
                        } else { // First time join the site, register to the site
                            builder.setTitle(marker.getSnippet())
                                    .setMessage("Do you want to join this site campaign?")
                                    .setCancelable(true)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Register to the site (WRITE)
                                            DocumentReference docUser = db.collection("users")
                                                    .document(currentUser.getUid());
                                            docUser.update("siteRegistered",
                                                    FieldValue.arrayUnion(marker.getTitle()));

                                            DocumentReference docSite = db.collection("sites")
                                                    .document(marker.getTitle());
                                            docSite.update("userRegistered",
                                                    FieldValue.arrayUnion(currentUser.getEmail()));

                                            site.setTotalPeople(site.getTotalPeople() + 1);
                                            new PutSite().execute();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();

                        }
                    }
                    else { // Leader and Admin access the site
                        if (currentUser.getUid().equals(marker.getTitle())) {
                            role = "2"; // Leader
                        } else {
                            role = "1"; // Admin
                        }
                        Intent intent = new Intent(MapsActivity.this, CampaignActivity.class);
                        startActivity(intent);
                    }

                }
                return true;
            }
        });

    }

    // Enable my location view on map
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        }
    }

    // Request permission from user to open map
    private void requestPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
    }

    // Update the current location
    @SuppressLint({"MissingPermission", "RestrictedApi"})
    private void startLocationUpdate() {
        mLocationRequest = new LocationRequest();
        client.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            }
        }, null);
    }

    // Broadcast Receiver
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level / (float) scale;
                if (batteryPct < 0.2) {
                    sendBatteryAlert();
                }
            }
        }
    };


    // Register to Receiver
    private void registerService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    // Alert low battery
    private void sendBatteryAlert(){
        AlertDialog.Builder batteryBuilder = new AlertDialog.Builder(this);
            batteryBuilder.setTitle("LOW BATTERY!!!")
                    .setMessage("Your phone battery is under 20%")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = batteryBuilder.create();
            alertDialog.show();
    }


    // Customized site markers
    private BitmapDescriptor bitmapDescriptor (Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth()
                , vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Add Marker for sites from RESTAPI
    private void addMarkerFromHttpHandler (JSONObject jsonObject) throws JSONException {
        LatLng position = new LatLng(
                jsonObject.getDouble("latitude"),
                jsonObject.getDouble("longitude"));
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(jsonObject.getString("id"))
                .snippet(jsonObject.getString("name")));
    }

    // Get All Sites
    private class GetSites extends AsyncTask<Void, Void, Void> {
        String jsonString = "";

        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.getRequest(SITE_API_URL);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    addMarkerFromHttpHandler(jsonObject);
                }
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
            jsonString = HttpHandler.getRequest(SITE_API_URL + "/" + markerID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                site.setName(jsonObject.getString("name"));
                site.setLeaderUid(jsonObject.getString("id"));
                site.setLeaderName(jsonObject.getString("leaderName"));
                site.setLatitude(jsonObject.getDouble("latitude"));
                site.setLongitude(jsonObject.getDouble("longitude"));
                site.setTotalPeople(jsonObject.getInt("totalPeople"));
                site.setTotalComment(jsonObject.getInt("totalComment"));
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
            status = HttpHandler.putRequest(MapsActivity.SITE_API_URL + "/" + site.getLeaderUid(), site);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            finish();
        }
    }

}

