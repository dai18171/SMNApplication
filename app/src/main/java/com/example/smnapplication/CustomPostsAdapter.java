package com.example.smnapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CustomPostsAdapter extends ArrayAdapter<RetrievedPosts> {

    private static final String TAG = "Twitter";
    private static final String TAG1 = "Facebook";

    private final LayoutInflater inflater;
    private final int layoutResource;
    private final List<RetrievedPosts> posts;

    public CustomPostsAdapter(Context context, int resource, List<RetrievedPosts> objects) {
        super(context, resource, objects);

        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        posts = objects;
    }

    @Override
    public int getCount() {
        Log.d(TAG1, ""+ posts.size());
        return posts.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Set a viewholder with the views needed
        ViewHolder viewHolder;

        if (convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder.userText = convertView.findViewById(R.id.userText);
            viewHolder.authorText = convertView.findViewById(R.id.authorText);
            viewHolder.descriptionText = convertView.findViewById(R.id.descriptionText);
            viewHolder.likesCountText = convertView.findViewById(R.id.likesCountText);
            viewHolder.sharesCountText = convertView.findViewById(R.id.sharesCountText);
            viewHolder.commentsCountText = convertView.findViewById(R.id.commentsCountText);
            viewHolder.contentImage = convertView.findViewById(R.id.contentImage);
            viewHolder.smnImage = convertView.findViewById(R.id.smnImage);
            viewHolder.profileImage = convertView.findViewById(R.id.profileImage);
            viewHolder.commentsImage = convertView.findViewById(R.id.commentsCountImage);
            viewHolder.sharesImage = convertView.findViewById(R.id.sharesCountImage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Set data on viewholder's views and hiding elements that are not included in each social media platform
        RetrievedPosts post = posts.get(position);
        viewHolder.descriptionText.setText(post.getContent());
        viewHolder.likesCountText.setText(post.getLikesCount());
        if (post.getSource().equals("Twitter")){
            viewHolder.smnImage.setImageResource(R.drawable.twittericon);
            viewHolder.commentsImage.setVisibility(View.INVISIBLE);
            viewHolder.commentsCountText.setVisibility(View.INVISIBLE);
            viewHolder.sharesImage.setVisibility(View.VISIBLE);
            viewHolder.sharesCountText.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.commentsCountText.setText(post.getCommentsCount());
        }
        if (post.getSource().equals("Instagram")){
            viewHolder.commentsCountText.setVisibility(View.VISIBLE);
            viewHolder.commentsImage.setVisibility(View.VISIBLE);
            viewHolder.sharesImage.setVisibility(View.INVISIBLE);
            viewHolder.sharesCountText.setVisibility(View.INVISIBLE);
            viewHolder.profileImage.setVisibility(View.INVISIBLE);
            viewHolder.authorText.setVisibility(View.INVISIBLE);
            viewHolder.userText.setVisibility(View.INVISIBLE);
            viewHolder.smnImage.setImageResource(R.drawable.instagramicon);
        }
        else{
            viewHolder.sharesCountText.setText(post.getSharesCount());
            Picasso.get().load("" + post.getProfileUrl()).into(viewHolder.profileImage);
            viewHolder.authorText.setText(post.getAuthor());
            viewHolder.userText.setText(post.getUsername());
        }
        if (post.getContentImageUrl() != null) {
            viewHolder.contentImage.setVisibility(View.VISIBLE);
            Picasso.get().load("" + post.getContentImageUrl()).into(viewHolder.contentImage);
        }

        return convertView;
    }

    private static class ViewHolder{
        TextView userText;
        TextView authorText;
        TextView descriptionText;
        TextView likesCountText;
        TextView sharesCountText;
        TextView commentsCountText;
        ImageView contentImage;
        ImageView smnImage;
        ImageView profileImage;
        ImageView commentsImage;
        ImageView sharesImage;
    }

}
