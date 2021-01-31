package com.example.smnapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookGraphResponseException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.appevents.internal.AppEventsLoggerUtility;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final List<String> trendingHashtags = new ArrayList<String>();

    private static final String TAG = "Twitter";
    private static final String TAG1 = "Facebook";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Switch Activities Buttons
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonPost = findViewById(R.id.buttonPost);

        //Listeners
        buttonSearch.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    //Switching activities
    @Override
    public void onClick(View v) {
        Intent intent = null;

        if (v.getId()==R.id.buttonPost){
            Log.d(TAG, "Entering posting activity");
            intent = new Intent(this, PostActivity.class);
        }
        else if (v.getId()==R.id.buttonSearch){
            Log.d(TAG, "Entering sharing activity");
            intent = new Intent(this, SearchActivity.class);
        }

        if (intent!=null){
            startActivity(intent);
        }
    }


}