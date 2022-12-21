package com.rimzhim.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rimzhim.Fragments.LeaderBordFragment;
import com.rimzhim.Fragments.MyContests.CompletedFragment;
import com.rimzhim.Fragments.MyContests.Up_ComingFragment;
import com.rimzhim.Fragments.MyContests.liveContestsFragment;
import com.rimzhim.Fragments.WinningkFragment;

public class WinnningsVPAdapter extends FragmentStateAdapter {

    private String contestId;

    public WinnningsVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String contestId) {
        super(fragmentManager, lifecycle);
        this.contestId =contestId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                Fragment fragment1 = new LeaderBordFragment();
                Bundle bundle = new Bundle();
                bundle.putString("contestId",contestId);
                fragment1.setArguments(bundle);
                return fragment1;

        }
        Fragment fragment2 = new WinningkFragment();
        Bundle bundle = new Bundle();
        bundle.putString("contestId",contestId);
        fragment2.setArguments(bundle);
        return fragment2;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}