package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SingleAdopt extends AppCompatActivity implements View.OnClickListener {


    public Uri imgUri;


    private static final int MAX_LENGTH = 20;
    private String mPost_key = null;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private DatabaseReference reff;

    private DatabaseReference Un;

    private TextView textName;
    private TextView textBreed;
    private ImageView image;
    private TextView textTel;
    private TextView textOwner;

    private EditText editTextComment;
    private Button buttonSendComment;

    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;

    List<ModelComment> commentLit;
    AdapterComment adapterComment;

    private static final int GALLERY_REQUEST = 1;

    private Button imgBtn;
    private ImageView imgShow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_adopt);

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

        Un = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("postadopt");
        mPost_key = getIntent().getExtras().getString("blog_id");
        reff = FirebaseDatabase.getInstance().getReference().child("user");

        editTextComment = (EditText) findViewById(R.id.commentBox);
        buttonSendComment = (Button) findViewById(R.id.sendButton);
        recyclerView = findViewById(R.id.recycleViewComment);

        buttonSendComment.setOnClickListener(this);

        textName = (TextView) findViewById(R.id.name);
        textBreed = (TextView) findViewById(R.id.breed);
        image = (ImageView) findViewById(R.id.imgShow);
        textTel = (TextView) findViewById(R.id.telOwner);
        textOwner = (TextView) findViewById(R.id.postOwner);

        storageReference = FirebaseStorage.getInstance().getReference();





        loadComments();



        /*Toast.makeText(SingleAdopt.this,post_key,Toast.LENGTH_SHORT).show();*/

        databaseReference.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String post_name =(String) dataSnapshot.child("name").getValue();
                String post_breed =(String) dataSnapshot.child("breed").getValue();
                String post_img =(String) dataSnapshot.child("image").getValue();
                String uid = (String) dataSnapshot.child("uid") .getValue();

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

                textName.setText(post_name);
                textBreed.setText(post_breed);

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

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("postadopt").child(mPost_key).child("comment");
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
                    final StorageReference filepath = storageReference.child("AdoptDogComment").child(random());
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

               /* Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());*/

                DatabaseReference addComment = databaseReference.child(mPost_key).child("comment").push();
                addComment.child("comment").setValue(comment);
                addComment.child("uid").setValue(uid);
                addComment.child("username").setValue(userName);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }





    @Override
    public void onClick(View v) {
        if (v == buttonSendComment) {
            /*Toast.makeText(SingleMissing.this,"send by click",Toast.LENGTH_SHORT).show();*/
            sendComment();
            /*Toast.makeText(SingleMissing.this, "Add Complete", Toast.LENGTH_SHORT).show();*/


        }
    }

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
}
