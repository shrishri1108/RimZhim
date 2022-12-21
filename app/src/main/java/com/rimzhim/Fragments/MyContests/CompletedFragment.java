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


import com.rimzhim.Adapters.completedContestAdapter;
import com.rimzhim.Adapters.contestAdapter;
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


public class CompletedFragment extends Fragment {
    RecyclerView completedView;
    List<DataItem> list = new ArrayList<>();
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    completedContestAdapter adapter;
  //contestAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completedView =(RecyclerView) view.findViewById(R.id.completedView);
        final LinearLayoutManager layout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        completedView.setLayoutManager(layout);
        completedView.setHasFixedSize(false);
       // adapter = new completedContestAdapter(new ArrayList<>(),getContext());
        adapter = new completedContestAdapter(new ArrayList<>(),getContext());
        completedView.setAdapter(adapter);
        loadData();
        completedView.addOnScrollListener(new PaginationListener(layout) {
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
        hashMap.put("type", "completed");
        String.valueOf(currentPage);
        Call<ContestsResponseModel> call = ApiClient.getUserService().ContestList(hashMap);
        call.enqueue(new Callback<ContestsResponseModel>() {
            @Override
            public void onResponse(Call<ContestsResponseModel> call, Response<ContestsResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        if(!response.body().getContest_list().getData().isEmpty()){
                            list =  response.body().getContest_list().getData();
                            /*completedContestAdapter adapter = new completedContestAdapter(getContext(),list);
                            completedView.setAdapter(adapter);*/

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
                        Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }


                }


            }

            @Override
            public void onFailure(Call<ContestsResponseModel> call, Throwable t) {
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });





    }
}