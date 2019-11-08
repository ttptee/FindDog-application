package com.example.finddog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class loginJ extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private EditText emailLogin;
    private EditText passwordLogin;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        TextView textgoregister = findViewById(R.id.gotoregister);
        textgoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (loginJ.this,registerJ.class);
                startActivity(i);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
                finish();
                startActivity(new Intent(getApplicationContext(),viewProfile.class));
        }

        emailLogin = (EditText) findViewById(R.id.emaillogin);
        passwordLogin = (EditText) findViewById(R.id.passwordlogin);
        loginButton  = (Button) findViewById(R.id.loginbtn);

        progressDialog = new ProgressDialog(this);

        loginButton.setOnClickListener(this);

    }

    private void userLogin(){
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();


        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging in");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), viewProfile.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            userLogin();
        }
    }
}