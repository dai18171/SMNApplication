package com.example.smnapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String FACEBOOK_PAGE_TOKEN = BuildConfig.FacebookPageToken;

    private static final String TAG = "Twitter";
    private static final String TAG1 = "Facebook";
    private static String mediaUrl = "";
    private static String source;
    private ListView showPostsList;
    private CustomPostsAdapter adapter;

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
        retrievePostsFromInstagramAndTwitter(searchingHashtag);
    }


    public void retrievePostsFromTwitter(String searchingHashtag, List<RetrievedPosts> retrievedPosts){
        Log.d(TAG, "The method is working");
        //Result
        List<Status> retrievedTwitterPosts = new ArrayList<>();
        //List<RetrievedPosts> retrievedPosts = new ArrayList<>();


        showPostsList = findViewById(R.id.postsListView);

        //Async Instance
        GetAsyncTwitterInstance asyncRetrieveInstance = new GetAsyncTwitterInstance();
        AsyncTwitter asyncRetrieveTwitter = asyncRetrieveInstance.getAsyncTwitterInstance();
        asyncRetrieveTwitter.addListener(new TwitterAdapter(){

            @Override
            public void searched(QueryResult queryResult) {
                //Clean the lists and add the results
                //retrievedPosts.clear();
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
                            0,
                            status.getUser().getProfileImageURLHttps());
                    retrievedPosts.add(post);
                }
                //Set custom adapter with the list of posts from both social media networks
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CustomPostsAdapter(ShowPostsActivity.this, R.layout.row, retrievedPosts);
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
        query.setCount(20);
        asyncRetrieveTwitter.search(query);

    }

    //This method retrieves the posts which include a certain hashtag from Instagram and Twitter
    public void retrievePostsFromInstagramAndTwitter(String searchingHashtag){

        showPostsList = findViewById(R.id.postsListView);
        List<RetrievedPosts> retrievedPosts = new ArrayList<>();
        //The graph api doesn't work with "#"
        String finalSearchingHashtag = searchingHashtag.replace("#","");
        //Creating the page access token
        com.facebook.AccessToken accessToken = new AccessToken(FACEBOOK_PAGE_TOKEN,
                "1095990997504855",
                "3900102430001413",
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        Bundle parameters = new Bundle();
        parameters.putString("user_id", "17841445768120860");
        parameters.putString("q", finalSearchingHashtag);
        HttpMethod method = HttpMethod.GET;
        //Finding the hashtag's id
        new GraphRequest(accessToken, "/ig_hashtag_search", parameters, method, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                //Handling response
                String id = null;
                JSONObject jsonObject = response.getJSONObject();
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1 = data.getJSONObject(0);
                    id = jsonObject1.getString("id");
                    Log.d(TAG1, id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Creating the graph path and putting the parameters
                Bundle parameters = new Bundle();
                StringBuilder path = new StringBuilder();
                path.append(id);
                path.append("/top_media");
                parameters.putString("fields", "media_type,media_url,like_count,comments_count,caption,timestamp");
                parameters.putString("user_id", "17841445768120860");
                HttpMethod method = HttpMethod.GET;
                //Searching for the most popular posts which inlude the hashtag id on Instagram
                new GraphRequest(accessToken, path.toString(), parameters, method, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        retrievedPosts.clear();
                        String source = "Instagram";
                        String media_type;
                        String media_url;
                        String caption;
                        int likesCount;
                        int commentsCount;
                        //Handling response
                        JSONObject postsObject = response.getJSONObject();
                        try {
                            JSONArray postsData = postsObject.getJSONArray("data");
                            for (int i=0; i<postsData.length(); i++){
                                media_type = postsData.getJSONObject(i).getString("media_type");
                                //Checking only for Images
                                if (media_type.equals("IMAGE")){
                                    media_url = postsData.getJSONObject(i).getString("media_url");
                                    caption = postsData.getJSONObject(i).getString("caption");
                                    likesCount = postsData.getJSONObject(i).getInt("like_count");
                                    commentsCount = postsData.getJSONObject(i).getInt("comments_count");
                                    //Creating a RetrievedPost object for every Instagram post
                                    RetrievedPosts post = new RetrievedPosts(source,
                                            "somebody",
                                            "sexy",
                                            caption,
                                            media_url,
                                            likesCount,
                                            0,
                                            commentsCount,
                                            null);
                                    //Log.d(TAG1, post.getContentImageUrl() + " " + post.getContent() + " " + post.getLikesCount() + " " + post.getCommentsCount() + " " + media_type);
                                    retrievedPosts.add(post);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Searching for the most popular posts which inlude the hashtag id on Twitter
                        retrievePostsFromTwitter(searchingHashtag, retrievedPosts);
                    }
                }).executeAsync();
            }
        }).executeAsync();
    }

}