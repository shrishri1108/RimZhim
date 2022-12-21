package com.rimzhim.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rimzhim.Fragments.AadhaarFragment;
import com.rimzhim.Fragments.PanFragment;


public class KYCViewPagerAdapter extends FragmentStateAdapter {
    public KYCViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new AadhaarFragment();

        }

        return new PanFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
