package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rimzhim.ModelClasses.SimpleResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivitySettingBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    Dialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_setting);
        Functions.whiteStatusBar(this);
        mdialog = new Dialog(this);

        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopupLogout();

            }
        });

        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),privacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        binding.tramsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),tramsAndConditionActivity.class);
                startActivity(intent);
            }
        });

        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),helpActivity.class);
                startActivity(intent);
            }
        });
        binding.contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),contactUsActivity.class);
                startActivity(intent);
            }
        });

        binding.accountDeactivate.setOnClickListener(view -> {
            OpenDialog();
        });

    }

    private void OpenDialog() {
        mdialog.setContentView(R.layout.account_deactivate_dialog);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button Yes = mdialog.findViewById(R.id.YesBTN);
        Button No = mdialog.findViewById(R.id.NoBtn);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
               DeactivateAccount();
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
            }
        });
        mdialog.show();
    }

    private void DeactivateAccount() {
        String tokenT =  loginResponsePref.getInstance(getApplicationContext()).getToken();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token",tokenT);
        Functions.showProgressDialog(SettingActivity.this);
        Call<SimpleResponse> call = ApiClient.getUserService().DeleteAccount(hashMap);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){

                    if(response.body().isResult()){
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        LogOut();
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LogOut() {
        String tokenT =  loginResponsePref.getInstance(getApplicationContext()).getToken();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token",tokenT);
        Functions.showProgressDialog(SettingActivity.this);
        Call<SimpleResponse> call  = ApiClient.getUserService().LogOut(hashMap);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        loginResponsePref.getInstance(getApplicationContext()).removeToken();
                        Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                        startActivity(intent);
                        finishAffinity();
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openPopupLogout()
    {
        mdialog.setContentView(R.layout.logoutdialoge);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button popupBTN = mdialog.findViewById(R.id.popupBTN);
        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
                mdialog.dismiss();
            }
        });
        mdialog.show();
    }
}