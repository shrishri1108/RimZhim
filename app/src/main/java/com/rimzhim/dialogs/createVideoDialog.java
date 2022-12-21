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
import com.rimzhim.Activities.VideoGalleryActivity;
import com.rimzhim.Activities.VideoRecording.VideoRecoderA;
import com.rimzhim.Activities.videorecorder.activity.RecorderActivity;
import com.rimzhim.MainMenu.MainMenuFragment;
import com.rimzhim.ModelClasses.videoListModel.videoList;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;

import java.util.Collections;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class createVideoDialog {
    public static  void openCreateVideoDialog(Context context){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
        bottomSheetDialog.setContentView(R.layout.create_video_dialog);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       LinearLayout createLL = bottomSheetDialog.findViewById(R.id.createLL);
       LinearLayout uploadLL = bottomSheetDialog.findViewById(R.id.uploadLL);

        createLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(context, RecorderActivity.class);
                context.startActivity(intent);
            }
        });

        uploadLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                TabLayout.Tab contest = MainMenuFragment.tabLayout.getTabAt(1);
                contest.select();
               // Intent intent = new Intent(context, uploadPreActivity.class);
               // context.startActivity(intent);

                /*Intent intent = new Intent(context, VideoGalleryActivity.class);
                context.startActivity(intent);*/
            }
        });
        bottomSheetDialog.show();
    }

}
