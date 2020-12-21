package com.example.smnapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.AsyncTwitter;
import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Trends;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;

import static twitter4j.TwitterMethod.PLACE_TRENDS;
import static twitter4j.TwitterMethod.SEARCH;

public class SearchActivity extends AppCompatActivity {

    private static final List<String> trendingHashtags = new ArrayList<String>();
    private static final List<String> keywordHashtags = new ArrayList<String>();
    private static final String TAG = "Twitter";
    private ListView hashtagList;
    private TextView searchTxt;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Views
        Button buttonSearchHashtag = findViewById(R.id.buttonSearchHashtag);
        Button fillTrendingsButton = findViewById(R.id.fillTrendingsButton);
        searchTxt = findViewById(R.id.searchTxt);
        hashtagList = findViewById(R.id.hashtagList);


        //Listeners
        //Button for getting keyword hashtagas
        buttonSearchHashtag.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Get the keyword to search
                String keyword = searchTxt.getText().toString();
                Toast.makeText(SearchActivity.this, "Retrieving hashtags with "+ keyword + "...", Toast.LENGTH_SHORT).show();

                //Async Instance
                GetAsyncTwitterInstance asyncKeywordInstance = new GetAsyncTwitterInstance();
                AsyncTwitter asyncKeywordTwitter = asyncKeywordInstance.getAsyncTwitterInstance();

                //Get hashtags with keyword in them
                asyncKeywordTwitter.addListener(new TwitterAdapter(){

                    //Handling Exception
                    @Override
                    public void onException(TwitterException te, TwitterMethod method) {
                        if (method == SEARCH) {
                            te.printStackTrace();
                        } else {
                            throw new AssertionError("Should not happen");
                        }
                    }

                    @Override
                    public void searched(QueryResult queryResult) {
                        //Clean the list whenever the button is pressed so i can have the new data
                        keywordHashtags.clear();

                        Log.d(TAG, "Retrieving hashtags with keyword was successful");

                        //Get hashtags via Tweets by using HashtagEntity
                        for(twitter4j.Status status : queryResult.getTweets()){
                            HashtagEntity[] hte = status.getHashtagEntities();
                            for(HashtagEntity hashtag : hte){
                                String hashtagWord = hashtag.getText().toLowerCase();
                                if(hashtagWord.contains(keyword) && !hashtagWord.equals(keyword)){
                                    if (!keywordHashtags.contains("#"+hashtagWord))
                                    {
                                        keywordHashtags.add("#"+hashtagWord);
                                        Log.d(TAG, hashtag.getText() );
                                    }

                                }
                            }
                        }



                        //Using the UiThread so i can change the listview
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //Setting Adapter for the ListView
                                adapter =  new ArrayAdapter<String>(SearchActivity.this, R.layout.trends_detail, R.id.txtHashtag, keywordHashtags);
                                hashtagList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "Adapter is set on ListView");
                            }
                        });
                    }
                });


                //Creating the query for hashtags and search
                Query query = new Query("#" + keyword + " " + keyword);
                query.setCount(100);
                asyncKeywordTwitter.search(query);
            }
        });

        //Button for getting trending hashtags
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

                        //Using the UiThread so i can change the listview
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //Setting Adapter for the ListView
                                adapter =  new ArrayAdapter<String>(SearchActivity.this, R.layout.trends_detail, R.id.txtHashtag, trendingHashtags);
                                hashtagList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "Adapter is set on ListView");
                            }
                        });

                        Log.d(TAG, "The trendings list is set");
                    }

                    //Handling Exception
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

        //Handle click event in ListView
        hashtagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                Log.d(TAG, "Entering showing posts activity");

                //Hashtag in the clicked item
                String hashtagToSearch = adapter.getItem(position);

                //Start showing posts activity
                intent = new Intent(SearchActivity.this, ShowPostsActivity.class);
                intent.putExtra("searchingHashtag", hashtagToSearch);
                if (intent != null){
                    startActivity(intent);
                }
            }
        });



    }


}