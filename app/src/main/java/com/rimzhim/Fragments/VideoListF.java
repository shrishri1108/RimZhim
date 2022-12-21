package com.rimzhim.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;




import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;

import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;


import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.platforminfo.UserAgentPublisher;
import com.rimzhim.Activities.VideoRecording.PreviewVideoA;
import com.rimzhim.BuildConfig;
import com.rimzhim.Interfaces.FragmentCallBack;
import com.rimzhim.MainMenu.MainMenuFragment;
import com.rimzhim.MainMenu.RelateToFragmentOnBack.RootFragment;
import com.rimzhim.ModelClasses.FollowUserResponseModel;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.likeVideoResponse;
import com.rimzhim.ModelClasses.videoListModel.data;
import com.rimzhim.ModelClasses.voteVideoResponseModel;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Constants;
import com.rimzhim.SimpleClasses.DoubleClickListener;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.RimZhim;
import com.rimzhim.dialogs.createVideoDialog;
import com.rimzhim.dialogs.reportDialog;

import net.ypresto.qtfaststart.QtFastStart;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListF extends RootFragment implements View.OnClickListener {
    View view;
    Context context;
    LinearLayout sideMenu, videoInfoLayout;
    VerticalViewPager menuPager;
    data Ditem;
    FragmentCallBack fragmentCallBack;
    boolean showad;
    int fragmentContainerId;
   // SimpleExoPlayerView exoPlayerView;
    int state = 1, voteState = 1;

    RelativeLayout mainlayout;
    GestureDetector gestureDetector;

    // creating a variable for exoplayer
    SimpleExoPlayer exoPlayer;
    ProgressBar progressBar;
    ShapeableImageView userImg;
    TextView userName, VideoDescription, likeCount, voteCount, songNameTv;
    Button reportBtn, createVideoBtn,  followBtn;

    ImageView likeBtn, share, voteBtn, songAnimationGif;
   // PlayerView playerview;

    Handler handler;
    Runnable runnable;
    Boolean animationRunning = false;
    int index;


    ///new

    PlayerView playerView;


    public VideoListF() {
        // Required empty public constructor
    }

    public VideoListF(boolean showad, data Ditem, VerticalViewPager menuPager, FragmentCallBack fragmentCallBack, int fragmentContainerId, int index) {
        this.showad = showad;
        this.Ditem = Ditem;
        this.menuPager = menuPager;
        this.fragmentCallBack = fragmentCallBack;
        this.fragmentContainerId = fragmentContainerId;
        this.index = index;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_list, container, false);
     //   exoPlayerView = view.findViewById(R.id.idExoPlayerVIew);

        progressBar = view.findViewById(R.id.p_bar);
        userImg = (ShapeableImageView) view.findViewById(R.id.userImg);
        userName = (TextView) view.findViewById(R.id.userName);
        voteCount = (TextView) view.findViewById(R.id.voteCount);
        songNameTv = (TextView) view.findViewById(R.id.songNameTv);
        likeCount = (TextView) view.findViewById(R.id.likeCount);
        VideoDescription = (TextView) view.findViewById(R.id.textVideoDescription);
        reportBtn = (Button) view.findViewById(R.id.reportBtn);
        createVideoBtn = (Button) view.findViewById(R.id.createVideoBtn);
        voteBtn = (ImageView) view.findViewById(R.id.voteBtn);
        followBtn = (Button) view.findViewById(R.id.followBtn);
        likeBtn = (ImageView) view.findViewById(R.id.likeBtn);
        share = (ImageView) view.findViewById(R.id.share);
        mainlayout =(RelativeLayout)view.findViewById(R.id.mainlayout);







        //setPlayer();
        //setOplayer();
        userName.setOnClickListener(this);
        userImg.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        share.setOnClickListener(this);
        voteBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);
        createVideoBtn.setOnClickListener(this);
        followBtn.setOnClickListener(this);


        LoginResponse.User user = SharedPrefManager.getInstance(getContext()).getUser();
        if(Ditem.getUser_id().equals(String.valueOf(user.getId()))){
            reportBtn.setVisibility(View.INVISIBLE);
            followBtn.setVisibility(View.INVISIBLE);
        }



        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpData();
            }
        }, 200);

        return view;
    }



    @SuppressLint("ClickableViewAccessibility")
    public void setUpData() {
        if (view == null && Ditem != null)
            return;

        else {
            Glide.with(getContext()).load(Ditem.getUser_image()).placeholder(R.drawable.ic_user_icon).into(userImg);
            likeCount.setText(Functions.getSuffix(Ditem.getLike_count()));
            voteCount.setText(Functions.getSuffix(Ditem.getVote_count()));
            userName.setText(Ditem.getUser_name().trim());
            songNameTv.setText(Ditem.getUser_name()+" sound.....");
            if(Ditem.getIs_vote_available().trim().equals("0")){
                voteBtn.setVisibility(View.GONE);
                voteCount.setVisibility(View.GONE);
            }else {
                voteBtn.setVisibility(View.VISIBLE);
                voteCount.setVisibility(View.VISIBLE);
            }

            if(Ditem.getIs_vote().trim().equals("1")){
                voteBtn.setImageResource(R.drawable.ic_vote_filled_icon);
            }

            if (Ditem.getIs_like().trim().equals("1")) {
                likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
            }
            if (Ditem.getIs_following().trim().equals("1")) {
                Drawable buttonDrawable = followBtn.getBackground();
                buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                followBtn.setBackground(buttonDrawable);
                followBtn.setText("Following");
            }
        }

        /*exoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if (!exoPlayer.getPlayWhenReady()) {
                        exoPlayer.setPlayWhenReady(true);
                    }
                    if (!animationRunning) {
                        if (handler != null && runnable != null) {
                            handler.removeCallbacks(runnable);
                        }
                        handler = new Handler(Looper.getMainLooper());
                        runnable = new Runnable() {
                            public void run() {
                                if (!(Ditem.getIs_like().equalsIgnoreCase("1")))
                                {
                                    //LikeVideo();

                                }
                                showHeartOnDoubleTap(Ditem, mainlayout, e);

                            }
                        };
                        handler.postDelayed(runnable, 200);
                    }

                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    if (exoPlayer != null) {
                        exoPlayer.setPlayWhenReady(false);
                    }
                    Log.d("onSingleTapConfirmed", "onSingleTap");
                    return false;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });*/

    }
    private boolean showHeartOnDoubleTap(data item, RelativeLayout mainlayout, MotionEvent e) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int x = (int) e.getX() - 100;
                    int y = (int) e.getY() - 100;
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    final ImageView iv = new ImageView(getContext());
                    lp.setMargins(x, y, 0, 0);
                    iv.setLayoutParams(lp);
                   if (item.getIs_like().equals("0")){
                        iv.setImageDrawable(getResources().getDrawable(
                                R.drawable.ic_like_fill));
                       likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
                       state =0;
                       int like = Integer.parseInt(Ditem.getLike_count());
                       like++;
                       Ditem.setLike_count(String.valueOf(like));
                       likeCount.setText(Functions.getSuffix(String.valueOf(like)));
                       Ditem.setIs_like("1");
                       LikeVideo();
                    }
                    mainlayout.addView(iv);
                    iv.animate().alpha(0).translationY(-200).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (iv!=null)
                                mainlayout.removeView(iv);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            if (iv!=null)
                                mainlayout.removeView(iv);
                        }
                    }).start();
                }
            });
        }
        catch (Exception excep)
        {
            Log.d(Constants.tag,"Exception : "+excep);
        }
        return true;
    }

/*

    private void setPlayer() {
        try {
            LoadControl loadControl = new DefaultLoadControl();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            //exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            Uri videouri = Uri.parse(Ditem.getVideo());
            String userAgent = "exoplayer_video";
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, (TransferListener) bandwidthMeter, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(), (TransferListener) bandwidthMeter, httpDataSourceFactory);
            // DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

            LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(loopingSource);
           // exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.toString());
        }

        exoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == exoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == exoPlayer.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    songNameTv.setSelected(true);
                   // Glide.with(getContext()).asGif().load(R.raw.song_line_anime).into(songAnimationGif);
                }
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }


            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
    }
*/

    private Uri videoCommpresser(Uri uri) {
        File input = new File(uri.getPath()); // Your input file
        File output = new File(uri.getPath()); // Your output file
        try {
            if (!output.exists()) // if there is no output file we'll create one
                output.createNewFile();

    }catch(
    IOException e)

    {
        Log.e("TAG", e.toString());
    }
    try

    {
        QtFastStart.fastStart(input, output);
    }catch(
    QtFastStart.MalformedFileException m)

    {
        Log.e("QT", m.toString());
    }
    catch(QtFastStart.UnsupportedFileException q)

    {
        Log.e("QT", q.toString());
    }
    catch(IOException i)

    {
        Log.e("QT", i.toString());
    }

    Uri uri1 = Uri.parse(output.getPath());
    return uri1;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }


    public void releasePriviousPlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePriviousPlayer();

    }


    @Override
    public void onClick(View view) {
        LoginResponse.User user = SharedPrefManager.getInstance(getContext()).getUser();
        switch (view.getId()) {
            case R.id.followBtn:
                if(followBtn.getText().equals("Following")){
                    Drawable buttonDrawable = followBtn.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
                    followBtn.setBackground(buttonDrawable);
                    followBtn.setText("Follow");
                    Ditem.setIs_following("0");
                    FollowUser();

                }else {
                    Drawable buttonDrawable = followBtn.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                    followBtn.setBackground(buttonDrawable);
                    followBtn.setText("Following");
                    Ditem.setIs_following("1");
                    FollowUser();
                }
                break;

            case R.id.reportBtn:
                ReportVideo();
                break;

            case R.id.likeBtn:
                if(Ditem.getIs_like().equals("0")){
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
                    state =0;
                    int like = Integer.parseInt(Ditem.getLike_count());
                        like++;
                        Ditem.setLike_count(String.valueOf(like));
                    Log.d("=======in", String.valueOf(index));
                        likeCount.setText(Functions.getSuffix(String.valueOf(like)));
                        Ditem.setIs_like("1");
                        LikeVideo();
                }else {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    state =1;
                    int like = Integer.parseInt(Ditem.getLike_count());
                    like--;
                    Ditem.setLike_count(String.valueOf(like));
                    likeCount.setText(Functions.getSuffix(String.valueOf(like)));
                    Ditem.setIs_like("0");
                    LikeVideo();
                }

                break;
            case R.id.voteBtn:
                if(Ditem.getIs_vote().equals("0")){
                    voteBtn.setImageResource(R.drawable.ic_vote_filled_icon);
                    voteState =0;
                    int vote = Integer.parseInt(Ditem.getVote_count());
                    vote++;
                    Ditem.setVote_count(String.valueOf(vote));
                    voteCount.setText(Functions.getSuffix(String.valueOf(vote)));
                    Ditem.setIs_vote("1");
                    VoteVideo();

                }else {
                    voteBtn.setImageResource(R.drawable.ic_vote_unfilled_icon);
                    voteState =1;
                    int vote = Integer.parseInt(Ditem.getVote_count());
                    vote--;
                    Ditem.setVote_count(String.valueOf(vote));
                    voteCount.setText(Functions.getSuffix(String.valueOf(vote)));
                    Ditem.setIs_vote("0");
                    VoteVideo();
                }
                break;

            case R.id.share:
                ShareVideo();
                break;

            case R.id.createVideoBtn:
                createVideoDialog.openCreateVideoDialog(getContext());
                break;


            case R.id.userName:
                if(Ditem.getUser_id().trim().equals(String.valueOf(user.getId()))){
                    TabLayout.Tab profile = MainMenuFragment.tabLayout.getTabAt(2);
                    profile.select();
                }else {
                    Functions.OpenOtherProfile(getContext(), Ditem.getUser_id());
                }
                if (exoPlayer != null) {
                    exoPlayer.setPlayWhenReady(false);
                }


                break;


            case R.id.userImg:
                if(Ditem.getUser_id().trim().equals(String.valueOf(user.getId()))){
                    TabLayout.Tab profile = MainMenuFragment.tabLayout.getTabAt(2);
                    profile.select();
                }else {
                    Functions.OpenOtherProfile(getContext(), Ditem.getUser_id());
                }
                if (exoPlayer != null) {
                    exoPlayer.setPlayWhenReady(false);
                }
                break;

        }

    }


    private void VoteVideo() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("video_id", String.valueOf(Ditem.getId()).trim());
        Call<voteVideoResponseModel> call = ApiClient.getUserService().voteVideo(hashMap);
        call.enqueue(new Callback<voteVideoResponseModel>() {
            @Override
            public void onResponse(Call<voteVideoResponseModel> call, Response<voteVideoResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isResult()) {
                            if (response.body().getMessage().trim().equals("Voted Successfully")) {


                                //voteBtn.setImageResource(R.drawable.ic_vote_filled_icon);
//
                               // Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();


                            } else {
                               // Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                               // voteBtn.setImageResource(R.drawable.ic_vote_unfilled_icon);


                         }

                        } else {

                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<voteVideoResponseModel> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LikeVideo() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("video_id", String.valueOf(Ditem.getId()).trim());
        Call<likeVideoResponse> call = ApiClient.getUserService().likeVideo(hashMap);
        call.enqueue(new Callback<likeVideoResponse>() {
            @Override
            public void onResponse(Call<likeVideoResponse> call, Response<likeVideoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isResult()) {
                      //  Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                       /* if (response.body().getMessage().trim().equals("Liked Successfully")) {
                            likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {
                            likeBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }*/


                    } else {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<likeVideoResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void ReportVideo() {
        reportDialog.openReportDialog(getContext(), String.valueOf(Ditem.getId()).trim());
    }
    private void FollowUser() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("follower_id", String.valueOf(Ditem.getUser_id()).trim());

        Call<FollowUserResponseModel> call = ApiClient.getUserService().FollowUser(hashMap);
        call.enqueue(new Callback<FollowUserResponseModel>() {
            @Override
            public void onResponse(Call<FollowUserResponseModel> call, Response<FollowUserResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().isResult()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<FollowUserResponseModel> call, Throwable t) {
                Toast.makeText(context, "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void ShareVideo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out the video on RimZhim app at: " + Ditem.getVideo() + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void mainMenuVisibility(boolean isvisible) {

        if (exoPlayer != null && isvisible) {
            exoPlayer.setPlayWhenReady(true);
        }

        else if (exoPlayer != null && !isvisible) {
            exoPlayer.setPlayWhenReady(false);
            //exoPlayerView.findViewById(R.id.exo_play).setAlpha(1);
        }


    }


}




