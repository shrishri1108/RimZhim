package com.rimzhim.Activities;

import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_SIZE;
import static com.rimzhim.SimpleClasses.PaginationListener.PAGE_START;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rimzhim.Adapters.InboxAdapter;
import com.rimzhim.BuildConfig;
import com.rimzhim.ModelClasses.GetChats.getChatResponse;
import com.rimzhim.ModelClasses.submitChatResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PaginationListener;
import com.rimzhim.SimpleClasses.PermissionUtils;
import com.rimzhim.SimpleClasses.Variables;
import com.rimzhim.databinding.ActivityInboxBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rimzhim.ModelClasses.GetChats.DataItem;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxActivity extends AppCompatActivity {
    ActivityInboxBinding binding;
    String UserName, UserID;
    List<DataItem> chats = new ArrayList<>();
    List<DataItem> temp = new ArrayList<>();
    PermissionUtils takePermissionUtils;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    //  private int totalPage = PAGE_SIZE;
    private boolean isLoading = false;
    private InboxAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inbox);
        Functions.whiteStatusBar(this);
        Intent intent = getIntent();
        UserName=intent.getStringExtra("userName");
        UserID=intent.getStringExtra("userId");
        Log.d("=========>", UserID);
        binding.UserName.setText(UserName);

        Functions.makeDirectry(Functions.getAppFolder(this)+ Variables.APP_HIDED_FOLDER);
        Functions.makeDirectry(Functions.getAppFolder(this)+Variables.DRAFT_APP_FOLDER);

        takePermissionUtils=new PermissionUtils(InboxActivity.this,mPermissionResult);
        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setStackFromEnd(true);
        // layout.setReverseLayout(true);
        binding.chatlist.setLayoutManager(layout);
        binding.chatlist.setHasFixedSize(false);
        //layout.setStackFromEnd(true);
       // adapter = new InboxAdapter(new ArrayList<>());
        binding.chatlist.setAdapter(adapter);
        binding.submitChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.msgText.getText().toString().trim().isEmpty()){
                    //submitChat(binding.msgText.getText().toString().trim());
                }
            }
        });
        binding.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    openCameraIntent();
                }
                else
                {
                    takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                }
            }
        });

        binding.galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    resultCallbackForGallery.launch(intent);
                }
                else
                {
                    takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                }
            }
        });
         getChats();


        binding.chatlist.addOnScrollListener(new PaginationListener(layout) {
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



    }

    private void getChats() {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("user_id",UserID);
        hashMap.put("page",String.valueOf(currentPage));

        Call<getChatResponse> call = ApiClient.getUserService().getChats(hashMap);
        call.enqueue(new Callback<getChatResponse>() {
            @Override
            public void onResponse(Call<getChatResponse> call, Response<getChatResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        PAGE_SIZE = response.body().getChats().getLast_page();
                        chats = response.body().getChats().getData();
                      /*  if (currentPage != PAGE_START) adapter.removeLoading();
                        adapter.addItems(chats);
                        if (currentPage < PAGE_SIZE) {
                            adapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;*/
                    }
                }
            }
            @Override
            public void onFailure(Call<getChatResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
   /* private void submitChat(String chat) {
        String token = loginResponsePref.getInstance(getApplicationContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("text",chat);
        hashMap.put("reciever_id",UserID);
        Call<submitChatResponse> call = ApiClient.getUserService().submitChat(hashMap);
        call.enqueue(new Callback<submitChatResponse>() {
            @Override
            public void onResponse(Call<submitChatResponse> call, Response<submitChatResponse> response) {

                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //getChats();
                        binding.msgText.getText().clear();
                    }
                }
            }
            @Override
            public void onFailure(Call<submitChatResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    String imageFilePath;
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
                            blockPermissionCheck.add(Functions.getPermissionStatus(InboxActivity.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked"))
                    {
                        Functions.showPermissionSetting(getApplicationContext(),getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    }
                    else
                    if (allPermissionClear)
                    {
                        //selectImage();
                    }
                }
            });

    // below three method is related with taking the picture from camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(InboxActivity.this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
            }
            if (photoFile != null) {
               /* Uri photoURI = FileProvider.getUriForFile(getActivity(), getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/

                Uri photoURI=   FileProvider.getUriForFile(getApplicationContext(),
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void beginCrop(Uri source) {
        Intent intent= CropImage.activity(source).setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(1,1).getIntent(InboxActivity.this);
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
                        //  beginCrop(selectedImage);

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


        Call<submitChatResponse> call = ApiClient.getUserService().sendImage(token, body, reciever_id);
        call.enqueue(new Callback<submitChatResponse>() {
            @Override
            public void onResponse(Call<submitChatResponse> call, Response<submitChatResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        // getChats();
                        binding.msgText.getText().clear();
                    }
                }

            }

            @Override
            public void onFailure(Call<submitChatResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}