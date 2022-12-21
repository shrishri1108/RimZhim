package com.rimzhim.Activities;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rimzhim.Adapters.TransactionHistoryAdapter;
import com.rimzhim.Adapters.followingAdapter;
import com.rimzhim.ModelClasses.TransactionHistory.DataItem;
import com.rimzhim.ModelClasses.TransactionHistory.TransactionHistoryResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PaginationListener;
import com.rimzhim.databinding.ActivityTrasactionHistoryBindingImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrasactionHistoryActivity extends AppCompatActivity {
    ActivityTrasactionHistoryBindingImpl binding;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    TransactionHistoryAdapter adapter;
    List<DataItem> List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_trasaction_history);
        Functions.whiteStatusBar(TrasactionHistoryActivity.this);
        binding.changebackIV.setOnClickListener(view -> {
            finish();
        });

        final LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        binding.recyclerView.setLayoutManager(layout);
        binding.recyclerView.setHasFixedSize(false);
        adapter = new TransactionHistoryAdapter(getApplicationContext(),new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);

        loadData();

        binding.recyclerView.addOnScrollListener(new PaginationListener(layout) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                loadData();
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


    }

    private void loadData() {
        String tokenT =  loginResponsePref.getInstance(getApplicationContext()).getToken();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token",tokenT);
        Call<TransactionHistoryResponse> call = ApiClient.getUserService().getTransactionHistory(hashMap);
        call.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                if(response.isSuccessful()){
                    //  Functions.dismissProgressDialog();
                    if(response.body().isResult()){
                        // Functions.dismissProgressDialog();

                        if(!response.body().getTransactions().getData().isEmpty()){
                            //Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            PAGE_SIZE = response.body().getTransactions().getLast_page();
                            List.addAll(response.body().getTransactions().getData());

                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addItems(List);
                            if (currentPage < PAGE_SIZE) {
                                adapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                        }

                    }else {
                        //  Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                    }


                }

            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}