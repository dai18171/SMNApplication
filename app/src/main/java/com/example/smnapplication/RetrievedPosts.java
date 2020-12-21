package com.example.smnapplication;


import androidx.annotation.Nullable;


/*
This is a general class to handle the different posts coming from Twitter,
Instagram, Facebook as one entity and put them in a general List.
 */
public class RetrievedPosts {
    private String source;
    private final String author;
    private final String username;
    private final String content;
    private final String profileUrl;
    private final String contentImageUrl;
    private int likesCount =  0;
    private int sharesCount = 0;

    public RetrievedPosts(String source, String author, String username, String content, @Nullable String contentImageUrl, int likesCount, int sharesCount, String profileUrl) {
        this.source = source;
        this.author = author;
        this.username = username;
        this.content = content;
        this.likesCount = likesCount;
        this.sharesCount = sharesCount;
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
        return String.valueOf(likesCount);
    }

    public String getSharesCount() {
        return String.valueOf(sharesCount);
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
}
