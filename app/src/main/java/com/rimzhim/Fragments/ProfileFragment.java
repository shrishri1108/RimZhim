package com.rimzhim.Fragments;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Activities.SettingActivity;

import com.rimzhim.Activities.editProfileActivity;
import com.rimzhim.Activities.followerActivity;
import com.rimzhim.Activities.leaderBordActivity;
import com.rimzhim.Activities.notificationActivity;
import com.rimzhim.Adapters.contestAdapter;
import com.rimzhim.Adapters.galleryVideosAdapter;
import com.rimzhim.Adapters.tagAdapter;
import com.rimzhim.BuildConfig;
import com.rimzhim.Interfaces.onClickOnTagAdapter;
import com.rimzhim.Interfaces.videoItemClickInterface;
import com.rimzhim.MainMenu.MainMenuFragment;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.RootFragment;
import com.rimzhim.ModelClasses.ContestResponse.ContestsResponseModel;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.SimpleResponse;
import com.rimzhim.ModelClasses.model.DataItem;
import com.rimzhim.ModelClasses.model.TagsItem;
import com.rimzhim.ModelClasses.model.UserProfileResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PaginationListener;
import com.rimzhim.SimpleClasses.PermissionUtils;

import com.rimzhim.WatchProfileVideos.WatchUserProfileVideoActivity;
import com.rimzhim.dialogs.createVideoDialog;
import com.rimzhim.dialogs.videoMoreMenuDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends RootFragment implements videoItemClickInterface, onClickOnTagAdapter {
    TextView name, userName, bio, followerCount, followingCount, voteCount;
    ShapeableImageView userImg;
    ImageView backgroundImg, menuBtn, bellBtn, cameraBtn, coverCameraBtn, addVideosBtn;
    Button editProfileBtn;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    LinearLayout followerLL, followingLl;
    RecyclerView profileVideosRecView;
    TabLayout tabLayout;
    // ViewPager2 videoViewPager;
    PermissionUtils takePermissionUtils;
    ProgressDialog progressDialog;
    SwipeRefreshLayout refreshLayout;
    String userId;
    RecyclerView tagView, profileVideoRecyclerView;
    String CameraAction ="profile";
    ImageView coverImg;

    Dialog mdialog;
    ShimmerFrameLayout shimmer;
    String VideoType = "all";
    List<TagsItem> TagList = new ArrayList<>();



    GridLayoutManager gridLayoutManager;
    galleryVideosAdapter galleryVideosAdapter;
    List<DataItem> videosList = new ArrayList<>();
    private  int page =1;
    private int PageSize;
    private boolean isLoading;
    private boolean isLastPage;
    ProgressDialog dialog;

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        shimmer =(ShimmerFrameLayout)view.findViewById(R.id.shimmer);
        name =(TextView)view.findViewById(R.id.name);
        userName =(TextView)view.findViewById(R.id.userName);
        bio =(TextView)view.findViewById(R.id.bio);
        userImg =(ShapeableImageView)view.findViewById(R.id.imageView);
        menuBtn =(ImageView) view.findViewById(R.id.menuBtn);
        bellBtn =(ImageView) view.findViewById(R.id.bellBtn);
        addVideosBtn =(ImageView) view.findViewById(R.id.addVideosBtn);
        cameraBtn =(ImageView) view.findViewById(R.id.cameraBtn);
        coverCameraBtn =(ImageView) view.findViewById(R.id.coverCameraBtn);
        editProfileBtn =(Button) view.findViewById(R.id.editProfileBtn);
        followerLL =(LinearLayout) view.findViewById(R.id.followerLL);
        followingLl =(LinearLayout) view.findViewById(R.id.followingLl);
        tabLayout =(TabLayout) view.findViewById(R.id.tabLayout);
        // videoViewPager =(ViewPager2) view.findViewById(R.id.videoViewPager);
        followerCount =(TextView)view.findViewById(R.id.followerCount);
        followingCount =(TextView)view.findViewById(R.id.followingCount);
        voteCount =(TextView)view.findViewById(R.id.voteCount);
        tagView =(RecyclerView)view.findViewById(R.id.tagView);
        coverImg =(ImageView)view.findViewById(R.id.coverImg);
        profileVideoRecyclerView =(RecyclerView)view.findViewById(R.id.profileVideoRecyclerView);
        tagView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        mdialog = new Dialog(getContext());

        tabLayout.addTab(tabLayout.newTab().setText("All Video"));
        tabLayout.addTab(tabLayout.newTab().setText("Live Contests"));

        galleryVideosAdapter = new galleryVideosAdapter(getContext(),videosList,ProfileFragment.this);
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        profileVideoRecyclerView.setLayoutManager(gridLayoutManager);
        profileVideoRecyclerView.setHasFixedSize(true);
        profileVideoRecyclerView.setAdapter(galleryVideosAdapter);

        loadUserData();
        loadAllVideos();
        //  loadUserVideos();




        profileVideoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    refreshLayout.setEnabled(true);
                }
                else
                {

                  //  recyclerView.setNestedScrollingEnabled(false);
                    refreshLayout.setEnabled(false);
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




        takePermissionUtils=new PermissionUtils(getActivity(),mPermissionResult);
        refreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.refreshPage);




        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), editProfileActivity.class);
                startActivity(intent);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // drawerLayout.openDrawer(navigationView,true);
                loadDialog();
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    CameraAction = "all";
                    selectImage();
                }
                else
                {
                    takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                }
            }
        });

        followerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , followerActivity.class);
                intent.putExtra("msg","follower");
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        followingLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , followerActivity.class);
                intent.putExtra("msg","following");
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        bellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , notificationActivity.class);
                startActivity(intent);
            }
        });

        addVideosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createVideoDialog.openCreateVideoDialog(getContext());
            }
        });

        coverCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    CameraAction = "cover";
                    selectImgForCover();
                }
                else
                {
                    takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                }
            }
        });


        /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        profile_video_ViewPagerAdapter adapter= new profile_video_ViewPagerAdapter(fragmentManager,getLifecycle(),userId);
        videoViewPager.setAdapter(adapter);*/

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                // videoViewPager.setCurrentItem(tab.getPosition());
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

       /* videoViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });*/
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                loadUserData();
                videosList.clear();
                TagList.clear();
                if(tabLayout.getSelectedTabPosition() == 0){
                    page =1;
                    VideoType = "all";
                    tabLayout.getTabAt(0).select();
                    loadAllVideos();
                }else if(tabLayout.getSelectedTabPosition() == 1){
                    page =1;
                    VideoType = "contest_video";
                    tabLayout.getTabAt(1).select();
                    loadAllVideos();
                }
                galleryVideosAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }


    private void loadAllVideos() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("user_id", userId.trim());
        hashMap.put("type",VideoType);
        hashMap.put("page",String.valueOf(page));
        Functions.showProgressDialog(getContext());
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
                       // Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("=======", response.body().getUser_profile().getName().toString());
                       // setVideoData(response);
                        videosList.addAll(response.body().getVideos().getData());
                        galleryVideosAdapter.notifyDataSetChanged();
                        setTags(response.body().getTags());

                    }else {
                        Functions.dismissProgressDialog();
                       // Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTags(List<TagsItem> tags) {
        TagList.clear();
        TagList.addAll(tags);
      //  Log.d("=======>>", TagList.toString());
        tagAdapter tagAdapter = new tagAdapter(getContext(),TagList, ProfileFragment.this);
        tagView.setAdapter(tagAdapter);
    }


    private void setVideoData(Response<UserProfileResponse> response) {
       /* videosList = response.body().getVideos().getData();
        profileVideoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        galleryVideosAdapter adapter = new galleryVideosAdapter(getContext(), videosList, ProfileFragment.this);
        profileVideoRecyclerView.setAdapter(adapter);
        profileVideoRecyclerView.getAdapter().notifyDataSetChanged();*/
    }

    private void selectImgForCover() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery), "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        builder.setTitle(getString(R.string.add_photo_));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {
                    openCameraIntent();
                } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    resultCallbackForGallery.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });

        builder.show();


    }


    private void loadDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogStyle);
        bottomSheetDialog.setContentView(R.layout.profile_setting_dialog);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayoutCompat settings = bottomSheetDialog.findViewById(R.id.settings);
        LinearLayoutCompat contests = bottomSheetDialog.findViewById(R.id.contests);
        LinearLayoutCompat shareApp = bottomSheetDialog.findViewById(R.id.shareApp);
       /* TextView contests = bottomSheetDialog.findViewById(R.id.contests);
        TextView share = bottomSheetDialog.findViewById(R.id.share);*/

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        contests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(getContext(), leaderBordActivity.class);
                startActivity(intent);
            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out RimZhim app at: https://play.google.com/store/apps/rimzhim?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                bottomSheetDialog.dismiss();
            }
        });





        bottomSheetDialog.show();

    }


    private void loadUserData() {
        LoginResponse.User user = SharedPrefManager.getInstance(getContext()).getUser();
        userId=String.valueOf(user.getId());
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("user_id", userId);
        hashMap.put("type", "all");

        Call<UserProfileResponse> call = ApiClient.getUserService().OpenOtherUserProfile(hashMap);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                       // Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("=======", response.body().getUser_profile().getName().toString());
                        // setProfileData(response);

                        name.setText(response.body().getUser_profile().getName());
                        userName.setText(response.body().getUser_profile().getUser_name());
                        bio.setText(response.body().getUser_profile().getBio());
                        Glide.with(getContext()).load(response.body().getUser_profile().getImage()).placeholder(R.drawable.ic_user_icon).into(userImg);
                        Glide.with(getContext()).load(response.body().getUser_profile().getBackgroundImage()).placeholder(R.drawable.profile_back).into(coverImg);

                        followerCount.setText(Functions.getSuffix(String.valueOf(response.body().getUser_profile().getNo_of_followers())));
                        followingCount.setText(Functions.getSuffix(String.valueOf(response.body().getUser_profile().getNo_of_followings())));
                        voteCount.setText(Functions.getSuffix(String.valueOf(response.body().getUser_profile().getNo_of_votes())));

                        TagList.addAll(response.body().getTags());
                        Log.d("=======>>", TagList.toString());
                        tagAdapter tagAdapter = new tagAdapter(getContext(),TagList, ProfileFragment.this);
                        tagView.setAdapter(tagAdapter);


                    }else {



                    }
                }


            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {

                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // this method will show the dialog of selete the either take a picture form camera or pick the image from gallary
    private void selectImage() {
        final CharSequence[] options = {getString(R.string.take_photo),
                getString(R.string.choose_from_gallery), "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        builder.setTitle(getString(R.string.add_photo_));
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {
                    openCameraIntent();
                } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    resultCallbackForGallery.launch(intent);
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }
            }
        });

        builder.show();

    }
    String imageFilePath;

    ActivityResultLauncher<Intent> resultCallbackForCrop = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                        if(CameraAction.equals("cover")){
                            handleCropForCover(cropResult.getUri());
                            CameraAction ="profile";

                        }else {
                            handleCrop(cropResult.getUri());
                        }

                    }
                }
            });

    private void handleCropForCover(Uri uri) {
        File ImageFile = new File(uri.getPath());
        String tokenT =  loginResponsePref.getInstance(getContext()).getToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("background_image"), ImageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("background_image", ImageFile.getName(), requestFile);
        RequestBody token = RequestBody.create(MediaType.parse("token"), tokenT);

        Call<LoginResponse> responseCall = ApiClient.getUserService().uploadCoverImg(token,body);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){
                    if(response.body().getResult().equals("true")){
                        progressDialog.dismiss();
                        loginResponsePref.getInstance(getContext()).setToken(String.valueOf(response.body().getToken()),response.body().getMessage());
                      //  Toast.makeText(getContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                        LoginResponse.User user = new LoginResponse.User(
                                Integer.valueOf(response.body().getUser().getId()),
                                Integer.valueOf(response.body().getUser().getRoleId()),
                                response.body().getUser().getName(),
                                response.body().getUser().getEmail(),
                                response.body().getUser().getPhone(),
                                response.body().getUser().getStateId(),
                                response.body().getUser().getCityId(),
                                response.body().getUser().getUser_name(),
                                response.body().getUser().getReferralCode(),
                                response.body().getUser().getDob(),
                                response.body().getUser().getLongitude(),
                                response.body().getUser().getGender(),
                                response.body().getUser().getWallet(),
                                response.body().getUser().getReferUserId(),
                                response.body().getUser().getLatitude(),
                                Integer.valueOf(response.body().getUser().getStatus()),
                                response.body().getUser().getBio(),
                                response.body().getUser().getWinning(),
                                response.body().getUser().getBackground_image(),
                                response.body().getUser().getImage(),
                                Integer.valueOf(response.body().getUser().getIsDelete()),
                                response.body().getUser().getAddress(),
                                response.body().getUser().getCreatedAt(),
                                response.body().getUser().getUpdatedAt(),
                                response.body().getUser().getTimeOfBirth(),
                                response.body().getUser().getSocialId(),
                                response.body().getUser().getSocialId()
                        );
                        SharedPrefManager.getInstance(getContext()).userLogin(user);
                        // Glide.with(getContext()).load(user.getImage().toString()).into(userImg);
                        Glide.with(getContext()).load(user.getBackground_image().toString()).into(coverImg);

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();


            }
        });




    }

    ActivityResultLauncher<Intent> resultCallbackForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                       // Log.d("=======>>>>>>>>>>>", selectedImage.toString());
                        if(CameraAction.equals("cover")){
                            begingCropForCover(selectedImage);
                        }else {
                            beginCrop(selectedImage);
                        }
                    }
                }
            });

    private void begingCropForCover(Uri selectedImage) {

        Intent intent= CropImage.activity(selectedImage).setCropShape(CropImageView.CropShape.RECTANGLE).getIntent(getActivity());
        resultCallbackForCrop.launch(intent);

    }

    ActivityResultLauncher<Intent> resultCallbackForCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Matrix matrix = new Matrix();
                        try {
                            ExifInterface exif = new ExifInterface(imageFilePath);
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    matrix.postRotate(90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    matrix.postRotate(180);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    matrix.postRotate(270);
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
                        if(CameraAction.equals("cover")){
                            begingCropForCover(selectedImage);

                        }else {
                            beginCrop(selectedImage);
                        }
                    }
                }
            });


    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
            }
            if (photoFile != null) {
              /*  Uri photoURI = FileProvider.getUriForFile(getActivity(), getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/

                Uri photoURI=   FileProvider.getUriForFile(requireContext().getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                resultCallbackForCamera.launch(pictureIntent);
            }
        }
    }



    private File createImageFile() throws Exception {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.ENGLISH).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    String imageBas64Small,imageBas64Big;

    private void beginCrop(Uri source) {
        Intent intent= CropImage.activity(source).setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1,1).getIntent(getActivity());
        resultCallbackForCrop.launch(intent);
    }
    // get the image uri after the image crope
    private void handleCrop(Uri userimageuri) {
        Log.d("=========im", userimageuri.getPath().toString());

        File ImageFile = new File(userimageuri.getPath());

        Log.d("=========file", ImageFile.toString());

        mdialog.setContentView(R.layout.profile_pic_dialog);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button cancelBTN = mdialog.findViewById(R.id.cancelBTN);
        Button uploadBTN = mdialog.findViewById(R.id.uploadBTN);
        ShapeableImageView imageView = mdialog.findViewById(R.id.imageView);
        if(ImageFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(ImageFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }

        uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                callApiForImage(ImageFile);
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
            }
        });
        mdialog.show();









      /*  InputStream imageStream = null;
        try {
            imageStream = getActivity().getContentResolver().openInputStream(userimageuri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

        String path = userimageuri.getPath();
        Matrix matrix = new Matrix();
        ExifInterface exif = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        imageBas64Big= Functions.bitmapToBase64(rotatedBitmap);
        Bitmap converetdImage = getResizedBitmap(rotatedBitmap, Constants.PROFILE_IMAGE_SQUARE_SIZE);
        imageBas64Small = Functions.bitmapToBase64(converetdImage);
*/
        // callApiForImage(ImageFile);

    }

    private void callApiForImage(File imageFile) {
        String tokenT =  loginResponsePref.getInstance(getContext()).getToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
        RequestBody token = RequestBody.create(MediaType.parse("token"), tokenT);
        Call<LoginResponse> responseCall = ApiClient.getUserService().uploadImg(token,body);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getResult().equals("true")){
                        progressDialog.dismiss();
                        loginResponsePref.getInstance(getContext()).setToken(String.valueOf(response.body().getToken()),response.body().getMessage());
                       // Toast.makeText(getContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                        LoginResponse.User user = new LoginResponse.User(
                                Integer.valueOf(response.body().getUser().getId()),
                                Integer.valueOf(response.body().getUser().getRoleId()),
                                response.body().getUser().getName(),
                                response.body().getUser().getEmail(),
                                response.body().getUser().getPhone(),
                                response.body().getUser().getStateId(),
                                response.body().getUser().getCityId(),
                                response.body().getUser().getUser_name(),
                                response.body().getUser().getReferralCode(),
                                response.body().getUser().getDob(),
                                response.body().getUser().getLongitude(),
                                response.body().getUser().getGender(),
                                response.body().getUser().getWallet(),
                                response.body().getUser().getReferUserId(),
                                response.body().getUser().getLatitude(),
                                Integer.valueOf(response.body().getUser().getStatus()),
                                response.body().getUser().getBio(),
                                response.body().getUser().getWinning(),
                                response.body().getUser().getBackground_image(),
                                response.body().getUser().getImage(),
                                Integer.valueOf(response.body().getUser().getIsDelete()),
                                response.body().getUser().getAddress(),
                                response.body().getUser().getCreatedAt(),
                                response.body().getUser().getUpdatedAt(),
                                response.body().getUser().getTimeOfBirth(),
                                response.body().getUser().getSocialId(),
                                response.body().getUser().getSocialId()
                        );
                        SharedPrefManager.getInstance(getContext()).userLogin(user);
                        Glide.with(getContext()).load(user.getImage().toString()).into(userImg);

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });





    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    boolean allPermissionClear=true;
                    List<String> blockPermissionCheck=new ArrayList<>();
                    for (String key : result.keySet())
                    {
                        if (!(result.get(key)))
                        {
                            allPermissionClear=false;
                            blockPermissionCheck.add(Functions.getPermissionStatus(getActivity(),key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked"))
                    {
                        Functions.showPermissionSetting(getContext(),getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    }
                    else
                    if (allPermissionClear)
                    {
                        selectImage();
                    }

                }
            });




    @Override
    public void onResume() {
        super.onResume();
        //loadUserData();

    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(int position, int itemId) {

        switch (itemId){
            case R.id.moreMenus:
                openMoreMenuDialog(position,videosList.get(position).getVideo(),String.valueOf(videosList.get(position).getId()));
               // videoMoreMenuDialog.videoMoreMenuDialog(getContext(),videosList.get(position).getVideo(),String.valueOf(videosList.get(position).getId()));
                break;
                case R.id.videoCover:
                Intent intent = new Intent(getContext(), WatchUserProfileVideoActivity.class);
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

    private void openMoreMenuDialog(int position, String video, String id) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogStyle);
        bottomSheetDialog.setContentView(R.layout.video_menu_dialog);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout editLL = bottomSheetDialog.findViewById(R.id.editLL);
        LinearLayout uploadContestsLL = bottomSheetDialog.findViewById(R.id.uploadContestsLL);
        LinearLayout shareLL = bottomSheetDialog.findViewById(R.id.shareLL);
        LinearLayout deleteLL = bottomSheetDialog.findViewById(R.id.deleteLL);
        editLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        uploadContestsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabLayout.Tab contest = MainMenuFragment.tabLayout.getTabAt(1);
                contest.select();
                bottomSheetDialog.dismiss();
            }
        });

        shareLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out the video on RimZhim app at: " + video + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);


            }
        });
        deleteLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteVideo(position, id);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }

    private void DeleteVideo(int position, String id) {
        String tokenT =  loginResponsePref.getInstance(getContext()).getToken();
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("token",tokenT);
            hashMap.put("video_id",id);
            Functions.showProgressDialog(getContext());
            Call<SimpleResponse> call = ApiClient.getUserService().DeleteVideo(hashMap);
            call.enqueue(new Callback<SimpleResponse>() {
                @Override
                public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                    Functions.dismissProgressDialog();
                    if(response.isSuccessful()){
                        if(response.body().isResult()){
                            Functions.makeToast(getContext(),"Video Deleted");
                            videosList.remove(position);
                            galleryVideosAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleResponse> call, Throwable t) {
                    Functions.dismissProgressDialog();
                    Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
    }

    @Override
    public void onTagClick(int position) {
        int tag_id = TagList.get(position).getId();
        page=1;
        videosList.clear();
        loadVideosAccordingTags(tag_id);
    }
    private void loadVideosAccordingTags(int tag_id) {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("user_id", userId.trim());
        hashMap.put("type",VideoType);
        hashMap.put("page",String.valueOf(page));
        hashMap.put("tag_id",String.valueOf(tag_id));
        Functions.showProgressDialog(getContext());
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
                        // Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("=======", response.body().getUser_profile().getName().toString());
                        // setVideoData(response);
                        videosList.addAll(response.body().getVideos().getData());
                        galleryVideosAdapter.notifyDataSetChanged();
                        setTags(response.body().getTags());

                    }else {
                        Functions.dismissProgressDialog();
                        // Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}