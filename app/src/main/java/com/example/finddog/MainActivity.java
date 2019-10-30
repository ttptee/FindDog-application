package com.example.finddog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
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


    imgGoLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent (MainActivity.this,loginJ.class);
                    startActivity(i);
        }
    });

        ImageView imggofind = findViewById(R.id.gofind);

        imggofind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,111);
            }



        });

}



}
