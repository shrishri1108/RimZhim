package com.rimzhim.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.rimzhim.Adapters.ViewPagerStatAdapter;
import com.rimzhim.Interfaces.FragmentCallBack;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.RootFragment;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.videoListModel.data;
import com.rimzhim.ModelClasses.videoListModel.videoList;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Constants;
import com.rimzhim.SimpleClasses.RimZhim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import retrofit2.Call;
import retrofit2.Response;


public class HomeFragment extends RootFragment implements FragmentCallBack {
    public static ArrayList<data> videoList;
    VerticalViewPager menuPager;
    public static ViewPagerStatAdapter pagerSatetAdapter;
    int page_count = 1;
    boolean isApiRuning = false;
    Handler handler;
    int oldSwipeValue = 0;
    TabLayout tabLayout;
    ShimmerFrameLayout shimmer;
    String type ="all";
    SwipeRefreshLayout swiperefresh;
    FrameLayout tabNoFollower;
    videoList Ditem;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Trending"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        shimmer =(ShimmerFrameLayout)view.findViewById(R.id.shimmer);
        tabNoFollower=view.findViewById(R.id.tabNoFollower);

        pagerSatetAdapter = new ViewPagerStatAdapter(getChildFragmentManager(),menuPager,this::onResponce);

        menuPager =  (VerticalViewPager)view.findViewById(R.id.viewpager);
        menuPager.setAdapter(pagerSatetAdapter);
        menuPager.setOffscreenPageLimit(1);
        loadVideos();

        swiperefresh = view.findViewById(R.id.swiperefresh);
        swiperefresh.setProgressViewOffset(false, 0, 200);
        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page_count = 0;
                if(!videoList.isEmpty()){
                    videoList.clear();
                }
                shimmer.setVisibility(View.VISIBLE);
                menuPager.setVisibility(View.GONE);
                shimmer.startShimmer();
                loadVideos();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                         type ="following";
                         videoList.clear();
                         page_count =1;
                         loadVideos();
                         shimmer.setVisibility(View.VISIBLE);
                         menuPager.setVisibility(View.GONE);
                         swiperefresh.setRefreshing(true);
                         break;
                    case 1:
                        type ="trending";
                        videoList.clear();
                        page_count =1;
                        loadVideos();
                        shimmer.setVisibility(View.VISIBLE);
                        menuPager.setVisibility(View.GONE);
                        shimmer.startShimmer();
                        swiperefresh.setRefreshing(true);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        menuPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0)
                {
                    swiperefresh.setEnabled(true);
                }
                else
                {
                    swiperefresh.setEnabled(false);
                }
                if (position == 0 && (pagerSatetAdapter !=null && pagerSatetAdapter.getCount()>0)) {
                 //  VideoListF fragment = (VideoListF) pagerSatetAdapter.getItem(menuPager.getCurrentItem());
                   VideoFragmentTamp fragment = (VideoFragmentTamp) pagerSatetAdapter.getItem(menuPager.getCurrentItem());
                   fragment.setUpData();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           fragment.setPlayer(is_visible_to_user);
                        }
                    }, 200);
                }
                if (videoList.size() > 5 && (videoList.size() -5) == (position+1)) {
                    if(!isApiRuning) {
                        page_count++;
                        loadVideos();
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private void loadVideos() {

        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("token", token);
        hashMap.put("type", type);
        hashMap.put("page", String.valueOf(page_count));
        shimmer.startShimmer();
        Call<videoList> call = ApiClient.getUserService().LoadVideoList(hashMap);
        call.enqueue(new retrofit2.Callback<videoList>() {
            @Override
            public void onResponse(Call<videoList> call, Response<videoList> response) {
                swiperefresh.setRefreshing(false);
                shimmer.stopShimmer();
                menuPager.setVisibility(View.VISIBLE);
                shimmer.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("true")) {
                        Ditem = response.body();

                        for(int i=0; i<response.body().getVideos().getData().size(); i++){
                            if (RimZhim.appLevelContext!=null)
                            {
                                HttpProxyCacheServer proxy = RimZhim.getProxy(RimZhim.appLevelContext);
                                String proxyUrl = proxy.getProxyUrl(Ditem.getVideos().getData().get(i).getVideo());
                                Ditem.getVideos().getData().get(i).setVideo(proxyUrl);
                            }
                        }
                        videoList = Ditem.getVideos().getData();
                        setVideoData(videoList);
                    }
                }
            }

            @Override
            public void onFailure(Call<videoList> call, Throwable t) {
                try {
                    shimmer.stopShimmer();
                    menuPager.setVisibility(View.VISIBLE);
                    shimmer.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });


    }

    private void setVideoData(ArrayList<data> videoList) {
        int index = 0;
        for (data item : videoList) {

           /* if (RimZhim.appLevelContext!=null)
            {
                HttpProxyCacheServer proxy = RimZhim.getProxy(RimZhim.appLevelContext);
                String proxyUrl = proxy.getProxyUrl(item.getVideo());
                item.setVideo(proxyUrl);
            }
*/
            //pagerSatetAdapter.addFragment(new VideoListF(false, item, menuPager, this::onResponce, R.id.mainMenuFragment,index++),"");
            pagerSatetAdapter.addFragment(new VideoFragmentTamp(false, item, menuPager, this::onResponce, R.id.mainMenuFragment,index++),"");

        }
        pagerSatetAdapter.refreshStateSet(false);
        pagerSatetAdapter.notifyDataSetChanged();

        if (!(swiperefresh.isEnabled())) {
            swiperefresh.setEnabled(false);
        }
        tabNoFollower.setVisibility(View.GONE);
        menuPager.setVisibility(View.VISIBLE);

       // Log.d("=========>", String.valueOf( menuPager.getCurrentItem()));

    }

    @Override
    public void onResponce(Bundle bundle) {
        if (bundle != null && bundle.get("action").equals("showad")) {

        } else if (bundle != null && bundle.get("action").equals("hidead")) {

        }
        else if (bundle != null && bundle.get("action").equals("removeList")) {
            pagerSatetAdapter.removeFragment(menuPager.getCurrentItem());
            Log.d(Constants.tag,"Check : size a "+videoList.size());
            videoList.remove(menuPager.getCurrentItem());
            Log.d(Constants.tag,"Check : size b "+videoList.size());
        }
    }

    // this will call when go to the home tab From other tab.
    // this is very importent when for video play and pause when the focus is changes
    boolean is_visible_to_user;
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        is_visible_to_user = visible;

        if (is_visible_to_user && pagerSatetAdapter != null && pagerSatetAdapter.getCount() > 0) {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tabNoFollower.getVisibility()==View.VISIBLE)
                    {
                        onPause();
                    }
                    else
                    {
                       // VideoListF fragment = (VideoListF) pagerSatetAdapter.getItem(menuPager.getCurrentItem());
                       // fragment.mainMenuVisibility(is_visible_to_user);
                    }
                }
            },200);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        if(pagerSatetAdapter !=null && pagerSatetAdapter.getCount()>0) {
           // VideoListF fragment = (VideoListF) pagerSatetAdapter.getItem(menuPager.getCurrentItem());
            VideoFragmentTamp fragment = (VideoFragmentTamp) pagerSatetAdapter.getItem(menuPager.getCurrentItem());
            fragment.mainMenuVisibility(false);
        }

    }
}