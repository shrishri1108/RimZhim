package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rimzhim.MainMenu.MainActivity;
import com.rimzhim.ModelClasses.LoginRequest;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends Activity {

    TextView signUp_mobileTV,signUp_gmailTV,forgot_passTV,sign_up_loginTV;
    Button login_buttonBTN;
    TextInputEditText userName;
    TextInputEditText password;
    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    CardView gmail_signIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login);
        Functions.whiteStatusBar(this);
        signUp_mobileTV = (TextView) findViewById(R.id.signUp_mobileTV);
        signUp_gmailTV = (TextView) findViewById(R.id.signup_gmailTV);
        forgot_passTV = (TextView) findViewById(R.id.forget_passTV);
        sign_up_loginTV= (TextView) findViewById(R.id.sign_up_loginTV);

        userName =(TextInputEditText) findViewById(R.id.usernameET);
        password =(TextInputEditText) findViewById(R.id.passwordET);


        login_buttonBTN = (Button) findViewById(R.id.login_buttonBTN);

        binding.gmailSignIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignup_gmail();

            }
        });
        binding.mobileSignCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignup_mobile();

            }
        });



        forgot_passTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgot_Password();
            }


        });

        login_buttonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  openDashboard_Screen();
                login();



            }


        });
        sign_up_loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //openSignup_Main();
            }


        });

    }
















    private void openSignup_mobile() {
        Intent intent = new Intent(this, Signup_Mobile.class);
        startActivity(intent);


    }
    private void openSignup_gmail() {
        Intent intent = new Intent(this, Signup_Gmail.class);
        startActivity(intent);


    }
    private void openForgot_Password() {
        Intent intent = new Intent(this, Forgot_Password.class);
        startActivity(intent);


    }

    public void login(){
        final String UserName = userName.getText().toString().trim();
        final String Password = password.getText().toString().trim();

        if(TextUtils.isEmpty(UserName)){
            binding.usernameET.setError("Enter Your User Name");
            binding.usernameET.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(Password)){
           binding.passwordET.setError("Enter Your Password");
            binding.passwordET.requestFocus();
            return;
        }

        progressDialog = new ProgressDialog(Login_Activity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(UserName);
        loginRequest.setPassword(Password);
        Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {



                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    if(response.body().getResult().equals("success")){
                        loginResponsePref.getInstance(getApplicationContext()).setToken(response.body().getToken(),response.body().getMessage());
                        Toast.makeText(Login_Activity.this,"Login Successful", Toast.LENGTH_LONG).show();
                        LoginResponse loginResponse = response.body();
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

                        SharedPrefManager.getInstance(Login_Activity.this).userLogin(user);
                        Functions.hideSoftKeyboard(Login_Activity.this);
                        Intent intent = new Intent(Login_Activity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(Login_Activity.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Login_Activity.this,"Login Failed", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Login_Activity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
}










