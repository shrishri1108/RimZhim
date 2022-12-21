package com.rimzhim.PaymentGateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.rimzhim.ModelClasses.SimpleResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.PaymentGateway.CreatePaymentResponse.CreatePaymentResponse;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityWithdrawalCashBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawalCashActivity extends AppCompatActivity {
    ActivityWithdrawalCashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdrawal_cash);
        Functions.whiteStatusBar(WithdrawalCashActivity.this);
        binding.changebackIV.setOnClickListener(view -> {
            finish();
        });
        binding.StartPaymentBtn.setOnClickListener(view -> WithdrawalCashRequest());
    }

    private void WithdrawalCashRequest() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        String amount = binding.amount.getText().toString().trim();
        if(TextUtils.isEmpty(amount)){
            binding.amount.setError("Add Amount");
            binding.amount.requestFocus();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("amount", amount);
        binding.ProgressBar.setVisibility(View.VISIBLE);
        Call<SimpleResponse> call = ApiClient.getUserService().WithdrawalCashRequest(hashMap);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                binding.ProgressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                binding.ProgressBar.setVisibility(View.GONE);
                Toast.makeText(WithdrawalCashActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}