package com.example.smnapplication;

import android.util.Log;

import twitter4j.conf.ConfigurationBuilder;

public class GetConfiguration {

    public static final String TAG = "Twitter";

    public void getConfiguration(){
        Log.d(TAG, "Configuration...");
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setUserStreamRepliesAllEnabled(true);
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("TWITTER_API_KEY")
                .setOAuthConsumerSecret("TWITTER_SECRET_KEY")
                .setOAuthAccessToken("TWITTER_TOKEN")
                .setOAuthAccessTokenSecret("TWITTER_SECRET_TOKEN");
        Log.d(TAG, "Successful Configuration");
    }
}
