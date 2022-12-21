package com.rimzhim.Chats;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rimzhim.BuildConfig;
import com.rimzhim.Interfaces.StickerOnClick;
import com.rimzhim.ModelClasses.GetChats.DataItem;
import com.rimzhim.ModelClasses.GetChats.getChatResponse;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.UserProfile.User;
import com.rimzhim.ModelClasses.submitChatResponse;
import com.rimzhim.ModelClasses.videoListModel.data;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PaginationListener;
import com.rimzhim.SimpleClasses.PermissionUtils;
import com.rimzhim.Sticker.StickerAdapter;
import com.rimzhim.Sticker.StickersModel;
import com.rimzhim.databinding.ActivityChatBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, StickerOnClick {
    ActivityChatBinding binding;
    List<DataItem> chats = new ArrayList<>();
  //  private AdapterChat adapter;
    String UserName, UserID, userImg;
   // LinearLayoutManager layout;
  /*  private  int page = 1;
    private int PageSize;
    private boolean isLoading;
    private boolean isLastPage;*/
    String sender_Id;
    PermissionUtils takePermissionUtils;
    Dialog mdialog;

    Timer timer;
    private int currentPage = 1;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;
    ChatingAdapter adapter;

    boolean isFirstSeen = true;
    Thread thread;

    JobScheduler scheduler;
    ScheduledExecutorService execService;

    LinearLayoutManager layout;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        Functions.whiteStatusBar(this);
        takePermissionUtils=new PermissionUtils(this,mPermissionResult);
        Intent intent = getIntent();
        UserName=intent.getStringExtra("userName");
        UserID=intent.getStringExtra("userId");
        userImg=intent.getStringExtra("userImg");
        Glide.with(getApplicationContext()).load(userImg).placeholder(R.drawable.ic_user_icon).into(binding.profileImg);
        Log.d("=========>", UserID);
        binding.nameptv.setText(UserName);
        binding.sendmsg.setOnClickListener(this);

        layout = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        binding.chatrecycle.setLayoutManager(layout);
        binding.chatrecycle.setHasFixedSize(false);
        layout.setReverseLayout(true);
        layout.setStackFromEnd(true);
        adapter = new ChatingAdapter(getApplicationContext(),new ArrayList<>(),userImg);
        binding.chatrecycle.setAdapter(adapter);

        getChats();

        LoginResponse.User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        sender_Id = String.valueOf(user.getId());





        binding.galleryBtn.setOnClickListener(view -> {
            if (takePermissionUtils.isStorageCameraPermissionGranted()) {
               selectImage();

            }
            else
            {
                takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
            }
        });

        binding.cameraBtn.setOnClickListener(view -> {
            if (takePermissionUtils.isStorageCameraPermissionGranted()) {
               openCamera();
            }
            else
            {
                takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
            }

        });

        binding.emojiBtn.setOnClickListener(view -> {
           // stickerDialog.openStickerDialog(ChatActivity.this);
            openStickerDialog(ChatActivity.this);

        });

        binding.BackBtn.setOnClickListener(view -> {
            finish();
        });


        binding.nameptv.setOnClickListener(view -> {
            Functions.OpenOtherProfile(ChatActivity.this, UserID);
        });

        binding.chatrecycle.addOnScrollListener(new PaginationListener(layout) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage ++;
                getChats();

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



       /* thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {
                               // getRecentChat();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();*/

      /* execService =   Executors.newScheduledThreadPool(5);
        execService.scheduleAtFixedRate(()->{
            Log.d("runner=====", "run");

            getRecentChats();

        }, 0, 3000, TimeUnit.MILLISECONDS);

*/


    }

    private void getRecentChats(){
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("user_id",UserID);
        isLoading = true;
        Call<getChatResponse> call = ApiClient.getUserService().getChats(hashMap);
        call.enqueue(new Callback<getChatResponse>() {
            @Override
            public void onResponse(Call<getChatResponse> call, Response<getChatResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body().isResult()) {
                        if(!response.body().getChats().getData().isEmpty()){
                            chats.clear();
                            layout.setStackFromEnd(true);

                            chats = response.body().getChats().getData();
                            adapter = new ChatingAdapter(getApplicationContext(),chats,userImg);
                            binding.chatrecycle.scrollToPosition(0);
                            binding.chatrecycle.setAdapter(adapter);


                        }
                    }else {

                    }
                }
            }
            @Override
            public void onFailure(Call<getChatResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }



    private void getMassages(List<DataItem> dataItems){
        for (DataItem item : dataItems) {
            adapter.addAtBottom(item);
        }
    }



    private void openCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
            }
            if (photoFile != null) {
              /*  Uri photoURI = FileProvider.getUriForFile(getActivity(), getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/

                Uri photoURI=   FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                resultCallbackForCamera.launch(pictureIntent);
            }
        }
    }

    private void selectImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultCallbackForGallery.launch(galleryIntent);
    }

    private void begingCrop(Uri selectedImage) {
        Intent intent= CropImage.activity(selectedImage).setCropShape(CropImageView.CropShape.RECTANGLE)
                .getIntent(ChatActivity.this);
        resultCallbackForCrop.launch(intent);
    }

    ActivityResultLauncher<Intent> resultCallbackForCrop = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                        sendPictures(cropResult.getUri());
                       // openImageActivity(cropResult.getUri());


                    }
                }
            });


    private void getChats() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("user_id",UserID);
        hashMap.put("page",String.valueOf(currentPage));
        isLoading = true;
        Call<getChatResponse> call = ApiClient.getUserService().getChats(hashMap);
        call.enqueue(new Callback<getChatResponse>() {
            @Override
            public void onResponse(Call<getChatResponse> call, Response<getChatResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body().isResult()) {
                        if(!response.body().getChats().getData().isEmpty()){
                            PAGE_SIZE = response.body().getChats().getLast_page();
                            chats = response.body().getChats().getData();
                            if (currentPage != PAGE_START) adapter.removeLoading();
                            adapter.addItems(chats);
                            if (currentPage < PAGE_SIZE) {
                                adapter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;

                            if(isFirstSeen){
                                binding.chatrecycle.smoothScrollToPosition(0);
                                isFirstSeen = false;

                            }


                        }
                    }else {

                    }
                }
            }
            @Override
            public void onFailure(Call<getChatResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendmsg:
                submitChat();
                binding.messaget.getText().clear();
                adapter.notifyDataSetChanged();
                break;
        }

    }

    private void submitChat() {
        final String msg = binding.messaget.getText().toString().trim();
        if(TextUtils.isEmpty(msg)){
            return;
        }

        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("text",msg);
        hashMap.put("reciever_id",UserID);

        Call<submitChatResponse> call = ApiClient.getUserService().submitChat(hashMap);
        call.enqueue(new Callback<submitChatResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<submitChatResponse> call, Response<submitChatResponse> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isResult()){
                        //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        LoginResponse.User user  = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                        String senderId = String.valueOf(user.getId());
                        DataItem item = new DataItem();
                        item.setSender_id(senderId);
                        item.setReciever_id(UserID);
                        item.setText(msg);
                        item.setFile_type("text");
                        item.setIs_file("0");
                        item.setTime(Functions.getTime());
                        adapter.addAtBottom(item);
                        binding.chatrecycle.smoothScrollToPosition(0);
                    }
                }
            }
            @Override
            public void onFailure(Call<submitChatResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
                            blockPermissionCheck.add(Functions.getPermissionStatus(ChatActivity.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked"))
                    {
                        Functions.showPermissionSetting(getApplicationContext(),getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    }
                    else
                    if (allPermissionClear)
                    {
                        selectImage();
                    }

                }
            });

    ActivityResultLauncher<Intent> resultCallbackForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                    //sendPictures(selectedImage);
                        begingCrop(selectedImage);
                    }
                }
            });

    String imageFilePath;
    private File createImageFile() throws Exception {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.ENGLISH).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
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
                        sendPictures(selectedImage);
                    }
                }
            });

    private void sendPictures(Uri selectedImage) {
        File ImageFile = new File(selectedImage.getPath());
        Log.d("path======", ImageFile.toString());
        String tokenT =  loginResponsePref.getInstance(getApplicationContext()).getToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("text"), ImageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("text", ImageFile.getName(), requestFile);
        RequestBody token = RequestBody.create(MediaType.parse("token"), tokenT);
        RequestBody reciever_id = RequestBody.create(MediaType.parse("reciever_id"), UserID);
        Functions.showProgressDialog(ChatActivity.this);
        Call<submitChatResponse> call = ApiClient.getUserService().sendImage(token, body, reciever_id);
        call.enqueue(new Callback<submitChatResponse>() {
            @Override
            public void onResponse(Call<submitChatResponse> call, Response<submitChatResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                       /* "id": 102,
                                "group_id": null,
                                "sender_id": "1",
                                "reciever_id": "115",
                                "is_file": "1",
                                "file_type": "jpg",
                                "text": "https://rimjhim.appmantra.live//public/storage/chats/1_user_chats_1658376833.jpg",
                                "is_read": "0",
                                "created_at": "2022-07-21T04:13:53.000000Z",
                                "updated_at": "2022-07-21T04:13:53.000000Z",
                                "time": "09:43 AM",
                                "day": "Thu"*/

                        LoginResponse.User user  = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                        String senderId = String.valueOf(user.getId());
                        DataItem item = new DataItem();
                        item.setSender_id(senderId);
                        item.setReciever_id(UserID);
                        item.setText(ImageFile.getAbsolutePath());
                        item.setFile_type(" ");
                        item.setIs_file("1");
                        item.setTime(Functions.getTime());
                        adapter.addAtBottom(item);
                        binding.chatrecycle.smoothScrollToPosition(0);

                    }
                }

            }

            @Override
            public void onFailure(Call<submitChatResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private   void openStickerDialog(Context context){

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
        bottomSheetDialog.setContentView(R.layout.sticker_dialog);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RecyclerView StickerView =bottomSheetDialog.findViewById(R.id.stickersView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3);
        StickerView.setLayoutManager(gridLayoutManager);
        StickerAdapter adapter = new StickerAdapter(context, AddStickers(context),ChatActivity.this);
        StickerView.setAdapter(adapter);
        bottomSheetDialog.show();
    }

    private static ArrayList<StickersModel> AddStickers(Context context) {
        ArrayList<StickersModel> list = new ArrayList<>();
        StickersModel stickersModel = new StickersModel();
        stickersModel.setSticker(R.raw.giphy);
        stickersModel.setName("ha");
        list.add(stickersModel);

        StickersModel stickersModel1 = new StickersModel();
        stickersModel1.setSticker(R.raw.giphy2);
        stickersModel1.setName("ha");
        list.add(stickersModel1);

        StickersModel stickersModel2 = new StickersModel();
        stickersModel2.setSticker(R.raw.giphy3);
        stickersModel2.setName("ha");
        list.add(stickersModel2);

        StickersModel stickersModel3 = new StickersModel();
        stickersModel3.setSticker(R.raw.giphy4);
        stickersModel3.setName("ha");
        list.add(stickersModel3);

        StickersModel stickersModel4 = new StickersModel();
        stickersModel4.setSticker(R.raw.love1);
        stickersModel4.setName("ha");
        list.add(stickersModel4);

        StickersModel stickersModel5 = new StickersModel();
        stickersModel5.setSticker(R.raw.love2);
        stickersModel5.setName("ha");
        list.add(stickersModel5);

        StickersModel stickersModel6 = new StickersModel();
        stickersModel6.setSticker(R.raw.h1);
        stickersModel6.setName("ha");
        list.add(stickersModel6);

        StickersModel stickersModel7 = new StickersModel();
        stickersModel7.setSticker(R.raw.h2);
        stickersModel7.setName("ha");
        list.add(stickersModel7);

        StickersModel stickersModel8 = new StickersModel();
        stickersModel8.setSticker(R.raw.h3);
        stickersModel8.setName("ha");
        list.add(stickersModel8);

        StickersModel stickersModel9 = new StickersModel();
        stickersModel9.setSticker(R.raw.d1);
        stickersModel9.setName("ha");
        list.add(stickersModel9);

        StickersModel stickersModel10 = new StickersModel();
        stickersModel10.setSticker(R.raw.d2);
        stickersModel10.setName("ha");
        list.add(stickersModel10);

        StickersModel stickersModel11 = new StickersModel();
        stickersModel11.setSticker(R.raw.d3);
        stickersModel11.setName("ha");
        list.add(stickersModel11);



        return list;

    }

    @Override
    public void onClick(int position) {
         /* int src =  AddStickers(ChatActivity.this).get(position).getSticker();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + src);
        sendPictures(uri);
        Log.d("=========>>>>>", uri.toString());*/

    }

    private void openImageActivity(Uri uri){
       Intent intent = new Intent(getApplicationContext(),SendImgActivity.class);
       intent.putExtra("img", uri);
       resultCallbackForSendImage.launch(intent);
    }

    ActivityResultLauncher<Intent> resultCallbackForSendImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String s=  data.getStringExtra("img");
                        Log.d("result=======", s);

                    }
                }
            });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  execService.shutdown();

    }


        @Override
        protected void onDestroy() {
            super.onDestroy();
            Thread.interrupted();
            //t.interrupted();
        }

}