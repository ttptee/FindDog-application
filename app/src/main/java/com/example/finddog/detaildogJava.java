package com.example.finddog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;

import androidx.annotation.Nullable;

public class detaildogJava extends Activity implements View.OnClickListener{
    private static final int MAX_LENGTH = 20;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText editTextName;
    private EditText editTextBreed;
    private EditText editTextSpecial;
    private EditText editTextDateTime;
    private EditText editTextPrize;

    private Button buttonSaveDetail;

    private ImageButton selectImage;
    private static final int GALLERY_REQUEST = 1;

    private Uri imgUri= null;

    private StorageReference storageReference;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detaildog);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("postmiss");

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"Please Login.",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, loginJ.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        selectImage = (ImageButton) findViewById(R.id.btnAddImg);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });


        editTextName = (EditText)findViewById(R.id.name);
        editTextBreed = (EditText)findViewById(R.id.breed);
        editTextSpecial = (EditText)findViewById(R.id.special);
        editTextDateTime = (EditText)findViewById(R.id.datetime);
        editTextPrize = (EditText)findViewById(R.id.prize);
        buttonSaveDetail = (Button)findViewById(R.id.detaildogsubmit);


        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonSaveDetail.setOnClickListener(this);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            imgUri = data.getData();
            selectImage.setImageURI(imgUri);

        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void saveDetail() {
        final String name = editTextName.getText().toString().trim();
        final String breed = editTextBreed.getText().toString().trim();
        final String special = editTextSpecial.getText().toString().trim();
        final String datetime = editTextDateTime.getText().toString().trim();
        final String prize = editTextPrize.getText().toString().trim();
        final String uid = firebaseAuth.getCurrentUser().getUid();




        final StorageReference filepath = storageReference.child("MissingDogImg").child(random());
        filepath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DatabaseReference newPost = databaseReference.child("postmiss").push();

                        HashMap<String,String> hashMap = new HashMap<>();

                        hashMap.put("image", String.valueOf(uri));
                        newPost.child("image").setValue(hashMap.put("image", String.valueOf(uri)));
                        newPost.child("name").setValue(name);
                        newPost.child("breed").setValue(breed);
                        newPost.child("special").setValue(special);
                        newPost.child("datetime").setValue(datetime);
                        newPost.child("prize").setValue(prize);
                        newPost.child("uid").setValue(uid);







                    }
                });

            }
        });





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
