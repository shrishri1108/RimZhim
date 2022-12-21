package com.rimzhim.Fragments.MyContests;


import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.rimzhim.Adapters.contestAdapter;
import com.rimzhim.Adapters.liveContestAdapter;
import com.rimzhim.ModelClasses.ContestResponse.ContestsResponseModel;
import com.rimzhim.ModelClasses.ContestResponse.DataItem;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.PaginationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class liveContestsFragment extends Fragment {
    RecyclerView liveView;
   // ArrayList<liveModel> list = new ArrayList<>();
    List<DataItem> list = new ArrayList<>();
    SwipeRefreshLayout refreshLayout;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
   // liveContestAdapter adapter;
   contestAdapter adapter;
   boolean isApiRunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_live_contests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        liveView =(RecyclerView) view.findViewById(R.id.liveView);
        final LinearLayoutManager layout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        liveView.setLayoutManager(layout);

        liveView.setLayoutManager(layout);
        liveView.setHasFixedSize(false);
        adapter = new contestAdapter(new ArrayList<>(),getContext());
        liveView.setAdapter(adapter);

        loadData();

        liveView.addOnScrollListener(new PaginationListener(layout) {
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
        HashMap<String, String> hashMap = new HashMap<>();
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        hashMap.put("token", token);
        hashMap.put("type", "live");
        hashMap.put("page",String.valueOf(currentPage));
        isApiRunning = true;
        Call<ContestsResponseModel> call = ApiClient.getUserService().ContestList(hashMap);
        call.enqueue(new Callback<ContestsResponseModel>() {
            @Override
            public void onResponse(Call<ContestsResponseModel> call, Response<ContestsResponseModel> response) {
                isApiRunning = false;
                if(response.isSuccessful()){

                    if(response.body().isResult()){
                        if(!response.body().getContest_list().getData().isEmpty()){
                            PAGE_SIZE = response.body().getContest_list().getLast_page();
                            list =  response.body().getContest_list().getData();

                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addItems(list);
                            if (currentPage < PAGE_SIZE) {
                                adapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                        }

                      //  upComingContestAdapter adapter = new upComingContestAdapter(getContext(), list ,Up_ComingFragment.this );


                    }else {
                        Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ContestsResponseModel> call, Throwable t) {
                isApiRunning = false;
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });





    }
}