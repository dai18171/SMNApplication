package com.example.smnapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;

import static androidx.core.content.ContextCompat.startActivity;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "Twitter";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_CODE_IMAGE = 1000;
    private static String filePath;
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
        CheckBox twitterCheckBox = findViewById(R.id.twitterCheckBox);
        CheckBox facebookCheckBox = findViewById(R.id.facebookCheckBox);
        CheckBox instagramCheckBox = findViewById(R.id.instagramCheckBox);

        EditText inputText = findViewById(R.id.inputText);

        imagePreview = findViewById(R.id.imagePreview);

        //Listeners
        buttonMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputText.getText().toString();
                //Cheking which checkboxes are checked
                if (twitterCheckBox.isChecked()){
                    //Making a post for twitter
                    PostOnTwitter aTwitterPost = new PostOnTwitter();
                    aTwitterPost.postOnTwitter(message, file);
                }
                if (facebookCheckBox.isChecked()){
                    //Making a post for facebook
                    PostOnFacebook aFacebookPost = new PostOnFacebook();
                    aFacebookPost.postOnFacebook(message, filePath);
                }
                if (instagramCheckBox.isChecked()) {
                    //Making a post for Instagram
                    String type = "image/*";
                    createInstagramIntent(type, filePath);
                }
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
            filePath = cursor.getString(columnIndex);
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

    //Intent for posting on Instagram as its given in the documentation
    private void createInstagramIntent(String type, String mediaPath){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        //Uri uri = Uri.fromFile(media);
        Uri imageUri = FileProvider.getUriForFile(
                PostActivity.this,
                "com.example.smnapplication.provider",
                media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, imageUri);

        // Broadcast the Intent.
        //I dont really know why this works :)
        Intent chooser = Intent.createChooser(share, "Share to");

        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivity(chooser);
    }
}