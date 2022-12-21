package com.rimzhim.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


import com.rimzhim.BuildConfig;
import com.rimzhim.ModelClasses.UploadKYCResponseModel;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PermissionUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AadhaarFragment extends Fragment {
    EditText aadhaarNum, uploadAadhaar;
    PermissionUtils takePermissionUtils;
    File ImageFile;
    Button saveBtn;
    public AadhaarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aadhaar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        takePermissionUtils=new PermissionUtils(getActivity(),mPermissionResult);
        aadhaarNum =(EditText)view.findViewById(R.id.aadhaarNum);
        uploadAadhaar =(EditText)view.findViewById(R.id.uploadAadhaar);
        saveBtn =(Button) view.findViewById(R.id.saveBtn);
        uploadAadhaar.setFocusable(false);
        uploadAadhaar.setOnClickListener(view1 -> {
            if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                selectImage();
            }
            else
            {
                takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
            }
        });



        saveBtn.setOnClickListener(view12 -> UploadKYC(ImageFile));
    }


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

    ActivityResultLauncher<Intent> resultCallbackForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        beginCrop(selectedImage);

                    }
                }
            });

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
                        beginCrop(selectedImage);
                    }
                }
            });

    ActivityResultLauncher<Intent> resultCallbackForCrop = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                        // handleCrop(cropResult.getUri());
                        //
                        setImageName(cropResult.getUri());
                    }
                }
            });

    private void setImageName(Uri uri) {
        ImageFile = new File(uri.getPath());
        uploadAadhaar.setText(ImageFile.getName());
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

    private void beginCrop(Uri source) {
        Intent intent= CropImage.activity(source).setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(1,1).getIntent(getActivity());
        resultCallbackForCrop.launch(intent);
    }

    private void UploadKYC(File imageFile) {
        final String AadhaarNum = aadhaarNum.getText().toString().trim();
        final String AadhaarImg = uploadAadhaar.getText().toString().trim();
        if(TextUtils.isEmpty(AadhaarNum)){
            aadhaarNum.setError("Enter Aadhaar Number");
            aadhaarNum.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(AadhaarImg)){
            Toast.makeText(getContext(),"Add Aadhaar Image", Toast.LENGTH_SHORT).show();
            return;
        }

        String tokenT =  loginResponsePref.getInstance(getContext()).getToken();
        RequestBody requestFile = RequestBody.create(MediaType.parse("pan_card"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pan_card", imageFile.getName(), requestFile);
        RequestBody token = RequestBody.create(MediaType.parse("token"), tokenT);
        RequestBody pan_no = RequestBody.create(MediaType.parse("pan_no"),AadhaarNum);
        RequestBody type = RequestBody.create(MediaType.parse("type"), "aadhaar");

        Functions.showProgressDialog(getContext());
        Call<UploadKYCResponseModel> call = ApiClient.getUserService(). UploadAadharKYC(token,pan_no,type,body);
        call.enqueue(new Callback<UploadKYCResponseModel>() {
            @Override
            public void onResponse(Call<UploadKYCResponseModel> call, Response<UploadKYCResponseModel> response) {
                if(response.isSuccessful()){
                    Functions.dismissProgressDialog();
                    Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UploadKYCResponseModel> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}