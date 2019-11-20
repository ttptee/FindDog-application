package com.example.finddog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class detaildogJava extends AppCompatActivity implements View.OnClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback {
    private static final String TAG = detaildogJava.class.getSimpleName();
    private static final int MAX_LENGTH = 20;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private boolean mLocationPermissionGranted;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText editTextName;
    private EditText editTextBreed;
    private EditText editTextSpecial;
    private EditText editTextDateTime;
    private EditText editTextPrize;

    private Button buttonSaveDetail;

    private ImageButton selectImage;
    private static final int GALLERY_REQUEST = 1;

    private Uri imgUri = null;

    private StorageReference storageReference;
    private CameraPosition mCameraPosition;
    private GoogleMap mMap;
    SearchView searchView;
    SupportMapFragment mapFragment;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    public LatLng pointll;
    private ImageView backButton;
    Button b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaildog);
//         Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }



        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please Login.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, loginJ.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        selectImage = (ImageButton) findViewById(R.id.btnAddImg);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });


        editTextName = (EditText) findViewById(R.id.name);
        editTextBreed = (EditText) findViewById(R.id.breed);
        editTextSpecial = (EditText) findViewById(R.id.special);
        editTextPrize = (EditText) findViewById(R.id.prize);
        buttonSaveDetail = (Button) findViewById(R.id.detaildogsubmit);
        backButton= (ImageView) findViewById(R.id.backbtn);


        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonSaveDetail.setOnClickListener(this);
        backButton.setOnClickListener(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        searchView = (SearchView) findViewById(R.id.search);
      mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mmap);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(detaildogJava.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mmap);
        mapFragment.getMapAsync(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imgUri = data.getData();
            selectImage.setImageURI(imgUri);

        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void saveDetail() {
        final String name = editTextName.getText().toString().trim();
        final String breed = editTextBreed.getText().toString().trim();
        final String special = editTextSpecial.getText().toString().trim();
        final String prize = editTextPrize.getText().toString().trim();
        final String uid = firebaseAuth.getCurrentUser().getUid();
        final double latitude = pointll.latitude;
        final double longitude = pointll.longitude;
        b = (Button) findViewById(R.id.detaildogsubmit);


        final StorageReference filepath = storageReference.child("MissingDogImg").child(random());
        filepath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DatabaseReference newPost = databaseReference.child("postmiss").push();

                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put("image", String.valueOf(uri));
                        newPost.child("image").setValue(hashMap.put("image", String.valueOf(uri)));
                        newPost.child("name").setValue(name);
                        newPost.child("breed").setValue(breed);
                        newPost.child("special").setValue(special);
                        newPost.child("prize").setValue(prize);
                        newPost.child("uid").setValue(uid);
                        newPost.child("Lat").setValue(latitude);
                        newPost.child("Lng").setValue(longitude);


                    }
                });

            }
        });


    }



    @Override
    public void onClick(View v) {
        if (v == buttonSaveDetail) {
            if ((TextUtils.isEmpty(editTextName.getText()))
                    || (TextUtils.isEmpty(editTextBreed.getText()))
                    || (TextUtils.isEmpty(editTextPrize.getText()))
                    || (TextUtils.isEmpty(editTextSpecial.getText())
                    )
            ) {
                Toast.makeText(this, "Please Complete All Fields", Toast.LENGTH_SHORT).show();
                /*b.setEnabled(false);*/
                /*finish();*/
            }
            else{
            Toast.makeText(this, "Adding", Toast.LENGTH_SHORT).show();
            saveDetail();
            Toast.makeText(this, "Add Complete", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, AllMissingPost.class));}
        }
        if(v==backButton){
            finish();

        }
    }

//
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setOnMapClickListener(this);
        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
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
    public void onMapClick(LatLng point) {
        DatabaseReference newPost = databaseReference;
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(point);

        // Clears the previously touched position
        mMap.clear();
//        newPost.removeValue();


        // Placing a marker on the touched position
        mMap.addMarker(markerOptions);

        pointll=point;



    }

    /*private void checkValidation() {

        if ((TextUtils.isEmpty(editTextName.getText()))
                || (TextUtils.isEmpty(editTextDateTime.getText()))
                || (TextUtils.isEmpty(editTextBreed.getText()))
                || (TextUtils.isEmpty(editTextPrize.getText()))
                || (TextUtils.isEmpty(editTextSpecial.getText())
        )
        ) {
            Toast.makeText(this, "Please Complete All Fields", Toast.LENGTH_SHORT).show();
            b.setEnabled(false);
        }

        else{
            b.setEnabled(true);
        }

    }*/

}
