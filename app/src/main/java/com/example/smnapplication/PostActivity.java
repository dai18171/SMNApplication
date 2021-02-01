package com.example.smnapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;

import java.util.List;


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
        Button buttonMakeStory = findViewById(R.id.buttonMakeStory);
        Button buttonPickImage = findViewById(R.id.buttonPickImage);
        CheckBox twitterCheckBox = findViewById(R.id.twitterCheckBox);
        CheckBox facebookCheckBox = findViewById(R.id.facebookCheckBox);
        CheckBox instagramCheckBox = findViewById(R.id.instagramCheckBox);

        EditText inputText = findViewById(R.id.inputText);

        imagePreview = findViewById(R.id.imagePreview);

        //Listeners
        buttonMakeStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebookCheckBox.isChecked() && !instagramCheckBox.isChecked()){
                    String type = "image/*";
                    createPostStoryIntent(type, filePath, "com.facebook.katana");
                }
                else if (instagramCheckBox.isChecked() && !facebookCheckBox.isChecked()){
                    InstagramPostStory(filePath);
                }
                else if (instagramCheckBox.isChecked() && facebookCheckBox.isChecked()){
                    InstagramPostStory(filePath);
                }

                Log.d(TAG, "Uploading story");
            }
        });


        buttonMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputText.getText().toString();
                //Cheking which checkboxes are checked
                if (instagramCheckBox.isChecked()) {
                    //Making a post for Instagram
                    InstagramPost(filePath);
                }
                if (twitterCheckBox.isChecked()) {
                    //Making a post for twitter
                    PostOnTwitter aTwitterPost = new PostOnTwitter();
                    aTwitterPost.postOnTwitter(message, file);
                }
                if (facebookCheckBox.isChecked()) {
                    //Making a post for facebook
                    PostOnFacebook aFacebookPost = new PostOnFacebook(message, filePath);
                    Thread thread = new Thread(aFacebookPost);
                    thread.start();
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
                } else {
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
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            //Set image on imagePreview
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
            Log.d(TAG, "Loading image preview");
            String[] filepath = {MediaStore.Images.Media.DATA};
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
        switch (requestCode) {
            case PERMISSION_CODE_IMAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted
                    pickImage();
                    Log.d(TAG, "You have permission to open gallery");
                } else {
                    Toast.makeText(this, "Permission was denied", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "You don't have permission to open gallery");
                }
            }
        }
    }


    private void createPostStoryIntent(String type, String mediaPath, String pName) {

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);
        if (pName != null)
            share.setPackage(pName);

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

    public void InstagramPostStory(String mediaPath){
        Intent myPhoto = new Intent("com.instagram.share.ADD_TO_STORY");
        // Create the URI from the media
        File media = new File(mediaPath);
        //Uri uri = Uri.fromFile(media);
        Uri imageUri = FileProvider.getUriForFile(
                PostActivity.this,
                "com.example.smnapplication.provider",
                media);
        myPhoto.setPackage("com.instagram.android");
        myPhoto.setType("image/*");
        myPhoto.setDataAndType(imageUri, "image/*");
        myPhoto.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        myPhoto.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(myPhoto);
    }

    public void InstagramPost(String mediaPath){
        Intent myPhoto = new Intent("com.instagram.share.ADD_TO_FEED");
        // Create the URI from the media
        File media = new File(mediaPath);
        //Uri uri = Uri.fromFile(media);
        Uri imageUri = FileProvider.getUriForFile(
                PostActivity.this,
                "com.example.smnapplication.provider",
                media);
        myPhoto.setPackage("com.instagram.android");
        myPhoto.setType("image/*");
        myPhoto.setDataAndType(imageUri, "image/*");
        myPhoto.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        myPhoto.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(myPhoto);
    }
}