package com.example.smnapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "Twitter";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_CODE_IMAGE = 1000;
    private static File file;

    ImageView imagePreview;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Views (Buttons, Text etc.)
        Button buttonMakePost = findViewById(R.id.buttonMakePost);
        Button buttonPickImage = findViewById(R.id.buttonPickImage);

        EditText inputText = findViewById(R.id.inputText);

        imagePreview = findViewById(R.id.imagePreview);

        //Configuration
        GetConfiguration config = new GetConfiguration();
        config.getConfiguration();

        //Listeners
        buttonMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Making a post for twitter
                String message = inputText.getText().toString();
                PostOnTwitter aTwitterPost = new PostOnTwitter();
                aTwitterPost.postOnTwitter(message, file);

                //Showing progress
                Toast.makeText(PostActivity.this, "Uploading your post.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Uploading the post");
            }
        });

        buttonPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check runtime permission
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                    //Permission not granted, you need to request
                    Log.d(TAG, "Permission not granted. You need to request.");
                    String permissions[] = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    //Pop-up
                    requestPermissions(permissions, PERMISSION_CODE_IMAGE);
                }
                else{
                    //Permission already granted
                    Log.d(TAG, "Permission is already granted.");
                    pickImage();
                }
            }
        });
    }

    public void pickImage() {
        //Intent to pick image
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, RESULT_LOAD_IMAGE);
        Log.d(TAG, "Opening gallery..");
    }

    //Handle result of pick image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE){
            //Set image on imagePreview
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
            Log.d(TAG, "Loading image preview");
            String[] filepath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(imageUri, filepath, null,
                    null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filepath[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            file = new File(filePath);
            Log.d(TAG, "File seems fine");
        }

    }

    //Handle result of permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE_IMAGE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission was granted
                    pickImage();
                    Log.d(TAG, "You have permission to open gallery");
                }
                else{
                    Toast.makeText(this, "Permission was denied", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "You don't have permission to open gallery");
                }
            }
        }
    }
}