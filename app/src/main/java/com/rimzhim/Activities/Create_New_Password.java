package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityCreateNewPasswordBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create_New_Password extends AppCompatActivity {
    ActivityCreateNewPasswordBinding binding;
    ProgressDialog progressDialog;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_new_password);
        Functions.whiteStatusBar(this);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");


        binding.continueButtonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewPassword();

            }
        });
        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void setNewPassword() {
        final String password = binding.newPasswordET.getText().toString().trim();
        final String confirmPassword = binding.conformNewPassET.getText().toString().trim();

        if(TextUtils.isEmpty(password)){
            binding.newPasswordET.setError("Enter the Password");
            binding.newPasswordET.requestFocus();
            return;

        }

        if(TextUtils.isEmpty(confirmPassword)){
            binding.conformNewPassET.setError("Enter the Password");
            binding.conformNewPassET.requestFocus();
            return;
        }

        if(!password.equals(confirmPassword)){
            binding.conformNewPassET.setError("Confirm Password Not Match");
            binding.conformNewPassET.requestFocus();
            return;
        }

        if(password.length() <= 6 || password.length() >10){
            binding.conformNewPassET.setError("Password length should be in between 7 to 10");
            binding.conformNewPassET.requestFocus();
            return;

        }
        progressDialog = new ProgressDialog(Create_New_Password.this);
        progressDialog.setMessage("Verifying OTP....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);




        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("phone",phone.trim());
        hashMap.put("password",password);



        Call<LoginResponse> call = ApiClient.getUserService().setNewPassword(hashMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    if(response.body().getResult().equals("true")){
                        Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),Login_Activity.class);
                        startActivity(intent);
                        finishAffinity();


                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();


            }
        });




    }
}