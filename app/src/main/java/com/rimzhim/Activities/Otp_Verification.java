package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.rimzhim.ModelClasses.optRequestModel;
import com.rimzhim.ModelClasses.optResponseModel;
import com.rimzhim.ModelClasses.verifyOtpModelRequest;
import com.rimzhim.ModelClasses.verifyOtpResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Otp_Verification extends AppCompatActivity {
    Button verify_otp_buttonBTN;
    String mobileNumber;
    TextView mobileNumberTV, resendTV;
    String otp;
    EditText otp1, otp2, otp3, otp4;
    ProgressDialog progressDialog;
    PinView pinView;
    ImageView changebackIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        Intent intent = getIntent();
        mobileNumber =  intent.getStringExtra("mobile_number");

        mobileNumberTV = (TextView)findViewById(R.id.mobilenumberTV);
        resendTV = (TextView)findViewById(R.id.resendTV);
        verify_otp_buttonBTN = (Button) findViewById(R.id.verify_otp_buttonBTN);
        pinView = findViewById(R.id.pin_view);

        changebackIV =(ImageView)findViewById(R.id.changebackIV);
        mobileNumberTV.setText(mobileNumber);












        sendOTP();

        verify_otp_buttonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();

            }


        });

        resendTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP();
            }
        });

        changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendOTP() {
        progressDialog = new ProgressDialog(Otp_Verification.this);
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
        progressDialog = new ProgressDialog(Otp_Verification.this);
        progressDialog.setMessage("Verifying OTP....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        String pinViewOtp;
        pinViewOtp = pinView.getText().toString().trim();

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
                        openSignup_Main();
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

    private void openSignup_Main() {
        Intent intent = new Intent(this, Signup_Main.class);
        intent.putExtra("mobile",mobileNumber);
        intent.putExtra("email","No_EMAIL");
        startActivity(intent);
        finish();


    }
}