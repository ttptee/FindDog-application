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

public class SingleAdopt extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference databaseReference;

    private TextView textName;
    private TextView textBreed;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_adopt);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("postadopt");
        mPost_key = getIntent().getExtras().getString("blog_id");

        textName = (TextView) findViewById(R.id.name);
        textBreed = (TextView) findViewById(R.id.breed);
        image = (ImageView) findViewById(R.id.imgShow);

        /*Toast.makeText(SingleAdopt.this,post_key,Toast.LENGTH_SHORT).show();*/

        databaseReference.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String post_name =(String) dataSnapshot.child("name").getValue();
                String post_breed =(String) dataSnapshot.child("breed").getValue();
                String post_img =(String) dataSnapshot.child("image").getValue();

                textName.setText(post_name);
                textBreed.setText(post_breed);

                Picasso.get().load(post_img).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
