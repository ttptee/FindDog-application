package com.example.finddog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgGoLogin = findViewById(R.id.loginbar);
    ImageView imgGoFind = findViewById(R.id.gofind);
                imgGoLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, loginJ.class);
            startActivity(i);
        }
    });

        ImageView imggofinddog = findViewById(R.id.gofinddog);
        imggofinddog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MainActivity.this,AllMissingPost.class);
                startActivity(i);
            }
        });

        ImageView imggoadopt = findViewById(R.id.goadopt);
        imggoadopt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent (MainActivity.this,allAdoptPost.class);
            startActivity(i);
        }
    });

    imgGoFind.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent e = new Intent(MainActivity.this, showPicAndML.class);
            startActivity(e);
        }
    });
}

}
