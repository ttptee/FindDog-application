package com.example.finddog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registerJ extends Activity {
    private EditText emailTV, passwordTV;
    private Button registerbtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth mAuth;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        registerbtn = (Button) findViewById(R.id.registerbtn) ;
        emailTV = (EditText) findViewById(R.id.email);
        passwordTV = (EditText) findViewById(R.id.password) ;
       progressDialog = new ProgressDialog(this);
       firebaseAuth = FirebaseAuth.getInstance();


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    registerUser();


            }
        });
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
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(registerJ.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(registerJ.this,"Could not register.. please try again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


}

}





