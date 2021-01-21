package com.example.smnapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PostOnFacebook {
    private static final String TAG1 = "Facebook";
    private static final String FACEBOOK_PAGE_TOKEN = BuildConfig.FacebookPageToken;

    public void postOnFacebook(String message, String filepath) throws JSONException{

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
        /*
        new GraphRequest(
                accessToken,
                "/103616291672657/feed?message=its working",
                null,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                 }
        }
        ).executeAsync();

         */


        Bitmap bmp = BitmapFactory.decodeFile(filepath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();



        Bundle parameters = new Bundle();
       // parameters.putString("url", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        parameters.putString("message", message);
        parameters.putByteArray("picture", byteArray);
        HttpMethod method = HttpMethod.POST;
        new GraphRequest(accessToken, "103616291672657/photos", parameters, method, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                Log.e(TAG1, response.toString());
            }
        }).executeAsync();



    }
}
