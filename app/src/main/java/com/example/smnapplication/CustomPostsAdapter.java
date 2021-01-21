package com.example.smnapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CustomPostsAdapter extends ArrayAdapter<RetrievedPosts> {

    private static final String TAG = "Twitter";

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
            viewHolder.contentImage = convertView.findViewById(R.id.contentImage);
            viewHolder.smnImage = convertView.findViewById(R.id.smnImage);
            viewHolder.profileImage = convertView.findViewById(R.id.profileImage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Set data on viewholder's views
        RetrievedPosts post = posts.get(position);
        viewHolder.authorText.setText(post.getAuthor());
        viewHolder.userText.setText(post.getUsername());
        viewHolder.descriptionText.setText(post.getContent());
        viewHolder.likesCountText.setText(post.getLikesCount());
        viewHolder.sharesCountText.setText(post.getSharesCount());
        viewHolder.contentImage.setImageResource(R.drawable.ic_baseline_image_24);
        viewHolder.smnImage.setImageResource(R.drawable.ic_baseline_image_24);
        Picasso.get().load("" + post.getProfileUrl()).into(viewHolder.profileImage);
        if (post.getContentImageUrl() != null) {
            viewHolder.contentImage.setVisibility(View.VISIBLE);
            Picasso.get().load("" + post.getContentImageUrl()).into(viewHolder.contentImage);
        }
        if (post.getSource().equals("Twitter")) {
            viewHolder.smnImage.setImageResource(R.drawable.twittericon);
        }



        return convertView;
    }

    private static class ViewHolder{
        TextView userText;
        TextView authorText;
        TextView descriptionText;
        TextView likesCountText;
        TextView sharesCountText;
        ImageView contentImage;
        ImageView smnImage;
        ImageView profileImage;
    }

}
