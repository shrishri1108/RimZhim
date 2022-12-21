package com.rimzhim.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rimzhim.ModelClasses.videoReportResponseModel;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class reportDialog {

    public static  void openReportDialog(Context context, String videoId){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
        bottomSheetDialog.setContentView(R.layout.report_dialog);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayoutCompat spamLt = bottomSheetDialog.findViewById(R.id.spamLt);
        LinearLayoutCompat dontLikeIt = bottomSheetDialog.findViewById(R.id.dontLikeIt);
        LinearLayoutCompat hateSpeech = bottomSheetDialog.findViewById(R.id.hateSpeech);
        LinearLayoutCompat violence = bottomSheetDialog.findViewById(R.id.violence);

        spamLt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                String comment = "It’s Spam";
                reportVideo(context,comment, videoId);

            }
        });
        dontLikeIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                String comment = "I just don’t like it";
                reportVideo(context,comment, videoId);

            }
        });
        hateSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                String comment = "Hate speech or symbols";
                reportVideo(context,comment, videoId);

            }
        });
        violence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                String comment = "Violence or dangerous organizations";
                reportVideo(context,comment, videoId);

            }
        });



        bottomSheetDialog.show();



    }

    private static void reportVideo(Context context,String comment, String videoId) {
        String token =  loginResponsePref.getInstance(context).getToken();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("video_id", videoId);
        hashMap.put("comment", comment);
        Functions.showProgressDialog(context);
        Call<videoReportResponseModel> call = ApiClient.getUserService().reportVideo(hashMap);
        call.enqueue(new Callback<videoReportResponseModel>() {
            @Override
            public void onResponse(Call<videoReportResponseModel> call, Response<videoReportResponseModel> response) {
                if(response.isSuccessful()){
                    Functions.dismissProgressDialog();

                    if(response.body().isResult()){
                        Functions.dismissProgressDialog();
                        Toast.makeText(context, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        reportSuccessDialog.openReportSuccessDialog(context, "your report has been successfully submitted");
                    }else {
                        Toast.makeText(context, response.body().getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Functions.dismissProgressDialog();
                    Toast.makeText(context,"Failed", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<videoReportResponseModel> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(context,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }


}
