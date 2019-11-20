package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SingleMissing extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final int MAX_LENGTH = 20;
    private String mPost_key = null;
    private DatabaseReference databaseReference;
    private DatabaseReference reff;

    public Uri imgUri;

    private static final int GALLERY_REQUEST = 1;

    private DatabaseReference Un;
    private StorageReference storageReference;

    private TextView textName;
    private TextView textBreed;
    private TextView textSpecial;
    private TextView textPrize;
    private TextView textTel;
    private TextView textOwner;
    private ImageView image;
    private GoogleMap mMap;
    private  ImageView backButton;


    private EditText editTextComment;
    private Button buttonSendComment;

    private FirebaseAuth firebaseAuth;

    /*private final Object timestamp = ServerValue.TIMESTAMP;*/

    RecyclerView recyclerView;

    List<ModelComment> commentLit;
    AdapterComment adapterComment;

    private Button imgBtn;
    private ImageView imgShow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_missing);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.showmap);
        mapFragment.getMapAsync(this);


        imgBtn = (Button) findViewById(R.id.commentBtn);
        imgShow = (ImageView) findViewById(R.id.commentPic);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");
        mPost_key = getIntent().getExtras().getString("blog_id");
        reff = FirebaseDatabase.getInstance().getReference().child("user");

        storageReference = FirebaseStorage.getInstance().getReference();


        firebaseAuth = FirebaseAuth.getInstance();

        Un = FirebaseDatabase.getInstance().getReference();

        editTextComment = (EditText) findViewById(R.id.commentBox);
        buttonSendComment = (Button) findViewById(R.id.sendButton);

        buttonSendComment.setOnClickListener(this);

        textName = (TextView) findViewById(R.id.name);
        textBreed = (TextView) findViewById(R.id.breed);
        textSpecial = (TextView) findViewById(R.id.special);
        textPrize = (TextView) findViewById(R.id.prize);
        textTel = (TextView) findViewById(R.id.telOwner);
        textOwner = (TextView) findViewById(R.id.postOwner);
        image = (ImageView) findViewById(R.id.imgShow);
        backButton = (ImageView) findViewById(R.id.backbtn);
        backButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycleViewComment);


        loadComments();


         /*Toast.makeText(SingleMissing.this,mPost_key,Toast.LENGTH_SHORT).show();*/

        editTextComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ) {
                    sendComment();
                    /*Toast.makeText(SingleMissing.this,"send by enter",Toast.LENGTH_SHORT).show();*/
                }
                return false;
            }
        });





    }
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap=googleMap;
            databaseReference.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String post_name =(String) dataSnapshot.child("name").getValue();
                String post_breed =(String) dataSnapshot.child("breed").getValue();
                String post_special =(String) dataSnapshot.child("special").getValue();
                String post_prize =(String) dataSnapshot.child("prize").getValue();
                String uid =(String) dataSnapshot.child("uid").getValue();
                final Double latitude = dataSnapshot.child("Lat").getValue(Double.class);
                final Double longitude = dataSnapshot.child("Lng").getValue(Double.class);





                reff.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String post_tel =(String) dataSnapshot.child("phone").getValue();
                        String post_owner =(String) dataSnapshot.child("name").getValue();
                        textTel.setText(post_tel);
                        textOwner.setText(post_owner);
//
//                        Double latitude = dataSnapshot.child("Lat").getValue(Double.class);
//                        Double longitude = dataSnapshot.child("Lng").getValue(Double.class);

                        LatLng location = new LatLng(latitude, longitude);

                        mMap.addMarker((new MarkerOptions().position(location)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                String post_img =(String) dataSnapshot.child("image").getValue();

                textName.setText(post_name);
                textBreed.setText(post_breed);
                textSpecial.setText(post_special);

                textPrize.setText(post_prize);

                Picasso.get().load(post_img).into(image);
            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
    }

    private void loadComments() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);

        commentLit = new ArrayList<>();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postmiss").child(mPost_key).child("comment");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentLit.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren()) {


                    ModelComment modelComment = dss.getValue(ModelComment.class);

                    commentLit.add(modelComment);

                    adapterComment = new AdapterComment(getApplicationContext(), commentLit);

                    recyclerView.setAdapter(adapterComment);


                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendComment() {

        final String comment = editTextComment.getText().toString().trim();
        final String uid = firebaseAuth.getCurrentUser().getUid();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String path = currentUser.getUid();
        Un.child("user").child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String userName =(String) dataSnapshot.child("name").getValue();

                if (imgUri!=null){
                final StorageReference filepath = storageReference.child("MissingDogComment").child(random());
                    filepath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference addComment = databaseReference.child(mPost_key).child("comment").push();

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("image", String.valueOf(uri));
                                addComment.child("image").setValue(hashMap.put("image", String.valueOf(uri)));
                                addComment.child("comment").setValue(comment);
                                addComment.child("uid").setValue(uid);
                                addComment.child("username").setValue(userName);
                            }
                        });
                    }
                });}else{

               /* String userName =(String) dataSnapshot.child("name").getValue();*/

               /* Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());*/

                DatabaseReference addComment = databaseReference.child(mPost_key).child("comment").push();
                addComment.child("comment").setValue(comment);
                addComment.child("uid").setValue(uid);
                addComment.child("username").setValue(userName);}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imgUri = data.getData();
            imgShow.setImageURI(imgUri);

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


    @Override
    public void onClick(View v) {
        if (v == buttonSendComment) {
            sendComment();
        }

        if(v==backButton){
            finish();
        }
    }
    }

