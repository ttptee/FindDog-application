package com.example.finddog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.type.LatLng;

import org.w3c.dom.Text;

public class checkPostBreed extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_post_breed);
        SharedPreferences sp = getSharedPreferences("BreedText", Context.MODE_PRIVATE);
        String TextBreed = sp.getString("BreedMLText", "Error");

        TextView testBreed = findViewById(R.id.BreedText);
        testBreed.setText(TextBreed);








    }

}
