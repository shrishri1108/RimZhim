package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.razorpay.PaymentResultWithDataListener;
import com.rimzhim.ModelClasses.JoinContestResponse.JoinContestResponse;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.UserProfile.User;
import com.rimzhim.ModelClasses.UserProfile.UserProfile;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.PaymentGateway.CreatePaymentResponse.CreatePaymentResponse;
import com.rimzhim.PaymentGateway.StartPayments;
import com.rimzhim.PaymentGateway.UpdatePaymentStatusReesponse.UpdatePaymentStatusResponse;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityTotalBalanceBinding;
import com.rimzhim.dialogs.contestBookedDialog;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalBalanceActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    ActivityTotalBalanceBinding binding;
    String list;
    String TotalAmountStr, TotalDepositCashStr, TotalWinningAmountStr;
    int entryAmount, contestId;
    int total, entry, addBalance;
    String where ="FromVideoGallery";
    Dialog mdialog;
    private static final String TAG = StartPayments.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_total_balance);
        Intent intent = getIntent();
        list =  intent.getStringExtra("list");
        entryAmount = intent.getIntExtra("entryAmount",0);
        contestId = intent.getIntExtra("contestId",0);
        mdialog = new Dialog(this);

        getUserWalletAmount();

        binding.ContinuetBTN.setOnClickListener(view -> {
            BookContest();

        });

        binding.changebackIV.setOnClickListener(view -> {
            finish();
        });
    }


    private void getUserWalletAmount() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("token", token);
        Functions.showProgressDialog(TotalBalanceActivity.this);
        Call<UserProfile> call = ApiClient.getUserService().userProfile(hashMap);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){

                        setUpData(response.body().getUser());

                    }else {
                       // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpData(User data) {
        binding.entry.setText("₹ "+String.valueOf(entryAmount));
        if(data.getWallet() != null){
            TotalAmountStr = data.getWallet().trim();
            binding.CurrentBalance.setText("₹ "+TotalAmountStr);
            total = Integer.parseInt(TotalAmountStr);

            if(total < entryAmount){
                addBalance = entryAmount - total;
                binding.addCash.setText("₹ "+String.valueOf(addBalance));
            }else {
                binding.addCash.setText("₹ 0.00");
            }
        }

        if(where.equals("FromPaymentGateway")){
            BookContest();
        }else {


        }
    }

    private void BookContest() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();

        if(total < entryAmount){
            AddBalanceDialog(TotalBalanceActivity.this);
           // Toast.makeText(getApplicationContext(),"Add Balance in Your Wallet",Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("contest_id",String.valueOf(contestId));
        hashMap.put("videos",list);
        Functions.showProgressDialog(TotalBalanceActivity.this);
        Call<JoinContestResponse> call = ApiClient.getUserService().JoinContest(hashMap);
        call.enqueue(new Callback<JoinContestResponse>() {
            @Override
            public void onResponse(Call<JoinContestResponse> call, Response<JoinContestResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                       OpenContestBookedDialog(TotalBalanceActivity.this,String.valueOf(contestId));

                    }else {
                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<JoinContestResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private  void OpenContestBookedDialog(Context context, String contest_id){
        Dialog mdialog;
        mdialog = new Dialog(context);
        mdialog.setContentView(R.layout.contest_booked_dialog);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button doneBTN = mdialog.findViewById(R.id.doneBTN);
        doneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                finish();
            }
        });
        mdialog.show();
    }

    private  void AddBalanceDialog(Context context){
        Dialog mdialog;
        mdialog = new Dialog(context);
        mdialog.setContentView(R.layout.dialog_add_balance);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button AddCashBTN = mdialog.findViewById(R.id.AddCashBTN);
        Button cancel = mdialog.findViewById(R.id.cancelBTN);
        AddCashBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                getOrderId();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mdialog.show();
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
           // Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
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

    private void startPayments(String orderId, String add) {
        int round = Integer.parseInt(add)*100;
        add =   String.valueOf(round);
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
            options.put("amount", add);
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

    private void getOrderId() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("amount", String.valueOf(addBalance));
        Functions.showProgressDialog(TotalBalanceActivity.this);
        Call<CreatePaymentResponse> call = ApiClient.getUserService().getOrderId(hashMap);
        call.enqueue(new Callback<CreatePaymentResponse>() {
            @Override
            public void onResponse(Call<CreatePaymentResponse> call, Response<CreatePaymentResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){

                    if(!response.body().getPayment_details().getOrder_id().isEmpty()){
                       // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        String OrderId = response.body().getPayment_details().getOrder_id();
                        String amount = response.body().getPayment_details().getAmount();
                        startPayments(OrderId, String.valueOf(amount));

                    }
                   // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CreatePaymentResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(TotalBalanceActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void VerifySignature(String razorpay_payment_id, String razorpay_order_id, String razorpay_signature) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("razorpay_payment_id", razorpay_payment_id);
        hashMap.put("razorpay_order_id", razorpay_order_id);
        hashMap.put("razorpay_signature", razorpay_signature);
        Functions.showProgressDialog(TotalBalanceActivity.this);
        Call<UpdatePaymentStatusResponse>  call = ApiClient.getUserService().updatePaymentStatus(hashMap);
        call.enqueue(new Callback<UpdatePaymentStatusResponse>() {
            @Override
            public void onResponse(Call<UpdatePaymentStatusResponse> call, Response<UpdatePaymentStatusResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        //Functions.makeToast(getApplicationContext(),response.body().getMessage().trim());
                        where = "FromPaymentGateway";
                        getUserWalletAmount();
                        //openDialog();

                       // BookContest();
                    }
                }
            }
            @Override
            public void onFailure(Call<UpdatePaymentStatusResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(TotalBalanceActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void openDialog() {
        mdialog.setContentView(R.layout.payment_success_dialog);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button popupBTN = mdialog.findViewById(R.id.popupBTN);
        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();

            }
        });
        mdialog.show();
    }

}