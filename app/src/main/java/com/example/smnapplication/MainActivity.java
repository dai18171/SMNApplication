package com.example.smnapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Trends;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;
import twitter4j.auth.AccessToken;

import static twitter4j.TwitterMethod.PLACE_TRENDS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final List<String> trendingHashtags = new ArrayList<String>();

    private static final String TAG = "Twitter";
    //Just switching between activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Switch Activities Buttons
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonPost = findViewById(R.id.buttonPost);

        buttonSearch.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        if (v.getId()==R.id.buttonPost){
            Log.d(TAG, "Entered posting activity");
            intent = new Intent(this, PostActivity.class);
        }
        else if (v.getId()==R.id.buttonSearch){
            Log.d(TAG, "Entered sharing activity");
            intent = new Intent(this, SearchActivity.class);
        }

        if (intent!=null){
            startActivity(intent);
        }
    }
}