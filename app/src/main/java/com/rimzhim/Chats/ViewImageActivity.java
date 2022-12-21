package com.rimzhim.Chats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;

public class ViewImageActivity extends AppCompatActivity {
    ImageView img;
    String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        Functions.whiteStatusBar(ViewImageActivity.this);
        Intent intent = getIntent();
        uri =  intent.getStringExtra("img");
        img =(ImageView)findViewById(R.id.img);
        Glide.with(ViewImageActivity.this).load(uri).into(img);
    }
}