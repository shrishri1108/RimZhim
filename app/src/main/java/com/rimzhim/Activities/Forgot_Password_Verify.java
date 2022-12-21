package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rimzhim.ModelClasses.optRequestModel;
import com.rimzhim.ModelClasses.optResponseModel;
import com.rimzhim.ModelClasses.verifyOtpModelRequest;
import com.rimzhim.ModelClasses.verifyOtpResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityForgotPasswordVerifyBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot_Password_Verify extends AppCompatActivity {
    ActivityForgotPasswordVerifyBinding binding;
    ProgressDialog progressDialog;
    String mobileNumber;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password_verify);
        Functions.whiteStatusBar(this);
        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobile_number");
        binding.codeTV.setText("Code has been send to "+"+91"+ mobileNumber);

        sendOtp();


        binding.resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp();
            }
        });

        binding.verifyPasswordButtonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();
            }
        });
        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void sendOtp() {
        progressDialog = new ProgressDialog(Forgot_Password_Verify.this);
        progressDialog.setMessage("Sending OTP....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        optRequestModel optRequestModel = new optRequestModel();
        optRequestModel.setEmail("");
        optRequestModel.setPhone(mobileNumber);



        Call<optResponseModel> optResponseModelCall = ApiClient.getUserService().userOtpOnMobile(optRequestModel);
        optResponseModelCall.enqueue(new Callback<optResponseModel>() {
            @Override
            public void onResponse(Call<optResponseModel> call, Response<optResponseModel> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    if(response.body().getResult().trim().equals("success")){
                        Toast.makeText(getApplicationContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                        otp = response.body().getOtp().trim();

                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getResult().toString(),Toast.LENGTH_SHORT).show();

                    }
                }
            }
            @Override
            public void onFailure(Call<optResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }


    private void verifyOTP() {
        progressDialog = new ProgressDialog(Forgot_Password_Verify.this);
        progressDialog.setMessage("Verifying OTP....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        String pinViewOtp;
        pinViewOtp = binding.pinView.getText().toString().trim();

        verifyOtpModelRequest verifyOtpModelRequest = new verifyOtpModelRequest();
        verifyOtpModelRequest.setOtp(pinViewOtp);
        verifyOtpModelRequest.setEmail("");
        verifyOtpModelRequest.setMobile(mobileNumber);

        Call<verifyOtpResponse> verifyOtpResponseCall = ApiClient.getUserService().userVerifyOtp(verifyOtpModelRequest);
        verifyOtpResponseCall.enqueue(new Callback<verifyOtpResponse>() {
            @Override
            public void onResponse(Call<verifyOtpResponse> call, Response<verifyOtpResponse> response) {

                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    if(response.body().getResult().trim().equals("success")){
                        // openSignup_Main();
                        openCreate_New_Password();
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();


                    }else {

                        Toast.makeText(getApplicationContext(),response.body().getResult(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<verifyOtpResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });




    }


    private void openCreate_New_Password() {
        Intent intent = new Intent(this, Create_New_Password.class);
        intent.putExtra("phone", mobileNumber);
        startActivity(intent);
        finish();

    }
}