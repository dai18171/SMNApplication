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
                .setOAuthConsumerKey("Tm4gZfXCRucNY22urfG8pbr5a")
                .setOAuthConsumerSecret("07KU1IObGDiRDdkXdjGwoa865aOvXPSrgz3avgh1XUSNmMLAgq")
                .setOAuthAccessToken("1328369885362196486-TSlqt87Syy7l478N98pNYuJ1VN9HiP")
                .setOAuthAccessTokenSecret("2tXCV46OV78xyaz2hwtMCmHgOmPeayeYr6phl3Tx2fw7x");
        Log.d(TAG, "Successful Configuration");
    }
}
