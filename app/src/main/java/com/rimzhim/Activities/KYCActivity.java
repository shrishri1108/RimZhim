package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Adapters.KYCViewPagerAdapter;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityKycactivityBinding;

public class KYCActivity extends AppCompatActivity {
    ActivityKycactivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kycactivity);
        Functions.whiteStatusBar(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        KYCViewPagerAdapter adapter= new KYCViewPagerAdapter(fragmentManager,getLifecycle());
        binding.viewPager.setAdapter(adapter);
        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tablayout.selectTab(binding.tablayout.getTabAt(position));
            }
        });
        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
    1 User Register / Login
    2 Profile creation
    3 Homepage
    4 View Contestors Profile
    5 Like /Voting /  share
    6 Prize distribution
    7 Follow, follwing
    8 Wallet
    9 Leaderboard
    10 Contest	*/


}