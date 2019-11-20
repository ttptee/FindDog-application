package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleAdopt extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private String mPost_key = null;
    private DatabaseReference databaseReference;
    private DatabaseReference reff;

    private TextView textName;
    private TextView textBreed;
    private ImageView image;
    private TextView textTel;
    private TextView textOwner;
    private  ImageView backButton;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_adopt);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.showmap);
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("postadopt");
        mPost_key = getIntent().getExtras().getString("blog_id");
        reff = FirebaseDatabase.getInstance().getReference().child("user");

        textName = (TextView) findViewById(R.id.name);
        textBreed = (TextView) findViewById(R.id.breed);
        image = (ImageView) findViewById(R.id.imgShow);
        textTel = (TextView) findViewById(R.id.telOwner);
        textOwner = (TextView) findViewById(R.id.postOwner);
        backButton = (ImageView) findViewById(R.id.backbtn);
        backButton.setOnClickListener(this);

        /*Toast.makeText(SingleAdopt.this,post_key,Toast.LENGTH_SHORT).show();*/
    }
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap=googleMap;

        databaseReference.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String post_name =(String) dataSnapshot.child("name").getValue();
                String post_breed =(String) dataSnapshot.child("breed").getValue();
                String post_img =(String) dataSnapshot.child("image").getValue();
                String uid = (String) dataSnapshot.child("uid") .getValue();
                final Double latitude = dataSnapshot.child("Lat").getValue(Double.class);
                final Double longitude = dataSnapshot.child("Lng").getValue(Double.class);

                reff.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String post_tel =(String) dataSnapshot.child("phone").getValue();
                        String post_owner =(String) dataSnapshot.child("name").getValue();
                        textTel.setText(post_tel);
                        textOwner.setText(post_owner);

                        LatLng location = new LatLng(latitude, longitude);

                        mMap.addMarker((new MarkerOptions().position(location)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                textName.setText(post_name);
                textBreed.setText(post_breed);

                Picasso.get().load(post_img).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View v) {

        if(v==backButton){
            finish();
        }
    }
}
