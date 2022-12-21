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

public class Signup_Mobile extends AppCompatActivity {

    Button next_buttonBTN;
    TextInputEditText mobile_numberET;
    ImageView changebackIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_mobile);
        Functions.whiteStatusBar(this);
        next_buttonBTN = (Button) findViewById(R.id.next_buttonBTN);
        mobile_numberET =(TextInputEditText)findViewById(R.id.mobile_numberET);
        changebackIV =(ImageView) findViewById(R.id.changebackIV);

        next_buttonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOtp_Verification();
            }


        });

        changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void openOtp_Verification() {
        String mobile = mobile_numberET.getText().toString().trim();

        if(TextUtils.isEmpty(mobile)){
            mobile_numberET.setError("Enter Mobile Number");
            mobile_numberET.requestFocus();
            return;
        }

        if(mobile.length() != 10){
            mobile_numberET.setError("Enter Only 10 digits Mobile Number");
            mobile_numberET.requestFocus();
            return;
        }

        Intent intent = new Intent(this, Otp_Verification.class);
        intent.putExtra("mobile_number",mobile_numberET.getText().toString().trim());
        startActivity(intent);
        mobile_numberET.setText("");



    }
}