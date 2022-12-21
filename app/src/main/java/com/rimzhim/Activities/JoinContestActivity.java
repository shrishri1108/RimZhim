package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Activities.VideoRecording.VideoRecoderA;
import com.rimzhim.Activities.videorecorder.activity.RecorderActivity;
import com.rimzhim.Adapters.WinnningsVPAdapter;
import com.rimzhim.Adapters.contestsViewPagerAdapter;
import com.rimzhim.Adapters.joinContestRankAdapter;
import com.rimzhim.Fragments.MyContests.Up_ComingFragment;
import com.rimzhim.ModelClasses.ContestResponse.DistributionItem;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityJoinContestBinding;
import com.rimzhim.dialogs.createVideoDialog;

import java.util.ArrayList;
import java.util.List;

public class JoinContestActivity extends AppCompatActivity {
    ActivityJoinContestBinding binding;
   public static List<DistributionItem> list = new ArrayList<>();
    String contestId;
    int totalContestant, leftContestant, entryAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join_contest);
        Functions.whiteStatusBar(JoinContestActivity.this);
        Intent intent = getIntent();
        contestId = intent.getStringExtra("contest_id");

        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("Winning"));
        binding.tabLayout.addTab( binding.tabLayout.newTab().setText("Leader Bord"));
        setUpTab();

        loadData();

        binding.playContestBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(JoinContestActivity.this);

               // createVideoDialog.openCreateVideoDialog(JoinContestActivity.this);
            }
        });

        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.contestsAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(JoinContestActivity.this);
                //createVideoDialog.openCreateVideoDialog(JoinContestActivity.this);
            }
        });


    }



    private void loadData() {
        for(int i = 0; i< Up_ComingFragment.contestsResponseModel.getContest_list().getData().size(); i++){
            if(Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getId() == Integer.valueOf(contestId)){
                binding.winngPriceAmountTv.setText(getString(R.string.rupee)+Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getWinning_amount_in_text());
                binding.contestsAmount.setText(getString(R.string.rupee)+String.valueOf(Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getEntry_amount()));
                entryAmount = Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getEntry_amount();

                if(Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getFilled_spot() != null){
                    totalContestant =Integer.parseInt(Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getSpots());
                    leftContestant =Integer.parseInt(Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getFilled_spot());
                    leftContestant = totalContestant - leftContestant;
                    binding.ProgressBar.setProgress(Functions.showProgress(totalContestant,leftContestant));
                    binding.totalContestantsCount.setText(Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getSpots()+" Total Spots");
                    binding.contestantsCount.setText(String.valueOf(leftContestant)+" Spots left");
                }else {

                }


            }

        }


        for(int i =0; i<Up_ComingFragment.contestsResponseModel.getContest_list().getData().size(); i++ ){
            if(Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getId() == Integer.valueOf(contestId)){
                list = Up_ComingFragment.contestsResponseModel.getContest_list().getData().get(i).getDistribution();

            }

        }





    }

    private void setUpTab() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        WinnningsVPAdapter adapter= new WinnningsVPAdapter(fragmentManager,getLifecycle(),contestId);
        binding.ViewPager.setAdapter(adapter);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.ViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.ViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });

    }

    private void openDialog(Context context) {
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
                // Intent intent = new Intent(context, uploadPreActivity.class);
                // context.startActivity(intent);

                Intent intent = new Intent(context, VideoGalleryActivity.class);
                intent.putExtra("entryAmount", entryAmount);
                intent.putExtra("contestId",Integer.parseInt(contestId));
                context.startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }
}