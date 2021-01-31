package com.example.smnapplication;


import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLEngineResult;

import twitter4j.Status;


/*
This is a general class to handle the different posts coming from Twitter,
Instagram, Facebook as one entity and put them in a general List.
 */
public class RetrievedPosts implements Serializable {
    private final String source;
    private final String author;
    private final String username;
    private final String content;
    private final String profileUrl;
    private final String contentImageUrl;
    private int likesCount =  0;
    private int sharesCount = 0;
    private int commentsCount = 0;

    public RetrievedPosts(String source, @Nullable String author, @Nullable String username, String content, @Nullable String contentImageUrl, int likesCount, int sharesCount, int commentsCount, @Nullable String profileUrl) {
        this.source = source;
        this.author = author;
        this.username = username;
        this.content = content;
        this.likesCount = likesCount;
        this.sharesCount = sharesCount;
        this.commentsCount = commentsCount;
        this.profileUrl = profileUrl;
        this.contentImageUrl = contentImageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return ""+content;
    }

    public String getLikesCount() {
        return ""+likesCount;
    }

    public String getSharesCount() {
        return ""+sharesCount;
    }

    public String getProfileUrl() {
        return ""+profileUrl;
    }

    public String getContentImageUrl() {
        return ""+contentImageUrl;
    }

    public String getSource() {
        return source;
    }

    public String getCommentsCount() {
        return ""+commentsCount;
    }

}
