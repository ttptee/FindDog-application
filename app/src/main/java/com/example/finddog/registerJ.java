package com.example.finddog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registerJ extends Activity implements View.OnClickListener {
    private EditText emailTV, passwordTV,confirmpasswordTV;
    private Button registerbtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private ImageView backButton;

    private FirebaseAuth mAuth;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        registerbtn = (Button) findViewById(R.id.registerbtn) ;
        emailTV = (EditText) findViewById(R.id.email);
        passwordTV = (EditText) findViewById(R.id.password) ;
        confirmpasswordTV = (EditText) findViewById(R.id.confirmpassword) ;
       progressDialog = new ProgressDialog(this);
       firebaseAuth = FirebaseAuth.getInstance();
       backButton=(ImageView) findViewById(R.id.backbtn);
        backButton.setOnClickListener(this);
        registerbtn.setOnClickListener(this);




//
//        registerbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    registerUser();
//
//
//            }
//        });
//
//        backButton.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        }));

    }
private void registerUser(){
        String email = emailTV.getText().toString().trim();
        String password = passwordTV.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        validate();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (firebaseAuth.getCurrentUser() != null)
                                firebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(getApplicationContext(), loginJ.class));
                            Toast.makeText(registerJ.this,"Register Successfully",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(registerJ.this,"Could not register.. please try again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


}

    private boolean validate() {
        boolean temp=true;
        String pass=passwordTV.getText().toString();
        String cpass=confirmpasswordTV.getText().toString();
        if(!pass.equals(cpass)){
            Toast.makeText(registerJ.this,"Password Not matching",Toast.LENGTH_SHORT).show();
            temp=false;
            finish();
            startActivity(new Intent(getApplicationContext(), registerJ.class));
        }
        return temp;
    }




    public void onClick(View v) {
        if (v == registerbtn) {
            registerUser();
        }
        if(v==backButton){
            finish();

        }
    }


}







