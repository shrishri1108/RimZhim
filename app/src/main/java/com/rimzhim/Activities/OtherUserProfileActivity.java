package com.rimzhim.Activities;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Adapters.galleryVideosAdapter;
import com.rimzhim.Adapters.tagAdapter;
import com.rimzhim.Chat.ChatA;
import com.rimzhim.Chats.ChatActivity;
import com.rimzhim.Fragments.ProfileFragment;
import com.rimzhim.Interfaces.onClickOnTagAdapter;
import com.rimzhim.Interfaces.videoItemClickInterface;
import com.rimzhim.ModelClasses.BlockUserResponseModel;
import com.rimzhim.ModelClasses.FollowUserResponseModel;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.model.TagsItem;
import com.rimzhim.ModelClasses.model.UserProfileResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.WatchProfileVideos.WatchUserProfileVideoActivity;
import com.rimzhim.databinding.ActivityOtherUserProfileBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.rimzhim.ModelClasses.model.DataItem;


public class OtherUserProfileActivity extends AppCompatActivity implements videoItemClickInterface, onClickOnTagAdapter {
    ActivityOtherUserProfileBinding binding;
    String userId, UserName, userImg;
    public static    UserProfileResponse UserProfileResponse;
    List<DataItem> videosList = new ArrayList<>();
    List<TagsItem> TagList = new ArrayList<>();
    String VideoType = "all";
    GridLayoutManager gridLayoutManager;
    galleryVideosAdapter galleryVideosAdapter;
    private  int page =1;
    private int PageSize;
    private boolean isLoading;
    private boolean isLastPage;
    Dialog mdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_other_user_profile);
        Functions.whiteStatusBar(this);
        Intent intent = getIntent();
        userId =  intent.getStringExtra("userId");
        mdialog = new Dialog(this);
        LoginResponse.User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        if(userId.equals(String.valueOf(user.getId()))){
            binding.followBtn.setVisibility(View.INVISIBLE);
            binding.blockBtn.setVisibility(View.INVISIBLE);
            binding.massageBtn.setVisibility(View.INVISIBLE);
        }
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("All Video"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Live Contests"));

        binding.tagView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false));

        galleryVideosAdapter = new galleryVideosAdapter(this,videosList, OtherUserProfileActivity.this);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        binding.profileVideoRecyclerView.setLayoutManager(gridLayoutManager);
        binding.profileVideoRecyclerView.setHasFixedSize(true);
        binding.profileVideoRecyclerView.setAdapter(galleryVideosAdapter);


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        VideoType ="all";
                        page =1;
                        videosList.clear();
                        loadAllVideos();

                        break;
                    case 1:
                        VideoType ="contest_video";
                        page =1;
                        videosList.clear();
                        loadAllVideos();
                        break;

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

      /*  binding.videoViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });*/

        binding.blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.blockBtn.getText().toString().equals("Block")){
                    OpenBlockDialog();
                }else {
                    blockUser();
                }
            }
        });


        binding.massageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), ChatA.class);
                /*intent1.putExtra("userName",UserName);
                intent1.putExtra("userId",userId);
                intent.putExtra("userImg",userImg);*/

                intent1.putExtra("user_name",UserName);
                intent1.putExtra("user_id",userId);
                intent1.putExtra("user_pic",userImg);

               /* receiverId = getIntent().getStringExtra("user_id");
                receiverName = getIntent().getStringExtra("user_name");
                receiverPic = getIntent().getStringExtra("user_pic");*/

                startActivity(intent1);
            }
        });

        binding.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FollowUser();
            }
        });

        binding.followerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), followerActivity.class);
                intent.putExtra("msg","follower");
                intent.putExtra("id",userId);
                startActivity(intent);

            }
        });

        binding.followingLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , followerActivity.class);
                intent.putExtra("msg","following");
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        loadProfile();
        loadAllVideos();



        binding.profileVideoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // refreshLayout.setRefreshing(false);
                int visibleItem = gridLayoutManager.getChildCount();
                int totalItem = gridLayoutManager.getItemCount();
                int firstVisibleItemPos = gridLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPos = gridLayoutManager.findLastVisibleItemPosition();

                if (firstVisibleItemPos == 0)
                {
                    // recyclerView.setNestedScrollingEnabled(true);
                    binding.refreshPage.setEnabled(true);
                }
                else
                {

                    //  recyclerView.setNestedScrollingEnabled(false);
                    binding.refreshPage.setEnabled(false);
                }

                if(! isLoading && ! isLastPage){
                    if((visibleItem + firstVisibleItemPos >=totalItem) && firstVisibleItemPos >= 0 && totalItem >=PageSize){
                        page++;
                        loadAllVideos();
                    }
                }
            }
        });


        binding.refreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                loadProfile();
                videosList.clear();
                TagList.clear();
                if(binding.tabLayout.getSelectedTabPosition() == 0){
                    page =1;
                    VideoType = "all";
                    binding.tabLayout.getTabAt(0).select();
                    loadAllVideos();
                }else if(binding.tabLayout.getSelectedTabPosition() == 1){
                    page =1;
                    VideoType = "contest_video";
                    binding.tabLayout.getTabAt(1).select();
                    loadAllVideos();
                }
                galleryVideosAdapter.notifyDataSetChanged();
                binding.refreshPage.setRefreshing(false);
            }
        });

    }

    private void OpenBlockDialog() {
        mdialog.setContentView(R.layout.dialog_block);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button yes = mdialog.findViewById(R.id.YesBTN);
        Button no = mdialog.findViewById(R.id.NoBTN);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                blockUser();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
            }
        });
        mdialog.show();
    }


    private void loadAllVideos() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("user_id", userId.trim());
        hashMap.put("type",VideoType);
        hashMap.put("page",String.valueOf(page));
        Functions.showProgressDialog(OtherUserProfileActivity.this);
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
                      //  Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("=======", response.body().getUser_profile().getName().toString());
                        videosList.addAll(response.body().getVideos().getData());
                        galleryVideosAdapter.notifyDataSetChanged();
                        setTags(response.body().getTags());
                       // setVideoData(response);
                    }else {
                        Functions.dismissProgressDialog();
                        Functions.makeToast(getApplicationContext(),response.body().getMessage());
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
        //  Log.d("=======>>", TagList.toString());
        tagAdapter tagAdapter = new tagAdapter(getApplicationContext(),TagList, OtherUserProfileActivity.this);
        binding.tagView.setAdapter(tagAdapter);
    }

    private void setVideoData(Response<UserProfileResponse> response) {
       /* videosList = response.body().getVideos().getData();
        binding.profileVideoRecyclerView.setLayoutManager(new GridLayoutManager(OtherUserProfileActivity.this,2));
        galleryVideosAdapter adapter = new galleryVideosAdapter(getApplicationContext(), videosList, OtherUserProfileActivity.this);
        binding.profileVideoRecyclerView.setAdapter(adapter);
        binding.profileVideoRecyclerView.getAdapter().notifyDataSetChanged();*/
    }


    public void   loadProfile() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("user_id", userId);
        hashMap.put("type", "all");

        //  Functions.showProgressDialog(OtherUserProfileActivity.this,"Loading..");
        Call<UserProfileResponse> call = ApiClient.getUserService().OpenOtherUserProfile(hashMap);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful()){
                    //Functions.dismissProgressDialog();
                    if(response.body().isResult()){
                        //Functions.dismissProgressDialog();
                       // Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("=======", response.body().getUser_profile().getName().toString());
                        setProfileData(response);
                        UserProfileResponse = response.body();
                    }
                }

            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                // Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setProfileData(Response<UserProfileResponse> response) {
        UserName = response.body().getUser_profile().getUser_name();
        userImg = response.body().getUser_profile().getImage();
        Log.d("=====>>>///", userImg);

        if(response.body().getUser_profile().getIs_block() == 1){
            binding.followBtn.setVisibility(View.GONE);
            binding.massageBtn.setVisibility(View.GONE);
            binding.blockBtn.setText("Unblock");
            binding.followerCount.setVisibility(View.GONE);
            binding.followingCount.setVisibility(View.GONE);
            binding.voteCount.setVisibility(View.GONE);
            binding.followerLL.setClickable(false);
            binding.followingLl.setClickable(false);
            binding.tabLayout.setVisibility(View.GONE);
            binding.tagView.setVisibility(View.GONE);
            binding.profileVideoRecyclerView.setVisibility(View.GONE);
        }
        if(String.valueOf(response.body().getUser_profile().getIs_following()).trim().equals("1")){
            Drawable buttonDrawable = binding.followBtn.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#FF000000"));
            binding.followBtn.setBackground(buttonDrawable);
            binding.followBtn.setText("Following");
        }else {
            Drawable buttonDrawable = binding.followBtn.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
            binding.followBtn.setBackground(buttonDrawable);
            binding.followBtn.setText("Follow");
        }

        binding.userName.setText(response.body().getUser_profile().getUser_name());
        binding.name.setText(response.body().getUser_profile().getName());
        binding.bio.setText(response.body().getUser_profile().getBio());
        Glide.with(getApplicationContext()).load(response.body().getUser_profile().getImage()).placeholder(R.drawable.ic_user_icon).into(binding.imageView);
        binding.followerCount.setText(Functions.getSuffix(String.valueOf(response.body().getUser_profile().getNo_of_followers())));
        Log.d("=======>>>", String.valueOf(response.body().getUser_profile().getNo_of_followers()));
        binding.followingCount.setText(Functions.getSuffix(String.valueOf(response.body().getUser_profile().getNo_of_followings())));
        binding.voteCount.setText(Functions.getSuffix(String.valueOf(response.body().getUser_profile().getNo_of_votes())));
        Glide.with(getApplicationContext()).load(response.body().getUser_profile().getBackgroundImage()).placeholder(R.drawable.profile_back).into(binding.imageView4);


        binding.tagView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false));
        tagAdapter tagAdapter = new tagAdapter(getApplicationContext(),response.body().getTags(),this);
        binding.tagView.setAdapter(tagAdapter);

       /* FragmentManager fragmentManager = getSupportFragmentManager();
        adapter= new profile_video_ViewPagerAdapter(fragmentManager,getLifecycle(),String.valueOf(response.body().getUser_profile().getId()));
        binding.videoViewPager.setAdapter(adapter);*/
    }
    private void blockUser() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("user_id",userId);
        Call<BlockUserResponseModel> call = ApiClient.getUserService().BlockUser(hashMap);
        call.enqueue(new Callback<BlockUserResponseModel>() {
            @Override
            public void onResponse(Call<BlockUserResponseModel> call, Response<BlockUserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){

                        if(response.body().getMessage().trim().equals("Blocked Successfully")){
                            binding.followBtn.setVisibility(View.GONE);
                            binding.massageBtn.setVisibility(View.GONE);
                            binding.blockBtn.setText("Unblock");
                            binding.followerCount.setVisibility(View.GONE);
                            binding.followingCount.setVisibility(View.GONE);
                            binding.voteCount.setVisibility(View.GONE);
                            binding.followerLL.setClickable(false);
                            binding.followingLl.setClickable(false);
                            binding.tabLayout.setVisibility(View.GONE);
                            binding.tagView.setVisibility(View.GONE);
                            binding.profileVideoRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }else {
                            binding.followBtn.setVisibility(View.VISIBLE);
                            binding.massageBtn.setVisibility(View.VISIBLE);
                            binding.blockBtn.setText("Block");
                            binding.followerCount.setVisibility(View.VISIBLE);
                            binding.followingCount.setVisibility(View.VISIBLE);
                            binding.voteCount.setVisibility(View.VISIBLE);
                            binding.followerLL.setClickable(true);
                            binding.followingLl.setClickable(true);
                            binding.tabLayout.setVisibility(View.VISIBLE);
                            binding.tagView.setVisibility(View.VISIBLE);
                            binding.profileVideoRecyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<BlockUserResponseModel> call, Throwable t) {
                Toast.makeText(OtherUserProfileActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void FollowUser() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("follower_id",userId);
        Call<FollowUserResponseModel> call = ApiClient.getUserService().FollowUser(hashMap);
        call.enqueue(new Callback<FollowUserResponseModel>() {
            @Override
            public void onResponse(Call<FollowUserResponseModel> call, Response<FollowUserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        Toast.makeText(getApplicationContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        if(response.body().getMessage().trim().equals("Follow Successfully")){

                            Drawable buttonDrawable = binding.followBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#FF000000"));
                            binding.followBtn.setBackground(buttonDrawable);
                            binding.followBtn.setText("Following");

                        }else {
                            Drawable buttonDrawable = binding.followBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
                            binding.followBtn.setBackground(buttonDrawable);
                            binding.followBtn.setText("Follow");
                        }

                    }else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onFailure(Call<FollowUserResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(int position, int itemId) {
        switch (itemId){

            case R.id.videoCover:
                Intent intent = new Intent(getApplicationContext(), WatchUserProfileVideoActivity.class);
                intent.putExtra("position",String.valueOf(position));
                intent.putExtra("userId",String.valueOf(videosList.get(position).getUser_id()));
                intent.putExtra("videoType", VideoType.trim());
                intent.putExtra("page", page);
                intent.putExtra("videoId", videosList.get(position).getId());
                Log.d("========>", String.valueOf(videosList.get(position).getUser_id()));
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onTagClick(int position) {
        String tag = TagList.get(position).getName();
        loadVideoAccordingTag(tag);
    }

    private void loadVideoAccordingTag(String tag) {



    }




}