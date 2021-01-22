package com.example.smnapplication;

import android.util.Log;

import twitter4j.conf.ConfigurationBuilder;

public class GetConfiguration {

    public static final String TAG = "Twitter";
    private static final String TWITTER_API_KEY = BuildConfig.TwitterApiKey;
    private static final String TWITTER_SECRET_KEY = BuildConfig.TwitterSecretKey;
    private static final String TWITTER_TOKEN = BuildConfig.TwitterToken;
    private static final String TWITTER_SECRET_TOKEN = BuildConfig.TwitterSecretToken;



    public void getConfiguration(){
        Log.d(TAG, "Configuration...");
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setUserStreamRepliesAllEnabled(true);
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_API_KEY)
                .setOAuthConsumerSecret(TWITTER_SECRET_KEY)
                .setOAuthAccessToken(TWITTER_TOKEN)
                .setOAuthAccessTokenSecret(TWITTER_SECRET_TOKEN);
        cb.setIncludeEntitiesEnabled(true);
        Log.d(TAG, "Successful Configuration");
    }
}
