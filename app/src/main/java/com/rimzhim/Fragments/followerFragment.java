package com.rimzhim.Fragments;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rimzhim.Adapters.contestAdapter;
import com.rimzhim.Adapters.followingAdapter;
import com.rimzhim.ModelClasses.ContestResponse.ContestsResponseModel;
import com.rimzhim.ModelClasses.FollowerFollowingResponseModel.DataItem;
import com.rimzhim.ModelClasses.FollowerFollowingResponseModel.Following;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PaginationListener;

public class followerFragment extends Fragment {
    RecyclerView followerView;
    List<DataItem> followerList = new ArrayList<>();
    ShimmerFrameLayout shimmer;
    String userId;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    followingAdapter adapter;
    String logInUserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userId = getArguments().getString("id");
        LoginResponse.User user = SharedPrefManager.getInstance(getContext()).getUser();
        logInUserId = String.valueOf(user.getId());

        followerView = (RecyclerView)view.findViewById(R.id.followerView);
        shimmer =(ShimmerFrameLayout)view.findViewById(R.id.shimmer);

        final LinearLayoutManager layout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        followerView.setLayoutManager(layout);
        followerView.setHasFixedSize(false);
        adapter = new followingAdapter(getContext(),new ArrayList<>());
        followerView.setAdapter(adapter);
       if(userId.equals(logInUserId)){
           loadFollowers();
      }else {
           otherUserFollowers();
       }

        followerView.addOnScrollListener(new PaginationListener(layout) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                if(userId.equals(logInUserId)){
                    loadFollowers();
                }else {
                    otherUserFollowers();
                }
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

    private void otherUserFollowers() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("type","follower");
        hashMap.put("user_id",userId);
        hashMap.put("page",String.valueOf(currentPage));

        // Functions.showProgressDialog(getContext(),"Loading...");
        shimmer.startShimmer();
        Call<Following> call = ApiClient.getUserService().OtherUserFollowing_list(hashMap);
        call.enqueue(new Callback<Following>() {
            @Override
            public void onResponse(Call<Following> call, Response<Following> response) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                followerView.setVisibility(View.VISIBLE);
                if(response.isSuccessful()){
                    //  Functions.dismissProgressDialog();
                    if(response.body().isResult()){
                        // Functions.dismissProgressDialog();

                        if(!response.body().getFollow_list().isEmpty()){
                            //Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            PAGE_SIZE = response.body().getFollow_list().get(0).getLast_page();
                            followerList= response.body().getFollow_list().get(0).getData();
                            Log.d("=======>>", followerList.toString());
                            /*followingAdapter adapter = new followingAdapter(getContext(), followerList);
                            followerView.setAdapter(adapter);*/
                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addItems(followerList);
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
            public void onFailure(Call<Following> call, Throwable t) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                followerView.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadFollowers() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("type","follower");
        hashMap.put("page",String.valueOf(currentPage));

       // Functions.showProgressDialog(getContext(),"Loading...");
        shimmer.startShimmer();
        Call<Following> call = ApiClient.getUserService().following_list(hashMap);
        call.enqueue(new Callback<Following>() {
            @Override
            public void onResponse(Call<Following> call, Response<Following> response) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                followerView.setVisibility(View.VISIBLE);
                if(response.isSuccessful()){
                  //  Functions.dismissProgressDialog();
                    if(response.body().isResult()){
                       // Functions.dismissProgressDialog();

                        if(!response.body().getFollow_list().isEmpty()){
                            //Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            PAGE_SIZE = response.body().getFollow_list().get(0).getLast_page();
                            followerList= response.body().getFollow_list().get(0).getData();
                            Log.d("=======>>", followerList.toString());
                            /*followingAdapter adapter = new followingAdapter(getContext(), followerList);
                            followerView.setAdapter(adapter);*/
                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addItems(followerList);
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
            public void onFailure(Call<Following> call, Throwable t) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                followerView.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }
}