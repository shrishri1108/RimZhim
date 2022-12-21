package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Adapters.followerFollowingVpAdapter;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivityFollowerBinding;

public class followerActivity extends AppCompatActivity {
    ActivityFollowerBinding binding;
    String msg, userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_follower);
        Functions.whiteStatusBar(this);

        Intent intent = getIntent();
        msg=intent.getStringExtra("msg");
        userId=intent.getStringExtra("id");
        FragmentManager fragmentManager = getSupportFragmentManager();
        followerFollowingVpAdapter adapter= new followerFollowingVpAdapter(fragmentManager,getLifecycle(), userId);
        binding.viewPager.setAdapter(adapter);

        if(msg.trim().equals("following")){
            binding.viewPager.setCurrentItem(1);
            binding.tablayout.selectTab(binding.tablayout.getTabAt(1));
        }


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
        LoginResponse.User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        binding.userName.setText(user.getName());
    }
}