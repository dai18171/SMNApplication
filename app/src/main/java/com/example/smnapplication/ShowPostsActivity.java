package com.example.smnapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.AsyncTwitter;
import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;

import static twitter4j.TwitterMethod.SEARCH;

public class ShowPostsActivity extends AppCompatActivity {

    private static final String TAG = "Twitter";
    private static String mediaUrl = "";
    private static String source;
    private static List<Status> retrievedTwitterPosts = new ArrayList<>();
    private ListView showPostsList;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Configuration
        GetConfiguration config = new GetConfiguration();
        config.getConfiguration();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_posts);

        //Get the hashtag im searching posts for
        Intent intent = getIntent();
        String searchingHashtag = intent.getStringExtra("searchingHashtag");
        Log.d(TAG, "The searching hashtag is: " + searchingHashtag);

        //Get Twitter Posts
        retrievePostsFromTwitter(searchingHashtag);


    }


    public void retrievePostsFromTwitter(String searchingHashtag){
        Log.d(TAG, "The method is working");
        //Result
        List<Status> retrievedTwitterPosts = new ArrayList<>();
        List<RetrievedPosts> retrievedPosts = new ArrayList<>();

        showPostsList = findViewById(R.id.postsListView);

        //Async Instance
        GetAsyncTwitterInstance asyncRetrieveInstance = new GetAsyncTwitterInstance();
        AsyncTwitter asyncRetrieveTwitter = asyncRetrieveInstance.getAsyncTwitterInstance();
        asyncRetrieveTwitter.addListener(new TwitterAdapter(){
            @Override
            public void searched(QueryResult queryResult) {
                //Clean the lists and add the results
                retrievedPosts.clear();
                retrievedTwitterPosts.clear();
                retrievedTwitterPosts.addAll(queryResult.getTweets());


                for (Status status: retrievedTwitterPosts){
                    MediaEntity[] media = status.getMediaEntities();
                    if (media.length > 0){
                        mediaUrl = media[0].getMediaURLHttps();
                    }
                    else{
                        mediaUrl = null;
                    }
                    //Create a RetrievedPost object for every Twitter status
                    source = "Twitter";
                    RetrievedPosts post = new RetrievedPosts(source,
                            status.getUser().getName(),
                            status.getUser().getScreenName(),
                            status.getText(),
                            mediaUrl,
                            status.getFavoriteCount(),
                            status.getRetweetCount(),
                            status.getUser().getProfileImageURLHttps());
                    retrievedPosts.add(post);
                }
                //Set custom adapter
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CustomAdapter(ShowPostsActivity.this, R.layout.row, retrievedPosts);
                        showPostsList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });

            }


            //Handle exception
            @Override
            public void onException(TwitterException te, TwitterMethod method) {
                if (method == SEARCH) {
                    te.printStackTrace();
                } else {
                    throw new AssertionError("Should not happen");
                }
            }
        });




        //Search for posts which include the searchingHashtag on Twitter
        Query query = new Query(searchingHashtag);
        query.setCount(100);
        asyncRetrieveTwitter.search(query);

    }
}