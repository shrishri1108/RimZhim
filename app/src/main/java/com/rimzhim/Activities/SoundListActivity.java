package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;

public class SoundListActivity extends AppCompatActivity {
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_list);
        Functions.blackStatusBar(this);
        backArrow =(ImageView)findViewById(R.id.changebackIV);
        backArrow.setOnClickListener(view -> finish());

    }
}