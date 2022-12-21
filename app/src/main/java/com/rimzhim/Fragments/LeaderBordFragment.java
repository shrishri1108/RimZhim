package com.rimzhim.Fragments;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rimzhim.Activities.leaderBordActivity;
import com.rimzhim.Adapters.leaderBordConstestantsAdapter;
import com.rimzhim.ModelClasses.LeaderBordModel.LeaderboardItem;
import com.rimzhim.ModelClasses.LeaderBordModel.LeaderboardResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PaginationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaderBordFragment extends Fragment {
    String contestId;
    RecyclerView recyclerView;
    leaderBordConstestantsAdapter adapter;
    List<LeaderboardItem> List = new ArrayList<>();

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;



    public LeaderBordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_leader_bord, container, false);
        contestId = getArguments().getString("contestId");
        recyclerView =(RecyclerView)view.findViewById(R.id.contestantsView);
        final LinearLayoutManager layout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
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




        return view;
    }

    private void loadData() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
       // String userId = String.valueOf(SharedPrefManager.getInstance(getContext()).getUser().getId());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("contest_id",contestId);
        hashMap.put("page", String.valueOf(currentPage));
        Functions.showProgressDialog(getContext());
        Call<LeaderboardResponse> call = ApiClient.getUserService().leaderboard(hashMap);
        call.enqueue(new Callback<LeaderboardResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<LeaderboardResponse> call, Response<LeaderboardResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        if(!response.body().getLeaderboard().isEmpty()){
                            List.addAll(response.body().getLeaderboard());
                            adapter = new leaderBordConstestantsAdapter(getContext(),List);
                            recyclerView.setAdapter(adapter);


                        }
                    }else {
                        // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<LeaderboardResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }
}