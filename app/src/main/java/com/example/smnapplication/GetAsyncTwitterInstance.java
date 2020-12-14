package com.example.smnapplication;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.auth.AccessToken;

public class GetAsyncTwitterInstance {

    private static final String TWITTER_API_KEY = BuildConfig.TwitterApiKey;
    private static final String TWITTER_SECRET_KEY = BuildConfig.TwitterSecretKey;
    private static final String TWITTER_TOKEN = BuildConfig.TwitterToken;
    private static final String TWITTER_SECRET_TOKEN = BuildConfig.TwitterSecretToken;

    public AsyncTwitter getAsyncTwitterInstance(){
        //Asynchronous twitter instance
        AsyncTwitterFactory tf = new AsyncTwitterFactory();
        AsyncTwitter asyncTwitter = tf.getInstance();
        asyncTwitter.setOAuthConsumer(TWITTER_API_KEY, TWITTER_SECRET_KEY);
        asyncTwitter.setOAuthAccessToken(new AccessToken(TWITTER_TOKEN,TWITTER_SECRET_TOKEN));

        return asyncTwitter;
    }
}
