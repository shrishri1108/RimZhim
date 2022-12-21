package com.rimzhim.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rimzhim.Activities.KYCActivity;
import com.rimzhim.Activities.TrasactionHistoryActivity;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.RootFragment;
import com.rimzhim.ModelClasses.UserProfile.User;
import com.rimzhim.ModelClasses.UserProfile.UserProfile;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.PaymentGateway.StartPayments;
import com.rimzhim.PaymentGateway.WithdrawalCashActivity;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletFragment  extends RootFragment implements View.OnClickListener {
    Button addCashBtn, withdrawalBtn;
    TextView TotalAmount, TotalDepositCash, TotalWinningAmount, TransactionHistory;
    String TotalAmountStr, TotalDepositCashStr, TotalWinningAmountStr;
    SwipeRefreshLayout refreshPage;
    ImageView goTransactionArrow;

    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        refreshPage = (SwipeRefreshLayout)view.findViewById(R.id.refreshPage);
        addCashBtn =(Button)view.findViewById(R.id.addCashBtn);
        withdrawalBtn =(Button)view.findViewById(R.id.withdrawalBtn);
        TotalAmount =(TextView)view.findViewById(R.id.showAmount);
        TotalDepositCash =(TextView)view.findViewById(R.id.showDepositCash);
        TotalWinningAmount =(TextView)view.findViewById(R.id.showWinningCash);
        TransactionHistory =(TextView)view.findViewById(R.id.openTransactionHistory);
        goTransactionArrow =(ImageView)view.findViewById(R.id.goTransactionArrow);
        addCashBtn.setOnClickListener(this);
        withdrawalBtn.setOnClickListener(this);
        TransactionHistory.setOnClickListener(this);
        loadUserData();

        refreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                loadUserData();
                refreshPage.setRefreshing(false);
            }
        });

        return view;
    }

    private void loadUserData() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("token", token);
        Call<UserProfile> call = ApiClient.getUserService().userProfile(hashMap);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        setUpData(response.body().getUser());
                    }else {
                        Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpData(User user) {
        int winning, total, TotalDepositAmount;
        if(user.getWallet()!=null){
            TotalAmountStr = user.getWallet();
            TotalWinningAmountStr = user.getWinning();
            TotalDepositCash.setText("₹ "+TotalAmountStr);


            TotalAmount.setText("₹ "+TotalAmountStr);
            if(user.getWinning() != null){
                TotalWinningAmount.setText("₹ "+TotalWinningAmountStr);
            }else {
                TotalWinningAmount.setText("₹ 0");
            }

            total = Integer.parseInt(user.getWallet());

            if(user.getWinning()!= null){
                winning = Integer.parseInt(user.getWinning());
                if(winning > total){
                    TotalDepositAmount =  winning - total;
                }else {

                    TotalDepositAmount = total - winning;
                }
                TotalDepositCashStr = String.valueOf(TotalDepositAmount);

                TotalDepositCash.setText("₹ "+TotalDepositCashStr);
            }


        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.addCashBtn:
                AddCash();
                break;

            case R.id.withdrawalBtn:
                if(TotalWinningAmountStr == null){
                    Toast.makeText(getContext(),"You Have not Balance in your Winnings", Toast.LENGTH_SHORT).show();
                }else {
                    WithdrawalAmount();
                }
                break;

            case R.id.openTransactionHistory:
                showTransactionHistory();
                break;

            case R.id.goTransactionArrow:
                showTransactionHistory();
                break;



        }

    }


    private void AddCash() {
        /*  Intent intent = new Intent(getContext(), KYCActivity.class);
            startActivity(intent);*/
        Intent intent = new Intent(getContext(), StartPayments.class);
        startActivity(intent);
    }

    private void WithdrawalAmount() {
        Intent intent = new Intent(getContext(), WithdrawalCashActivity.class);
        startActivity(intent);
    }

    private void showTransactionHistory() {
        Intent intent = new Intent(getContext(), TrasactionHistoryActivity.class);
        startActivity(intent);
    }
}