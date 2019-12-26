package com.example.finddog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLRemoteModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class showPicAndML extends AppCompatActivity {
    private static final String TAG = "ODAutoMLILProcessor";
    public EditText inputBreedEdittext;
    public String text;
    public TextView Predict;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic_and_ml);
        Predict = (TextView) findViewById(R.id.textViewPredict);
        Button Gocamera = (Button)findViewById(R.id.camerabtn);
         imageView = findViewById(R.id.showPic);

        Gocamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
                Predict.setText("Image Error please try again");
            }
        });
        Button GoGallery = findViewById(R.id.importpicbtn);
        GoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intentGoGal = new Intent(Intent.ACTION_GET_CONTENT);
                intentGoGal.setType("image/*");
                startActivityForResult(Intent.createChooser(intentGoGal
                        , "Select photo from"), 1);*/
                Intent intentGoGal = new Intent(Intent.ACTION_GET_CONTENT);
                intentGoGal.setType("image/*");
                startActivityForResult(intentGoGal, 1);

            }
        });
        Button goCheckpostandBreedbtn = findViewById(R.id.submitBreed);
        goCheckpostandBreedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGOcheckpage = new Intent(showPicAndML.this,checkPostBreed.class);
                inputBreedEdittext =findViewById(R.id.inputBreed);
                String BreedEdittext = inputBreedEdittext.getText().toString().trim();
                SharedPreferences sp = getSharedPreferences("BreedText", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (BreedEdittext.isEmpty()){
                editor.putString("BreedMLText",text);
                editor.commit();}

                else {
                    editor.putString("BreedMLText",BreedEdittext);
                    editor.commit();
                }

                startActivity(intentGOcheckpage);
            }
        });
    }
//////////////////////////////

public void onActivityResult(int requestCode, int resultCode
        , Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == RESULT_OK) {
        Uri uri = data.getData();
        Predict.setText("Image Error please try again");
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(showPicAndML.this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        imageFromBitmap(bitmap);

    }
    else {

        Log.e(TAG, "go back " );

           /* super.onActivityResult(requestCode, resultCode, data);*/

           /* Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            imageFromBitmap(bitmap);


            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);*/
    }
}
    /////////////////////////////////////////
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//        imageView.setImageBitmap(bitmap);
//        imageFromBitmap(bitmap);
//
//
//        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);



    //}

    ///////////////////////////////////////////
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getRotationCompensation(String cameraId, Activity activity, Context context)
            throws CameraAccessException {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // On most devices, the sensor orientation is 90 degrees, but for some
        // devices it is 270 degrees. For devices with a sensor orientation of
        // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
        CameraManager cameraManager = (CameraManager) context.getSystemService(CAMERA_SERVICE);
        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360;

        // Return the corresponding FirebaseVisionImageMetadata rotation value.
        int result;
        switch (rotationCompensation) {
            case 0:
                result = FirebaseVisionImageMetadata.ROTATION_0;
                break;
            case 90:
                result = FirebaseVisionImageMetadata.ROTATION_90;
                break;
            case 180:
                result = FirebaseVisionImageMetadata.ROTATION_180;
                break;
            case 270:
                result = FirebaseVisionImageMetadata.ROTATION_270;
                break;
            default:
                result = FirebaseVisionImageMetadata.ROTATION_0;
                Log.e(TAG, "Bad rotation value: " + rotationCompensation);
        }
        return result;

    }
    private void imageFromBitmap(Bitmap bitmap) {
        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("AutoML/manifest.json")
                .build();
        // [START image_from_bitmap]
        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        // [END image_from_bitmap]
        FirebaseVisionImageLabeler labeler;
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.5f)  // Evaluate your model in the Firebase console
                            // to determine an appropriate value.
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);

            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            // Task completed successfully
                            // ...


                            for (FirebaseVisionImageLabel label: labels) {
                                 text = label.getText();
                                String text2 = label.getEntityId();
                                float confidence = label.getConfidence();


                                Log.d(TAG, "Text: "+text);
                                Predict.setText(text+" "+confidence*100+"%");







                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                            Log.d(TAG, "onFailure: ");
                            TextView Predict = (TextView) findViewById(R.id.textViewPredict);
                            Predict.setText("Predict failed pls try again");

                        }
                    });
        } catch (FirebaseMLException e) {
            // ...


        }
        /////////////////////

    }


}







