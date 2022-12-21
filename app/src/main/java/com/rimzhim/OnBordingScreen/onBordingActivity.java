package com.rimzhim.OnBordingScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.rimzhim.Activities.Login_Activity;
import com.rimzhim.R;
import com.rimzhim.SharedPres.onBoardingPref;
import com.rimzhim.databinding.ActivityOnBordingBinding;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class onBordingActivity extends AppCompatActivity {
    ActivityOnBordingBinding binding;
    ArrayList<onBordingScreenModel> list = new ArrayList<>();
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_on_bording);

        if(onBoardingPref.getInstance(getApplicationContext()).getAct()){
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();

        }

        onBordingScreenModel onBordingScreenModel = new onBordingScreenModel();
        onBordingScreenModel.setName(R.raw.video_call);
        onBordingScreenModel.setDescription("Show your Hidden Skills");
        onBordingScreenModel.setColor( Color.parseColor("#333C83"));
        list.add(onBordingScreenModel);

        onBordingScreenModel onBordingScreenModel1 = new onBordingScreenModel();
        onBordingScreenModel1.setName(R.raw.following);
        onBordingScreenModel1.setDescription("Add favorite Music to Your Video ");
        onBordingScreenModel1.setColor( Color.parseColor("#F7A98F"));
        list.add(onBordingScreenModel1);

        onBordingScreenModel onBordingScreenModel2 = new onBordingScreenModel();
        onBordingScreenModel2.setName(R.raw.disco_ball);
        onBordingScreenModel2.setDescription("Add favorite Music to Your Video ");
        onBordingScreenModel2.setColor( Color.parseColor("#FF5B58"));
        list.add(onBordingScreenModel2);





        OnBoardingViewPagerAdapter adapter = new OnBoardingViewPagerAdapter(getApplicationContext(),list);
        binding.slider.setAdapter(adapter);


        binding.dotsIndicator.addDot(list.size());
        if(binding.slider.getCurrentItem()==2){
            binding.skipBtn.setVisibility(View.VISIBLE);
        }
        binding.dotsIndicator.setViewPager2(binding.slider);
        binding.skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(intent);
                finish();

            }
        });

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == list.size()+1) {
                    currentPage = 0;
                }
                //  addSlider.setCurrentItem(currentPage++, true);
                binding.slider.setCurrentItem(currentPage++, true);

                /*f(3==binding.slider.getCurrentItem()){
                    binding.skipBtn.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                    startActivity(intent);
                    finish();
                    timer.cancel();

                }*/

            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);

    }
}