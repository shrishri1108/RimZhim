package com.rimzhim.PaymentGateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.rimzhim.Activities.Signup_Main;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.PaymentGateway.CreatePaymentResponse.CreatePaymentResponse;
import com.rimzhim.PaymentGateway.UpdatePaymentStatusReesponse.UpdatePaymentStatusResponse;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityStartPaymentsBinding;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartPayments extends AppCompatActivity implements PaymentResultWithDataListener {
    private static final String TAG = StartPayments.class.getSimpleName();
    ActivityStartPaymentsBinding binding;
    Dialog mdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = DataBindingUtil.setContentView(this,R.layout.activity_start_payments);
      Functions.whiteStatusBar(this);
      mdialog = new Dialog(this);
      binding.StartPaymentBtn.setOnClickListener(view -> getOrderId());
      binding.changebackIV.setOnClickListener(view -> finish());
    }
    private void getOrderId() {
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
        Call<CreatePaymentResponse> call = ApiClient.getUserService().getOrderId(hashMap);
        call.enqueue(new Callback<CreatePaymentResponse>() {
            @Override
            public void onResponse(Call<CreatePaymentResponse> call, Response<CreatePaymentResponse> response) {
                if(response.isSuccessful()){
                    binding.ProgressBar.setVisibility(View.GONE);
                    if(!response.body().getPayment_details().getOrder_id().isEmpty()){
                        //Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        String OrderId = response.body().getPayment_details().getOrder_id();
                        String amount = response.body().getPayment_details().getAmount();
                        startPayments(OrderId, amount);
                        binding.amount.setText("");
                    }
                    //Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CreatePaymentResponse> call, Throwable t) {
                binding.ProgressBar.setVisibility(View.GONE);
                Toast.makeText(StartPayments.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void startPayments(String orderId, String amount) {
        int round = Integer.parseInt(amount)*100;
        amount =   String.valueOf(round);
        LoginResponse.User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String userId =   String.valueOf(user.getId());
        String name =   String.valueOf(user.getName());
        String email =   String.valueOf(user.getEmail());
        String number = String.valueOf(user.getPhone());
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_7FriwB5uT69cov");
        checkout.setImage(R.drawable.ic_user_icon);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", R.string.app_name);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", orderId);
            options.put("theme.color", "#ED4D4D");
            options.put("currency", "INR");
            options.put("amount", amount);
            options.put("prefill.email", email);
            options.put("prefill.contact",number);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
            String razorpay_payment_id = paymentData.getPaymentId().toString().trim();
            String razorpay_order_id = paymentData.getOrderId().toString().trim();
            String razorpay_signature = paymentData.getSignature().toString().trim();

            Log.d("razorpay_signature====", razorpay_signature +"  " +razorpay_order_id+" "+razorpay_payment_id);
            VerifySignature(razorpay_payment_id, razorpay_order_id, razorpay_signature);


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(getApplicationContext(),s.toString(),Toast.LENGTH_SHORT).show();
    }


    private void VerifySignature(String razorpay_payment_id, String razorpay_order_id, String razorpay_signature) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("razorpay_payment_id", razorpay_payment_id);
        hashMap.put("razorpay_order_id", razorpay_order_id);
        hashMap.put("razorpay_signature", razorpay_signature);

        Functions.showProgressDialog(StartPayments.this);
        Call<UpdatePaymentStatusResponse>  call = ApiClient.getUserService().updatePaymentStatus(hashMap);
        call.enqueue(new Callback<UpdatePaymentStatusResponse>() {
            @Override
            public void onResponse(Call<UpdatePaymentStatusResponse> call, Response<UpdatePaymentStatusResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){

                    if(response.body().isResult()){
                        //Functions.makeToast(getApplicationContext(),response.body().getMessage().trim());
                        openDialog();
                    }
                }
            }
            @Override
            public void onFailure(Call<UpdatePaymentStatusResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(StartPayments.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openDialog() {
        mdialog.setContentView(R.layout.payment_success_dialog);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button popupBTN = mdialog.findViewById(R.id.popupBTN);
        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                finish();
            }
        });
        mdialog.show();
    }
}