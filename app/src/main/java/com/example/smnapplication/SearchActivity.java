package com.example.smnapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterMethod;
import twitter4j.auth.AccessToken;

import static twitter4j.TwitterMethod.PLACE_TRENDS;
import static twitter4j.TwitterMethod.UPDATE_STATUS;

public class SearchActivity extends AppCompatActivity {

    private static final List<String> trendingHashtags = new ArrayList<String>();
    private static final String TAG = "Twitter";
    private ListView hashtagList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Views
        Button buttonSearchHashtag = findViewById(R.id.buttonSearchHashtag);
        Button fillTrendingsButton = findViewById(R.id.fillTrendingsButton);
        TextView searchTxt = findViewById(R.id.searchTxt);
        hashtagList = findViewById(R.id.hashtagList);

        //Configuration
        GetConfiguration config = new GetConfiguration();
        config.getConfiguration();

        buttonSearchHashtag.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


            }
        });

        fillTrendingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SearchActivity.this, "Retrieving trending hashtags...", Toast.LENGTH_SHORT).show();

                //Async instance
                GetAsyncTwitterInstance asyncInstance = new GetAsyncTwitterInstance();
                AsyncTwitter asyncSearchTwitter = asyncInstance.getAsyncTwitterInstance();

                //Getting trends worldwide
                asyncSearchTwitter.addListener(new TwitterAdapter(){
                    //Handle trending hashtags
                    @Override
                    public void gotPlaceTrends(Trends trends) {
                        //Clean the list whenever the button is pressed so i can have the new data
                        trendingHashtags.clear();
                        Log.d(TAG, "Retrieving trends was successful.");
                        int i;
                        for (i=0; i<trends.getTrends().length; i++){
                            if (trends.getTrends()[i].getName().startsWith("#"))
                            {
                                Log.d(TAG, trends.getTrends()[i].getName());
                                trendingHashtags.add(trends.getTrends()[i].getName());
                            }
                        }

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //Setting Adapter for the ListView
                                adapter =  new ArrayAdapter<String>(SearchActivity.this, R.layout.trends_detail, R.id.txtHashtag, trendingHashtags);
                                hashtagList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });

                        Log.d(TAG, "The trendings list is set");
                    }

                    @Override public void onException(TwitterException e, TwitterMethod method) {
                        if (method == PLACE_TRENDS) {
                            e.printStackTrace();
                        } else {
                            throw new AssertionError("Should not happen");
                        }
                    }
                });

                //Getting trendings
                asyncSearchTwitter.getPlaceTrends(1);

            }
        });


    }


}