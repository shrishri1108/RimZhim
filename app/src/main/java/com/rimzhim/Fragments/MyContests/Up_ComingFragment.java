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


import com.facebook.shimmer.ShimmerFrameLayout;
import com.rimzhim.Adapters.contestAdapter;
import com.rimzhim.Interfaces.onClicksInterfaces.upComingContestItemOnClick;
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


public class Up_ComingFragment extends Fragment implements upComingContestItemOnClick {
        RecyclerView upComingView;
        List<DataItem> list = new ArrayList<>();
        public static ContestsResponseModel contestsResponseModel;
        String contest_id;
        SwipeRefreshLayout refreshLayout;
        ShimmerFrameLayout shimmer;
        contestAdapter adapter;
        private int currentPage = PAGE_START;
       private boolean isLastPage = false;
       private int totalPage = 10;
       private boolean isLoading = false;
       int itemCount = 0;
       boolean isApiRunning = false;

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up__coming, container, false);

            upComingView =(RecyclerView)view.findViewById(R.id.upComingView);
            shimmer =(ShimmerFrameLayout) view.findViewById(R.id.shimmer);


            final LinearLayoutManager layout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
            upComingView.setLayoutManager(layout);
            contestsResponseModel = new ContestsResponseModel();

            upComingView.setLayoutManager(layout);
            upComingView.setHasFixedSize(false);
            adapter = new contestAdapter(new ArrayList<>(),getContext());
            upComingView.setAdapter(adapter);

            loadData();



            upComingView.addOnScrollListener(new PaginationListener(layout) {
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

   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        upComingView =(RecyclerView)view.findViewById(R.id.upComingView);
        shimmer =(ShimmerFrameLayout) view.findViewById(R.id.shimmer);
        refreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.refreshPage);



        final LinearLayoutManager layout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        upComingView.setLayoutManager(layout);
        contestsResponseModel = new ContestsResponseModel();

        upComingView.setLayoutManager(layout);
        upComingView.setHasFixedSize(false);
        adapter = new contestAdapter(new ArrayList<>(),getContext());
        upComingView.setAdapter(adapter);
        loadData();

        upComingView.addOnScrollListener(new PaginationListener(layout) {
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isApiRunning){
                    currentPage =1;
                    list.clear();
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }*/

    private void loadData() {
            HashMap<String, String> hashMap = new HashMap<>();
            String token = loginResponsePref.getInstance(getContext()).getToken().trim();
            hashMap.put("token", token);
            hashMap.put("type", "upcoming");
            hashMap.put("page",String.valueOf(currentPage));
            shimmer.startShimmer();
        isApiRunning = true;
        Call<ContestsResponseModel> call = ApiClient.getUserService().ContestList(hashMap);
        call.enqueue(new Callback<ContestsResponseModel>() {
            @Override
            public void onResponse(Call<ContestsResponseModel> call, Response<ContestsResponseModel> response) {
                isApiRunning = false;
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                upComingView.setVisibility(View.VISIBLE);
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        if(!response.body().getContest_list().getData().isEmpty()){
                            PAGE_SIZE = response.body().getContest_list().getLast_page();

                            list =  response.body().getContest_list().getData();
                            contestsResponseModel = response.body();

                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addItems(list);
                            if (currentPage < PAGE_SIZE) {
                                adapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                        }

                    }else {
                       // Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ContestsResponseModel> call, Throwable t) {
                isApiRunning = false;
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                upComingView.setVisibility(View.VISIBLE);
               // Functions.dismissProgressDialog();
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(int position) {
           // contestBookedDialog.openAccountReadyDialog(getContext());
    }
}