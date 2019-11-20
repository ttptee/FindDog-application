package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;

public class AllMissingPost extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private RecyclerView missingList;
    private FirebaseDatabase firebase;
    private DatabaseReference databaseReference;

    private FirebaseRecyclerOptions<MissingBlog> options;
    private FirebaseRecyclerAdapter<MissingBlog, ViewHolder> adapter;
    private GoogleMap mMap;
    Marker marker;
    public Double latitude;
    public Double longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 12;
    private static final String TAG = AllMissingPost.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_missing_post);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapalldog);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        ImageView logogo = findViewById(R.id.logo);
        logogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllMissingPost.this, MainActivity.class);
                startActivity(i);
            }
        });

        missingList = (RecyclerView) findViewById(R.id.missingListAll);
        missingList.setHasFixedSize(true);

        ChildEventListener mChildEventListener;
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");
        databaseReference = FirebaseDatabase.getInstance().getReference("postmiss");


        options = new FirebaseRecyclerOptions.Builder<MissingBlog>()
                .setQuery(databaseReference, MissingBlog.class).build();


        adapter = new FirebaseRecyclerAdapter<MissingBlog, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(ViewHolder viewHolder, int i, MissingBlog missingBlog) {
                Picasso.get().load(missingBlog.getImage()).into(viewHolder.postImg);

                viewHolder.postName.setText(missingBlog.getName());
                viewHolder.postBreed.setText(missingBlog.getBreed());

                final String post_key = getRef(i).getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleMissingPage = new Intent(AllMissingPost.this,SingleMissing.class);
                        singleMissingPage.putExtra("blog_id",post_key);
                        startActivity(singleMissingPage);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_row,parent,false);

                return new ViewHolder(view);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        missingList.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        missingList.setAdapter(adapter);






        ImageView add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (AllMissingPost.this,detaildogJava.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
            adapter.startListening();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap=googleMap;


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    String Lat = dss.child("Lat").getValue().toString();
                    String Lng = dss.child("Lng").getValue().toString();
                    String Breed=dss.child("breed").getValue().toString();
                    final String namee= dss.child("name").getValue().toString();
                    final String pic = dss.child("image").getValue().toString();
                    final String a = dss.getKey();



                    double latitude = Double.parseDouble(Lat);
                    double longitude = Double.parseDouble(Lng);
                    LatLng loc = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(loc).title(namee).snippet(Breed));


//                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                        @Override
//                        public void onInfoWindowClick(Marker marker) {
//                            Intent singleMissingPage = new Intent(AllMissingPost.this,SingleMissing.class);
//                            singleMissingPage.putExtra("blog_id",a);
//                            startActivity(singleMissingPage);
//                        }
//                    });


//                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                        @Override
//                        public View getInfoWindow(Marker marker) {
//                            return null;
//                        }
//
//                        @Override
//                        public View getInfoContents(Marker marker) {
//
//                            View v = getLayoutInflater().inflate(R.layout.info_window,null);
//
//                            TextView name = (TextView) v.findViewById(R.id.infotext);
//                            ImageView pict = (ImageView)v.findViewById(R.id.infoImage);
//
//                                name.setText(marker.getTitle());
//                                Picasso.get()
//                                        .load(pic)
//                                        .into(pict);
//
//
//                            return v;
//                        }
//                    })          ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    //
//
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }




   /* @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.startListening();

    }*/
}
