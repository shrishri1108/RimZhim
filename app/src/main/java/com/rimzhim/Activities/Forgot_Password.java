package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityForgotPasswordBinding;

public class Forgot_Password extends AppCompatActivity {
    Button next_buttonBTN;
    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
        Functions.whiteStatusBar(this);
        binding.nextButtonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next_Move();

            }
        });
        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void  next_Move() {

        final   String mobile =  binding.mobileNumberET.getText().toString().trim();

        if(TextUtils.isEmpty(mobile)){
            binding.mobileNumberET.setError("Enter Mobile Number");
            binding.mobileNumberET.requestFocus();
            return;
        }

        if(mobile.length() != 10){
            binding.mobileNumberET.setError("Enter Only 10 digits Mobile Number");
            binding.mobileNumberET.requestFocus();
            return;
        }

        Intent intent = new Intent(this, Forgot_Password_Verify.class);
        intent.putExtra("mobile_number", binding.mobileNumberET.getText().toString().trim());
        startActivity(intent);
        binding.mobileNumberET.setText("");




    }

    private void openForgot_Password_Verify() {
        Intent intent = new Intent(this, Forgot_Password_Verify.class);
        startActivity(intent);
        finish();
    }
}