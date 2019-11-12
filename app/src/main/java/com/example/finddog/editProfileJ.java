package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editProfileJ extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView userEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;


    private EditText editTextName, editTextPhone;
    private Button buttonSaveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, loginJ.class));
        }

//        FirebaseUser checkUser = firebaseAuth.getCurrentUser();
//        DatabaseReference  checkId =  databaseReference.child("user").child(checkUser.getUid());
//        if (databaseReference.child("user").child(checkUser.getUid()) == checkId){
//            finish();
//            startActivity(new Intent(this, detaildogJava.class));
//        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        buttonSaveInfo = (Button) findViewById(R.id.buttonSaveInfo);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        userEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.logoutBtn);

        userEmail.setText("Welcome "+user.getEmail());
        buttonLogout.setOnClickListener(this);
        buttonSaveInfo.setOnClickListener(this);






    }

    private void saveUserInformation(){
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name,phone);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child("user").child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information Saved..", Toast.LENGTH_SHORT).show();


    }





    @Override
    public void onClick(View v) {
        if(v == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, loginJ.class));
        }

        if (v == buttonSaveInfo){
            saveUserInformation();
            finish();
            startActivity(new Intent(this, viewProfile.class));
        }



    }
}
