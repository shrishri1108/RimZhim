package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.rimzhim.ModelClasses.CMSPagesResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityPrivacyPolicyBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class privacyPolicyActivity extends AppCompatActivity {
    ActivityPrivacyPolicyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy);
        Functions.whiteStatusBar(this);
        binding.changebackIV.setOnClickListener(view -> finish());
        getPrivacyPolicy();

    }

    private void getPrivacyPolicy() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type","privacypolicy");
        Functions.showProgressDialog(privacyPolicyActivity.this);
        Call<CMSPagesResponse> call = ApiClient.getUserService().getCMSPages(hashMap);
        call.enqueue(new Callback<CMSPagesResponse>() {
            @Override
            public void onResponse(Call<CMSPagesResponse> call, Response<CMSPagesResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        if(!response.body().getPages().isEmpty()){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                binding.policiesText.setText(Html.fromHtml(response.body().getPages(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                binding.policiesText.setText(Html.fromHtml(response.body().getPages()));
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Data Not Found",Toast.LENGTH_SHORT).show();

                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<CMSPagesResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}