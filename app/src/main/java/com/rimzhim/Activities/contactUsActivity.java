package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityContactUsBinding;

public class contactUsActivity extends AppCompatActivity {
    ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);
        Functions.whiteStatusBar(this);

        binding.callCrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumber = "1234567890";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobileNumber));
                startActivity(intent);

            }
        });

        binding.emailCrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sendEmail = "abc@gmail.com";
                String subject = "subject";
                String body ="Body";
                Uri uri = Uri.parse("mailto:"+sendEmail+"?subject="+subject+"&body="+body);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));


            }
        });
        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}