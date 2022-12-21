package com.rimzhim.WatchProfileVideos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.rimzhim.Adapters.ViewPagerStatAdapter;
import com.rimzhim.Fragments.VideoListF;
import com.rimzhim.Interfaces.FragmentCallBack;
import com.rimzhim.ModelClasses.model.DataItem;
import com.rimzhim.ModelClasses.model.UserProfileResponse;
import com.rimzhim.ModelClasses.videoListModel.data;
import com.rimzhim.ModelClasses.videoListModel.videoList;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.RimZhim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class watchUserProfileVideosFragment extends Fragment implements FragmentCallBack {
    String position, userId, videoType;
    int pos;
    List<data> videoList = new ArrayList<>();
    VerticalViewPager menuPager;
    ViewPagerStatAdapter pagerSatetAdapter;
    int page_count = 0;
    boolean isApiRuning = false;
    Handler handler;
    int oldSwipeValue = 0;
    int page=1, videoId;



    public watchUserProfileVideosFragment() {
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
        View view = inflater.inflate(R.layout.fragment_watch_user_profile_videos2, container, false);
        position = getArguments().getString("position");
        userId = getArguments().getString("userId");
        videoType = getArguments().getString("videoType");
      //  page = getArguments().getInt("page",1);
        videoId = getArguments().getInt("videoId",1);
       // Log.d(">>>>>>====", String.valueOf(videoId));
        pos = Integer.parseInt(position);

        pagerSatetAdapter = new ViewPagerStatAdapter(getChildFragmentManager(),menuPager,this::onResponce);
        menuPager =  (VerticalViewPager)view.findViewById(R.id.viewpager);
        menuPager.setAdapter(pagerSatetAdapter);
        menuPager.setOffscreenPageLimit(1);

        loadVideos();

        menuPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0)
                {
                    //swiperefresh.setEnabled(true);
                }
                else
                {
                    //swiperefresh.setEnabled(false);
                }
                if (position == 0 && (pagerSatetAdapter !=null && pagerSatetAdapter.getCount()>0)) {
                    WatchVideoListF fragment = (WatchVideoListF) pagerSatetAdapter.getItem(menuPager.getCurrentItem());
                   // fragment.setUpData();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // fragment.setPlayer(is_visible_to_user);
                        }
                    }, 200);
                }
                //  Log.d(Constants.tag,"Check : check "+(position+1)+"    "+(dataList.size()-1)+"      "+(dataList.size() > 2 && (dataList.size() - 1) == position));
                //   Log.d(Constants.tag,"Test : Test "+(position+1)+"    "+(dataList.size()-5)+"      "+(dataList.size() > 5 && (dataList.size() - 5) == (position+1)));
                if (videoList.size() > 5 && (videoList.size() -5) == (position+1)) {
                    if(!isApiRuning) {
                        page++;
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
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("user_id", userId);
        hashMap.put("video_id", String.valueOf(videoId));
        hashMap.put("page", String.valueOf(page));
        Call<videoList> call = ApiClient.getUserService().GalleryVideoList(hashMap);
        call.enqueue(new Callback<videoList>() {
            @Override
            public void onResponse(Call<videoList> call, Response<videoList> response) {
                if(response.isSuccessful()){

                    if(!response.body().getVideos().getData().isEmpty()){
                        videoList.addAll(response.body().getVideos().getData());
                        setVideoData(videoList);

                    }
                }


            }

            @Override
            public void onFailure(Call<videoList> call, Throwable t) {
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setVideoData(List<data> videoList) {
        for (data item : this.videoList) {
            if (RimZhim.appLevelContext!=null)
            {
                HttpProxyCacheServer proxy = RimZhim.getProxy(RimZhim.appLevelContext);
                String proxyUrl = proxy.getProxyUrl(item.getVideo());
                item.setVideo(proxyUrl);
            }

            pagerSatetAdapter.addFragment(new WatchVideoListF(false, item, menuPager, this::onResponce, R.id.mainMenuFragment,pos), "");
        }
        pagerSatetAdapter.refreshStateSet(false);
        pagerSatetAdapter.notifyDataSetChanged();
        menuPager.setVisibility(View.VISIBLE);


    }

    @Override
    public void onResponce(Bundle bundle) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        page =1;
        videoList.clear();
    }

}