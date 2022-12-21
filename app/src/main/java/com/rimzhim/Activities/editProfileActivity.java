package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityEditProfileBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        Functions.whiteStatusBar(this);
        //set user Full name and bio
        LoginResponse.User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        binding.fullNameET.setText(user.getName());
        binding.bioET.setText(user.getBio());

        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        binding.saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processToEditProfile(user);

            }
        });
    }

    private void processToEditProfile(LoginResponse.User user) {
        final  String fullName = binding.fullNameET.getText().toString().trim();
        final  String bio = binding.bioET.getText().toString().trim();

        /*if(binding.bioET.getText().toString().trim().equals(user.getBio().trim())){
            Toast.makeText(getApplicationContext(),"There is no Changes in Profile",Toast.LENGTH_SHORT).show();
        }else if (binding.fullNameET.getText().toString().trim().equals(user.getName().trim())){
            Toast.makeText(getApplicationContext(),"There is no Changes in Profile",Toast.LENGTH_SHORT).show();

        }else*/

        if(TextUtils.isEmpty(fullName)){
            binding.fullNameET.setError("Add Full Name");
            binding.fullNameET.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(bio)){
            binding.bioET.setError("Add Bio");
            binding.bioET.requestFocus();
            return;

        }
        editUserProfile(fullName, bio);


    }

    private void editUserProfile(String fullName, String bio) {
        String token =  loginResponsePref.getInstance(getApplicationContext()).getToken();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name",fullName);
        hashMap.put("bio",bio);
        hashMap.put("token",token);

        progressDialog = new ProgressDialog(editProfileActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        Call<LoginResponse> call = ApiClient.getUserService().editProfile(hashMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getResult().equals("true")){
                        progressDialog.dismiss();
                        loginResponsePref.getInstance(getApplicationContext()).setToken(String.valueOf(response.body().getToken()),response.body().getMessage());

                        Toast.makeText(getApplicationContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                        LoginResponse.User user = new LoginResponse.User(
                                Integer.valueOf(response.body().getUser().getId()),
                                Integer.valueOf(response.body().getUser().getRoleId()),
                                response.body().getUser().getName(),
                                response.body().getUser().getEmail(),
                                response.body().getUser().getPhone(),
                                response.body().getUser().getStateId(),
                                response.body().getUser().getCityId(),
                                response.body().getUser().getUser_name(),
                                response.body().getUser().getReferralCode(),
                                response.body().getUser().getDob(),
                                response.body().getUser().getLongitude(),
                                response.body().getUser().getGender(),
                                response.body().getUser().getWallet(),
                                response.body().getUser().getReferUserId(),
                                response.body().getUser().getLatitude(),
                                Integer.valueOf(response.body().getUser().getStatus()),
                                response.body().getUser().getBio(),
                                response.body().getUser().getWinning(),
                                response.body().getUser().getBackground_image(),
                                response.body().getUser().getImage(),
                                Integer.valueOf(response.body().getUser().getIsDelete()),
                                response.body().getUser().getAddress(),
                                response.body().getUser().getCreatedAt(),
                                response.body().getUser().getUpdatedAt(),
                                response.body().getUser().getTimeOfBirth(),
                                response.body().getUser().getSocialId(),
                                response.body().getUser().getSocialId()
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        binding.fullNameET.setText(response.body().getUser().getName().trim());
                        binding.bioET.setText(response.body().getUser().getBio());
                        Functions.hideSoftKeyboard(editProfileActivity.this);
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(editProfileActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}