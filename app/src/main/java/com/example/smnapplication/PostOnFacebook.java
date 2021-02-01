package com.example.smnapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import java.io.ByteArrayOutputStream;


public class PostOnFacebook implements Runnable{
    private static final String TAG1 = "Facebook";
    private static final String FACEBOOK_PAGE_TOKEN = BuildConfig.FacebookPageToken;
    private String message;
    private String filepath;

    public PostOnFacebook(@Nullable String message, @Nullable String filepath) {
        this.message = message;
        this.filepath = filepath;
    }

    public void postOnFacebook(){
        //Creating the page access token
        AccessToken accessToken = new AccessToken(FACEBOOK_PAGE_TOKEN,
                "1095990997504855",
                "3900102430001413",
                null,
               null,
                null,
                null,
                null,
                null,
                null);

        //Post request
        Bundle parameters = new Bundle();
        if (message!=null){
            parameters.putString("message", message);
        }

        if (filepath!=null){
            //Decoding filepath
            Bitmap bmp = BitmapFactory.decodeFile(filepath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            parameters.putByteArray("picture", byteArray);
            HttpMethod method = HttpMethod.POST;
            new GraphRequest(accessToken, "103616291672657/photos", parameters, method, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    Log.e(TAG1, response.toString());
                }
            }).executeAsync();
        }
        else{
            HttpMethod method = HttpMethod.POST;
            new GraphRequest(accessToken, "103616291672657/feed", parameters, method, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    Log.e(TAG1, response.toString());
                }
            }).executeAsync();
        }
    }

    @Override
    public void run() {
        postOnFacebook();
    }
}
