package com.rimzhim.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.rimzhim.BuildConfig;
import com.rimzhim.MainMenu.MainMenuFragment;

import com.rimzhim.ModelClasses.SimpleResponse;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class videoMoreMenuDialog {
    public static  void videoMoreMenuDialog(Context context, String videoLink, String videoId){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
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
                        "Hey check out the video on RimZhim app at: " + videoLink + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);


            }
        });
        deleteLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteVideo(context, videoId);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    private static void DeleteVideo(Context context, String videoId) {
        String tokenT =  loginResponsePref.getInstance(context).getToken();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token",tokenT);
        hashMap.put("video_id",videoId);
        Functions.showProgressDialog(context);
        Call<SimpleResponse> call = ApiClient.getUserService().DeleteVideo(hashMap);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Functions.dismissProgressDialog();
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        Functions.makeToast(context,"Video Deleted");
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Functions.dismissProgressDialog();
                Toast.makeText(context,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
