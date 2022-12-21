package com.rimzhim.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rimzhim.Fragments.MyContests.CompletedFragment;
import com.rimzhim.Fragments.MyContests.Up_ComingFragment;
import com.rimzhim.Fragments.MyContests.liveContestsFragment;


public class contestsViewPagerAdapter extends FragmentStateAdapter {

    public contestsViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new liveContestsFragment();

            case 2:
                return new CompletedFragment();



        }

        return new Up_ComingFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}