package com.rimzhim.Chats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rimzhim.R;

public class SendImgActivity extends AppCompatActivity {
    ImageView img, sendBtn;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        url = intent.getStringExtra("img");
        setContentView(R.layout.activity_send_img);
        img =(ImageView) findViewById(R.id.img);
        sendBtn =(ImageView)findViewById(R.id.sendImg);




        sendBtn.setOnClickListener(view -> {
            Intent intent2 = new Intent();
            intent2.putExtra("img", url);
            setResult(RESULT_OK, intent2);
            finish();
        });


    }
}