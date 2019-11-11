package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class viewProfile extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceUser;
    private DatabaseReference ref;

    private TextView userEmail;
    private TextView name;
    private TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        ImageView imgedit = findViewById(R.id.editProfile);
        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(viewProfile.this, editProfileJ.class);
                startActivity(i);
            }
        });

        ImageView logogo = findViewById(R.id.logo);
        logogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(viewProfile.this, MainActivity.class);
                startActivity(i);
            }
        });
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("user");
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = firebaseAuth.getCurrentUser().getUid();

        userEmail = (TextView) findViewById(R.id.textViewUserEmail);
        userEmail.setText("Welcome "+user.getEmail());

        name= (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);

        ref = FirebaseDatabase.getInstance().getReference().child("user").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child("name").getValue().toString();
                String p = dataSnapshot.child("phone").getValue().toString();
                name.setText(n);
                phone.setText(p);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String user_id = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    finish();
                    startActivity(new Intent(viewProfile.this,viewProfile.class));
                }   else {
                    finish();
                    startActivity(new Intent(viewProfile.this,editProfileJ.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }

    private void checkUser (){
        final String user_id = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent goToMain = new Intent(viewProfile.this,viewProfile.class);
                    startActivity(goToMain);
                }   else {
                    Intent profile = new Intent(viewProfile.this,editProfileJ.class);
                    startActivity(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
