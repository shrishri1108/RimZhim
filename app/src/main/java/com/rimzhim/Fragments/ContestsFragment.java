package com.rimzhim.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Adapters.contestsViewPagerAdapter;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.RootFragment;
import com.rimzhim.R;


public class ContestsFragment extends RootFragment {
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    SwipeRefreshLayout refreshPage;

    public ContestsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contests, container, false);
        viewPager2 = (ViewPager2)view.findViewById(R.id.videoViewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        refreshPage = (SwipeRefreshLayout) view.findViewById(R.id.refreshPage);
        tabLayout.addTab(tabLayout.newTab().setText("Up coming"));
        tabLayout.addTab(tabLayout.newTab().setText("Live"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));
        setUpTab();

        refreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpTab();
                refreshPage.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setUpTab() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        contestsViewPagerAdapter adapter= new contestsViewPagerAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}