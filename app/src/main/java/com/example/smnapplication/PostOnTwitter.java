package com.example.smnapplication;

import android.util.Log;
import android.widget.Toast;

import java.io.File;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterMethod;
import twitter4j.UploadedMedia;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import static twitter4j.TwitterMethod.UPDATE_STATUS;

public class PostOnTwitter {


    private static final String TAG = "Twitter";


    public void postOnTwitter(String message, File file){
        GetConfiguration config = new GetConfiguration();
        config.getConfiguration();
        AsyncTwitterFactory factory = new AsyncTwitterFactory();
        AsyncTwitter twitter = factory.getInstance();
        twitter.setOAuthConsumer("Tm4gZfXCRucNY22urfG8pbr5a", "07KU1IObGDiRDdkXdjGwoa865aOvXPSrgz3avgh1XUSNmMLAgq");
        twitter.setOAuthAccessToken(new AccessToken("1328369885362196486-TSlqt87Syy7l478N98pNYuJ1VN9HiP","2tXCV46OV78xyaz2hwtMCmHgOmPeayeYr6phl3Tx2fw7x"));
        twitter.addListener(new TwitterAdapter(){
            @Override public void updatedStatus(Status status) {
                Log.d(TAG, "The post is up");
            }

            @Override public void onException(TwitterException e, TwitterMethod method) {
                if (method == UPDATE_STATUS) {
                    e.printStackTrace();
                } else {
                    throw new AssertionError("Should not happen");
                }
            }
        });
        StatusUpdate status = new StatusUpdate(message);
        status.setMedia(file);
        twitter.updateStatus(status);
    }


}
