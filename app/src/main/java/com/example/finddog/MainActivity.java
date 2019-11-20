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
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    private List<SlideModel> slideLists;
    private List<SlideModel> slideLists2;
    private ViewFlipper viewFlipper;
    private ViewFlipper viewFlipper2;
    DatabaseReference databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        ImageView imgGoLogin = findViewById(R.id.loginbar);
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
                Intent i = new Intent(MainActivity.this, AllMissingPost.class);
                startActivity(i);
            }
        });
        ViewFlipper godog = findViewById(R.id.viewFlipper_slide_show);
        godog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AllMissingPost.class);
                startActivity(i);
            }
        });
        ViewFlipper godog2 = findViewById(R.id.viewFlipper_slide_show2);
        godog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, allAdoptPost.class);
                startActivity(i);
            }
        });

        ImageView imggoadopt = findViewById(R.id.goadopt);
        imggoadopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, allAdoptPost.class);
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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("postadopt");
        slideLists = new ArrayList<>();
        slideLists2 = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        usingFirebaseDatabase();
        usingFirebaseDatabase2();
    }

    private void initialize() {
        viewFlipper = findViewById(R.id.viewFlipper_slide_show);
        viewFlipper2 = findViewById(R.id.viewFlipper_slide_show2);

    }

    private void usingFirebaseDatabase() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    slideLists.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SlideModel model = snapshot.getValue(SlideModel.class);

                        slideLists.add(model);
                    }
                    usingFirebaseImages(slideLists);
                } else {
                    Toast.makeText(MainActivity.this, "No images in firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "NO images found \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void usingFirebaseDatabase2() {
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("postadopt");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    slideLists2.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SlideModel model2 = snapshot.getValue(SlideModel.class);

                        slideLists2.add(model2);
                    }
                    usingFirebaseImages2(slideLists2);
                } else {
                    Toast.makeText(MainActivity.this, "No images in firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "NO images found \n" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void usingFirebaseImages(List<SlideModel> slideLists) {
        for (int i = 0; i < slideLists.size(); i++) {
            String downloadImage = slideLists.get(i).getImage();
            flipImages(downloadImage);

        }
    }
    private void usingFirebaseImages2(List<SlideModel> slideLists2) {
        for (int i = 0; i < slideLists2.size(); i++) {
            String downloadImage2 = slideLists2.get(i).getImage();
            flipImages2(downloadImage2);

        }
    }


    public void flipImages(String image) {
        ImageView imageView = new ImageView(this);
        Picasso.get().load(image).into(imageView);

        viewFlipper.addView(imageView);

        viewFlipper.setFlipInterval(2500);
        viewFlipper.setAutoStart(true);

        viewFlipper.startFlipping();
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

    }

    public void flipImages2(String image) {
        ImageView imageView = new ImageView(this);
        Picasso.get().load(image).into(imageView);

        viewFlipper2.addView(imageView);

        viewFlipper2.setFlipInterval(2500);
        viewFlipper2.setAutoStart(true);

        viewFlipper2.startFlipping();
        viewFlipper2.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper2.setOutAnimation(this, android.R.anim.slide_out_right);


    }
}







