package com.example.finddog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//Facebook SDK
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;


public class loginJ extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private EditText emailLogin;
    private EditText passwordLogin;
    private LoginButton loginButtonF;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        emailLogin = (EditText) findViewById(R.id.emaillogin);
        passwordLogin = (EditText) findViewById(R.id.passwordlogin);
        loginButton = (Button) findViewById(R.id.loginbtn);
        loginButtonF = findViewById(R.id.facebook_loginbtn);
        progressDialog = new ProgressDialog(this);

        loginButton.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        loginButtonF.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButtonF.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(loginJ.this,"1",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(loginJ.this,"2",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(loginJ.this,"3",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){

                Toast.makeText(loginJ.this,"user logged out",Toast.LENGTH_LONG).show();

            }
            else
                loaduserProfile(currentAccessToken);
        }
    };

    private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

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
                            startActivity(new Intent(getApplicationContext(),profileJ.class));
                        }
                        else {
                            Toast.makeText(loginJ.this,"Email or Password is incorrect",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        //facebook SDK



    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            userLogin();
        }
    }

    }

