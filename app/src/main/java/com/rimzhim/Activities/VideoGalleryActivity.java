package com.rimzhim.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rimzhim.Adapters.contestVideoGalleryAdapter;
import com.rimzhim.Adapters.galleryVideosAdapter;
import com.rimzhim.Adapters.tagAdapter;
import com.rimzhim.Fragments.ProfileFragment;
import com.rimzhim.Interfaces.onClickOnTagAdapter;
import com.rimzhim.Interfaces.videoItemClickInterface;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.model.DataItem;
import com.rimzhim.ModelClasses.model.TagsItem;
import com.rimzhim.ModelClasses.model.UserProfileResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityVideoGalleryBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoGalleryActivity extends AppCompatActivity implements videoItemClickInterface , onClickOnTagAdapter{

    GridLayoutManager gridLayoutManager;
    contestVideoGalleryAdapter galleryVideosAdapter;
    List<DataItem> videosList = new ArrayList<>();
    private  int page =1;
    private int PageSize;
    private boolean isLoading;
    private boolean isLastPage;
    ActivityVideoGalleryBinding binding;
    List<TagsItem> TagList = new ArrayList<>();
    ArrayList<String> selectedVideoList = new ArrayList<String>();

    int contestId, entryAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_gallery);
        Functions.whiteStatusBar(this);
        Intent fromContest = getIntent();
        entryAmount = fromContest.getIntExtra("entryAmount",0);
        contestId = fromContest.getIntExtra("contestId",0);

        binding.tagView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false));
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        binding.profileVideoRecyclerView.setLayoutManager(gridLayoutManager);
        binding.profileVideoRecyclerView.setHasFixedSize(true);
        galleryVideosAdapter = new contestVideoGalleryAdapter(getApplicationContext(),videosList, VideoGalleryActivity.this);
        binding.profileVideoRecyclerView.setAdapter(galleryVideosAdapter);


        loadAllVideos();

        binding.profileVideoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem = gridLayoutManager.getChildCount();
                int totalItem = gridLayoutManager.getItemCount();
                int firstVisibleItemPos = gridLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPos = gridLayoutManager.findLastVisibleItemPosition();

                if (firstVisibleItemPos == 0)
                {
                    // recyclerView.setNestedScrollingEnabled(true);
                    //refreshLayout.setEnabled(true);
                }
                else
                {

                    //  recyclerView.setNestedScrollingEnabled(false);
                   // refreshLayout.setEnabled(false);
                }

                if(! isLoading && ! isLastPage){
                    if((visibleItem + firstVisibleItemPos >=totalItem) && firstVisibleItemPos >= 0 && totalItem >=PageSize){
                        page++;
                        Log.d("page====", String.valueOf(page));
                        loadAllVideos();
                    }
                }
            }
        });

        binding.JoinBTN.setOnClickListener(view -> {
            selectedVideoList.clear();
            Functions.showProgressDialog(VideoGalleryActivity.this);
            for(int i=0; i<videosList.size(); i++){
                if(videosList.get(i).isSelected()){
                   selectedVideoList.add(String.valueOf(videosList.get(i).getId()));
                }
            }
            if(selectedVideoList.isEmpty()){
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),"Video Gallery is Empty",Toast.LENGTH_SHORT).show();
                return;
            }

            entryAmount = entryAmount*selectedVideoList.size();

            StringBuffer sb = new StringBuffer();

            for (String s : selectedVideoList) {
                if(sb.length() > 0){
                    sb.append(",");
                }
                sb.append(s);

            }
            Functions.dismissProgressDialog();
            String list = sb.toString();
            Log.d("list======>", list);
            Intent intent = new Intent(getApplicationContext(), TotalBalanceActivity.class);
            intent.putExtra("list",list);
            intent.putExtra("entryAmount",entryAmount);
            intent.putExtra("contestId",contestId);
            startActivity(intent);
            finish();
        });

        binding.changebackIV.setOnClickListener(view -> {
            finish();
        });

    }

    private void loadAllVideos() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        LoginResponse.User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String userId=String.valueOf(user.getId());
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("user_id", userId.trim());
        hashMap.put("page",String.valueOf(page));
        hashMap.put("type","all");
        Functions.showProgressDialog(VideoGalleryActivity.this);
        isLoading = true;
        Call<UserProfileResponse> call = ApiClient.getUserService().OpenOtherUserProfile(hashMap);
        call.enqueue(new Callback<UserProfileResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful()){
                    Functions.dismissProgressDialog();
                    if(response.body().isResult()){
                        Functions.dismissProgressDialog();
                        PageSize = response.body().getVideos().getLast_page();
                       // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d(">>>>>>====", response.body().getUser_profile().getName().toString());
                        // setVideoData(response);
                        videosList.addAll(response.body().getVideos().getData());
                        galleryVideosAdapter.notifyDataSetChanged();
                        setTags(response.body().getTags());

                    }else {
                        Functions.dismissProgressDialog();
                       // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                Functions.dismissProgressDialog();
                isLoading = false;
                if(page < PageSize){
                    isLastPage = false;
                }else isLastPage = true;

            }
            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setTags(List<TagsItem> tags) {
        TagList.clear();
        TagList.addAll(tags);
        tagAdapter tagAdapter = new tagAdapter(getApplicationContext(),TagList, VideoGalleryActivity.this);
         binding.tagView.setAdapter(tagAdapter);
    }

    @Override
    public void onClick(int position, int itemId) {



    }

    @Override
    public void onTagClick(int position) {


    }
}