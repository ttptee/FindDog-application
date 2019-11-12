package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleMissing extends AppCompatActivity {
    private String mPost_key = null;
    private DatabaseReference databaseReference;
    private DatabaseReference reff;

    private TextView textName;
    private TextView textBreed;
    private TextView textSpecial;
    private TextView textDatetime;
    private TextView textPrize;
    private TextView textTel;
    private TextView textOwner;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_missing);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");
        mPost_key = getIntent().getExtras().getString("blog_id");
        reff = FirebaseDatabase.getInstance().getReference().child("user");

        textName = (TextView) findViewById(R.id.name);
        textBreed = (TextView) findViewById(R.id.breed);
        textSpecial = (TextView) findViewById(R.id.special);
        textDatetime = (TextView) findViewById(R.id.datetime);
        textPrize = (TextView) findViewById(R.id.prize);
        textTel = (TextView) findViewById(R.id.telOwner);
        textOwner = (TextView) findViewById(R.id.postOwner);
        image = (ImageView) findViewById(R.id.imgShow);

         /*Toast.makeText(SingleMissing.this,mPost_key,Toast.LENGTH_SHORT).show();*/


        databaseReference.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String post_name =(String) dataSnapshot.child("name").getValue();
                String post_breed =(String) dataSnapshot.child("breed").getValue();
                String post_special =(String) dataSnapshot.child("special").getValue();
                String post_datetime =(String) dataSnapshot.child("datetime").getValue();
                String post_prize =(String) dataSnapshot.child("prize").getValue();
                String uid =(String) dataSnapshot.child("uid").getValue();

                reff.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String post_tel =(String) dataSnapshot.child("phone").getValue();
                        String post_owner =(String) dataSnapshot.child("name").getValue();
                        textTel.setText(post_tel);
                        textOwner.setText(post_owner);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                String post_img =(String) dataSnapshot.child("image").getValue();

                textName.setText(post_name);
                textBreed.setText(post_breed);
                textSpecial.setText(post_special);
                textDatetime.setText(post_datetime);
                textPrize.setText(post_prize);

                Picasso.get().load(post_img).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
