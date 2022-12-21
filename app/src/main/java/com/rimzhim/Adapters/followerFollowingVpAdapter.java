package com.rimzhim.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rimzhim.Fragments.followerFragment;
import com.rimzhim.Fragments.followingFragment;


public class followerFollowingVpAdapter extends FragmentStateAdapter {
    String act;
    String userId;
    public followerFollowingVpAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String userId) {
        super(fragmentManager, lifecycle);
        this.userId =userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                Fragment fragment1 = new followingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",userId);
                fragment1.setArguments(bundle);
                return fragment1;

        }
        Fragment fragment1 = new followerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",userId);
        fragment1.setArguments(bundle);
        return fragment1;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}