package com.example.smnapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Just switching between activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Switch Activities Buttons
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonPost = findViewById(R.id.buttonPost);

        buttonSearch.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        if (v.getId()==R.id.buttonPost){
            intent = new Intent(this, PostActivity.class);
        }
        else if (v.getId()==R.id.buttonSearch){
            intent = new Intent(this, SearchActivity.class);
        }

        if (intent!=null){
            startActivity(intent);
        }
    }
}