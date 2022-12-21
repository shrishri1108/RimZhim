package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;

public class Signup_Gmail extends AppCompatActivity {
    Button next_buttonBTN;
    TextInputEditText emailTv;
    ImageView changebackIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_gmail);
        Functions.whiteStatusBar(this);
        next_buttonBTN = (Button) findViewById(R.id.next_buttonBTN);
        emailTv =(TextInputEditText)findViewById(R.id.email_passwordET);
        changebackIV =(ImageView) findViewById(R.id.changebackIV);

        next_buttonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOtp_Verification_Gmail();
            }


        });
        changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openOtp_Verification_Gmail() {
        final  String emailStr = emailTv.getText().toString().trim();
        if(TextUtils.isEmpty(emailStr)){
            emailTv.setError("Enter Your Email");
            emailTv.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            emailTv.setError("Enter a valid email");
            emailTv.requestFocus();
            return;
        }

        Intent intent = new Intent(this, Otp_Verification_Gmail.class);
        intent.putExtra("email",emailStr);
        startActivity(intent);
        finish();


    }
}