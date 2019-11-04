package com.example.finddog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;

public class detaildogJava extends Activity implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText editTextName;
    private EditText editTextBreed;
    private EditText editTextSpecial;
    private EditText editTextDateTime;
    private EditText editTextPrize;

    private Button buttonSaveDetail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaildog);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"Please Login.",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, loginJ.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText)findViewById(R.id.name);
        editTextBreed = (EditText)findViewById(R.id.breed);
        editTextSpecial = (EditText)findViewById(R.id.special);
        editTextDateTime = (EditText)findViewById(R.id.datetime);
        editTextPrize = (EditText)findViewById(R.id.prize);
        buttonSaveDetail = (Button)findViewById(R.id.detaildogsubmit);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonSaveDetail.setOnClickListener(this);




    }


    private void saveDetail() {
        String name = editTextName.getText().toString().trim();
        String breed = editTextBreed.getText().toString().trim();
        String special = editTextSpecial.getText().toString().trim();
        String datetime = editTextDateTime.getText().toString().trim();
        String prize = editTextPrize.getText().toString().trim();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DetailMissing detailMissing = new DetailMissing(uid,name,breed,special,datetime,prize);

        databaseReference.child("postmiss").push().setValue(detailMissing);

        Toast.makeText(this, "Post created..", Toast.LENGTH_SHORT).show();





    }


    @Override
    public void onClick(View v) {
        if (v == buttonSaveDetail){
            saveDetail();
            finish();
            startActivity(new Intent(this, AllMissingPost.class));
        }
    }


}
