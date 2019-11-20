package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SingleMissing extends AppCompatActivity implements View.OnClickListener {
    private String mPost_key = null;
    private DatabaseReference databaseReference;
    private DatabaseReference reff;

    private DatabaseReference Un;

    private TextView textName;
    private TextView textBreed;
    private TextView textSpecial;
    private TextView textDatetime;
    private TextView textPrize;
    private TextView textTel;
    private TextView textOwner;
    private ImageView image;


    private EditText editTextComment;
    private Button buttonSendComment;

    private FirebaseAuth firebaseAuth;

    /*private final Object timestamp = ServerValue.TIMESTAMP;*/

    RecyclerView recyclerView;

    List<ModelComment> commentLit;
    AdapterComment adapterComment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_missing);




        Un = FirebaseDatabase.getInstance().getReference();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");
        mPost_key = getIntent().getExtras().getString("blog_id");
        reff = FirebaseDatabase.getInstance().getReference().child("user");


        firebaseAuth = FirebaseAuth.getInstance();

        editTextComment = (EditText) findViewById(R.id.commentBox);
        buttonSendComment = (Button) findViewById(R.id.sendButton);

        buttonSendComment.setOnClickListener(this);

        textName = (TextView) findViewById(R.id.name);
        textBreed = (TextView) findViewById(R.id.breed);
        textSpecial = (TextView) findViewById(R.id.special);
        textDatetime = (TextView) findViewById(R.id.datetime);
        textPrize = (TextView) findViewById(R.id.prize);
        textTel = (TextView) findViewById(R.id.telOwner);
        textOwner = (TextView) findViewById(R.id.postOwner);
        image = (ImageView) findViewById(R.id.imgShow);

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

                String userName =(String) dataSnapshot.child("name").getValue();

               /* Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());*/

                DatabaseReference addComment = databaseReference.child(mPost_key).child("comment").push();
                addComment.child("comment").setValue(comment);
                addComment.child("uid").setValue(uid);
                addComment.child("username").setValue(userName);

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

}
