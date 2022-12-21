package com.rimzhim.WatchProfileVideos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.rimzhim.ModelClasses.model.DataItem;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WatchUserProfileVideoActivity extends AppCompatActivity {
    String position, userId, videoType;
    int page;
    int videoId;
    DataItem item ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_user_profile_video);
        Functions.blackStatusBar(this);
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        userId  =  intent.getStringExtra("userId");
        videoType  = intent.getStringExtra("videoType");
        page = intent.getIntExtra("page",1);
        videoId = intent.getIntExtra("videoId",1);



        Fragment fragment = new watchUserProfileVideosFragment();
        Bundle bundle = new Bundle();
        bundle.putString("position",position);
        bundle.putString("userId",userId);
        bundle.putString("videoType",videoType);
        bundle.putInt("page",page);
        bundle.putInt("videoId",videoId);


        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = new watchUserProfileVideosFragment();
        getSupportFragmentManager().beginTransaction().remove(fragment);
        finish();
    }
}