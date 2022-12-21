package com.rimzhim.Activities;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rimzhim.Adapters.followingAdapter;
import com.rimzhim.Adapters.leaderBordConstestantsAdapter;
import com.rimzhim.ModelClasses.ContestResponse.ContestsResponseModel;
import com.rimzhim.ModelClasses.LeaderBordModel.LeaderboardItem;
import com.rimzhim.ModelClasses.LeaderBordModel.LeaderboardResponse;
import com.rimzhim.ModelClasses.leaderBordContestantModel;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PaginationListener;
import com.rimzhim.databinding.ActivityLeaderBordBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class leaderBordActivity extends AppCompatActivity {

    List<LeaderboardItem> List = new ArrayList<>();
    ActivityLeaderBordBinding binding;
    String contest_id;
    RecyclerView recyclerView;
    leaderBordConstestantsAdapter adapter;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_leader_bord);
        Functions.whiteStatusBar(this);
        Intent intent = getIntent();
        contest_id = intent.getStringExtra("contest_id");
        recyclerView = (RecyclerView)findViewById(R.id.contestantsView);
        binding.changebackIV.setOnClickListener(view -> finish());

        final LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(false);

        loadData();


        recyclerView.addOnScrollListener(new PaginationListener(layout) {
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
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        String userId = String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("contest_id",contest_id);
        hashMap.put("page", String.valueOf(currentPage));
        binding.progress.setVisibility(View.VISIBLE);
        Call<LeaderboardResponse> call = ApiClient.getUserService().leaderboard(hashMap);
        call.enqueue(new Callback<LeaderboardResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<LeaderboardResponse> call, Response<LeaderboardResponse> response) {
                binding.progress.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        if(!response.body().getLeaderboard().isEmpty()){
                            List.addAll(response.body().getLeaderboard());
                            adapter = new leaderBordConstestantsAdapter(getApplicationContext(),List);
                            recyclerView.setAdapter(adapter);
                            setWinnersImages(List);

                        }
                    }else {
                       // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<LeaderboardResponse> call, Throwable t) {
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(leaderBordActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    private void setWinnersImages(java.util.List<LeaderboardItem> list) {
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getRank() == 1){
                binding.FirstPosUserImg.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(list.get(i).getUser_image()).placeholder(R.drawable.ic_user_icon).into(binding.FirstPosUserImg);
            }
            else if(list.get(i).getRank() == 2){
                binding.SecondPosUserImg.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(list.get(i).getUser_image()).placeholder(R.drawable.ic_user_icon).into(binding.SecondPosUserImg);

            }else if(list.get(i).getRank() == 3){
                binding.ThirdPosUserImg.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(list.get(i).getUser_image()).placeholder(R.drawable.ic_user_icon).into(binding.ThirdPosUserImg);
            }

        }
    }

}