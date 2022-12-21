package com.rimzhim.WatchProfileVideos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import com.google.android.material.imageview.ShapeableImageView;
import com.rimzhim.BuildConfig;
import com.rimzhim.Fragments.VideoFragmentTamp;
import com.rimzhim.Interfaces.FragmentCallBack;
import com.rimzhim.ModelClasses.FollowUserResponseModel;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.likeVideoResponse;
import com.rimzhim.ModelClasses.model.DataItem;
import com.rimzhim.ModelClasses.videoListModel.data;
import com.rimzhim.ModelClasses.voteVideoResponseModel;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Constants;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.dialogs.createVideoDialog;
import com.rimzhim.dialogs.reportDialog;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WatchVideoListF extends Fragment implements View.OnClickListener, Player.EventListener {
    View view;
    Context context;
    LinearLayout sideMenu,videoInfoLayout;
    VerticalViewPager menuPager;
    data item;
    FragmentCallBack fragmentCallBack;
    boolean showad;
    int fragmentContainerId;
    RelativeLayout mainlayout;
    SimpleExoPlayer exoplayer;
    PlayerView playerView;
    // creating a variable for exoplayer

    ProgressBar progressBar;
    ShapeableImageView userImg;
    TextView userName, VideoDescription, likeCount, voteCount, songNameTv;
    Button reportBtn, createVideoBtn,  followBtn;
    ImageView likeBtn,share, voteBtn, songAnimationGif;
    int state = 1, voteState = 1;
    Handler handler;
    Runnable runnable;
    Boolean animationRunning = false;
    int pos;

    public WatchVideoListF() {
        // Required empty public constructor
    }

    public WatchVideoListF(boolean showad, data item, VerticalViewPager menuPager, FragmentCallBack fragmentCallBack, int fragmentContainerId,int pos) {
        this.showad=showad;
        this.item =item;
        this.menuPager=menuPager;
        this.fragmentCallBack = fragmentCallBack;
        this.fragmentContainerId=fragmentContainerId;
        this.pos=pos;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_watch_video_list, container, false);
        context = view.getContext();
        playerView = view.findViewById(R.id.playerview);
       // exoPlayerView = view.findViewById(R.id.idExoPlayerVIew);
        progressBar = view.findViewById(R.id.p_bar);
        userImg =(ShapeableImageView)view.findViewById(R.id.userImg);
        userName =(TextView)view.findViewById(R.id.userName);
        voteCount =(TextView)view.findViewById(R.id.voteCount);
        songNameTv =(TextView)view.findViewById(R.id.songNameTv);
        likeCount =(TextView)view.findViewById(R.id.likeCount);
        VideoDescription=(TextView)view.findViewById(R.id.textVideoDescription);
        reportBtn=(Button) view.findViewById(R.id.reportBtn);
        createVideoBtn=(Button) view.findViewById(R.id.createVideoBtn);
        voteBtn=(ImageView)view.findViewById(R.id.voteBtn);
        followBtn=(Button) view.findViewById(R.id.followBtn);
        likeBtn =(ImageView)view.findViewById(R.id.likeBtn);
        share =(ImageView)view.findViewById(R.id.share);
        mainlayout =(RelativeLayout)view.findViewById(R.id.mainlayout);


        initializePlayer();
       // setUpPlayer();
        setUpData(item);

        userName.setOnClickListener(this);
        userImg.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        share.setOnClickListener(this);
        voteBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);
        createVideoBtn.setOnClickListener(this);
        followBtn.setOnClickListener(this);

        LoginResponse.User user = SharedPrefManager.getInstance(getContext()).getUser();
        if(item.getUser_id().equals(String.valueOf(user.getId()))){
            reportBtn.setVisibility(View.INVISIBLE);
            followBtn.setVisibility(View.INVISIBLE);
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setUpData(item);
            }
        }, 200);

        return view;
    }

    private void initializePlayer() {
        if(exoplayer==null && item!=null){

            ExecutorService executorService= Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    LoadControl loadControl = new DefaultLoadControl.Builder()
                            .setAllocator(new DefaultAllocator(true, 16))
                            .setBufferDurationsMs(1 * 1024, 1 * 1024, 500, 1024)
                            .setTargetBufferBytes(-1)
                            .setPrioritizeTimeOverSizeThresholds(true)
                            .createDefaultLoadControl();

                    DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);
                    try {
                        exoplayer = new SimpleExoPlayer.Builder(context).
                                setTrackSelector(trackSelector)
                                .setLoadControl(loadControl)
                                .build();
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(), context.getString(R.string.app_name));
                        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getVideo()));
                        //  exoplayer.setThrowsWhenUsingWrongThread(false);

                        exoplayer.prepare(videoSource);
                        exoplayer.addListener(WatchVideoListF.this);
                        exoplayer.setRepeatMode(Player.REPEAT_MODE_ALL);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                    .setUsage(C.USAGE_MEDIA)
                                    .setContentType(C.CONTENT_TYPE_MOVIE)
                                    .build();
                            exoplayer.setAudioAttributes(audioAttributes, true);
                        }
                    }
                    catch (Exception e)
                    {
                        //Log.d(Constants.tag,"Exception audio focus : "+e);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playerView = view.findViewById(R.id.playerview);
                            playerView.findViewById(R.id.exo_play).setVisibility(View.GONE);
                            if(exoplayer!=null) {
                                playerView.setPlayer(exoplayer);

                            }
                        }
                    });

                }
            });

        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            progressBar.setVisibility(View.VISIBLE);
        }
        else if (playbackState == Player.STATE_READY) {
            // thumb_image.setVisibility(View.GONE);

            progressBar.setVisibility(View.GONE);
        }


    }



    /* private void setUpPlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            Uri videouri = Uri.parse(item.getVideo());
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
            LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(loopingSource);
          //  exoPlayer.setPlayWhenReady(true);


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
                }
                else if (playbackState == exoPlayer.STATE_READY) {
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
    @SuppressLint("ClickableViewAccessibility")
    private void setUpData(data item) {
        Glide.with(getContext()).load(item.getUser_image()).into(userImg);
        likeCount.setText(Functions.getSuffix(String.valueOf(item.getLike_count())));
        voteCount.setText(Functions.getSuffix(String.valueOf(item.getVote_count())));
        userName.setText(item.getUser_name().trim());
        songNameTv.setText(item.getUser_name()+" sound.....");
        if(item.getIs_vote_available().trim().equals("0")){
            voteBtn.setVisibility(View.GONE);
            voteCount.setVisibility(View.GONE);
        }else {
            voteBtn.setVisibility(View.VISIBLE);
            voteCount.setVisibility(View.VISIBLE);
        }
       // VideoDescription.setText();
      //  songNameTv.setText(item.);
        if(item.getIs_vote().trim().equals("1")){
            voteBtn.setImageResource(R.drawable.ic_vote_filled_icon);
        }else {
            voteBtn.setImageResource(R.drawable.ic_vote_unfilled_icon);
        }

        if(item.getIs_like().trim().equals("1")){
           // Log.d("====>>>>>>", item.getIs_like().trim());
            likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
        }else {
            likeBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        if(item.getIs_following().trim().equals("1")){
            Drawable buttonDrawable = followBtn.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
            followBtn.setBackground(buttonDrawable);
            followBtn.setText("Following");
        }

        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if (!exoplayer.getPlayWhenReady()) {
                        exoplayer.setPlayWhenReady(true);
                    }
                    if (!animationRunning) {
                        if (handler != null && runnable != null) {
                            handler.removeCallbacks(runnable);
                        }
                        handler = new Handler(Looper.getMainLooper());
                        runnable = new Runnable() {
                            public void run() {
                                if (!(String.valueOf(item.getIs_like()).equalsIgnoreCase("1")))
                                {
                                    //LikeVideo();

                                }
                                showHeartOnDoubleTap(item, mainlayout, e);

                            }
                        };
                        handler.postDelayed(runnable, 200);
                    }

                    return super.onDoubleTap(e);
                }
                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {
                    if (exoplayer != null) {
                        exoplayer.setPlayWhenReady(false);
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
        });
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
                    if (String.valueOf(item.getIs_like()).equals("0")){
                        iv.setImageDrawable(getResources().getDrawable(
                                R.drawable.ic_like_fill));
                        likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
                        state =0;
                        int like = Integer.parseInt(item.getLike_count());
                        like++;
                        item.setLike_count(String.valueOf(like));
                        likeCount.setText(Functions.getSuffix(String.valueOf(like)));
                        item.setIs_like("1");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.followBtn:
                if(followBtn.getText().equals("Following")){
                    Drawable buttonDrawable = followBtn.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.parseColor("#ED4D4D"));
                    followBtn.setBackground(buttonDrawable);
                    followBtn.setText("Follow");
                    item.setIs_following("0");
                    FollowUser();
                }else {
                    Drawable buttonDrawable = followBtn.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                    followBtn.setBackground(buttonDrawable);
                    followBtn.setText("Following");
                    item.setIs_following("1");
                    FollowUser();
                }
                break;

            case R.id.reportBtn:
                ReportVideo();
                break;

            case R.id.likeBtn:
                if(item.getIs_like().equals("0")){
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
                    state =0;
                    int like = Integer.parseInt(item.getLike_count());
                    like++;
                    item.setLike_count(String.valueOf(like));
                    likeCount.setText(Functions.getSuffix(String.valueOf(like)));
                    item.setIs_like("1");
                    LikeVideo();

                }else {
                    likeBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    state =1;
                    int like = Integer.parseInt(item.getLike_count());
                    like--;
                    item.setLike_count(String.valueOf(like));
                    likeCount.setText(Functions.getSuffix(String.valueOf(like)));
                    item.setIs_like("0");
                    LikeVideo();

                }
                break;

            case R.id.voteBtn:
                if(item.getIs_vote().equals("0")){
                    voteBtn.setImageResource(R.drawable.ic_vote_filled_icon);
                    voteState =0;
                    int vote = Integer.parseInt(item.getVote_count());
                    vote++;
                    item.setVote_count(String.valueOf(vote));
                    voteCount.setText(Functions.getSuffix(String.valueOf(vote)));
                    item.setIs_vote("1");
                    VoteVideo();

                }else {
                    voteBtn.setImageResource(R.drawable.ic_vote_unfilled_icon);
                    voteState =1;
                    int vote = Integer.parseInt(item.getVote_count());
                    vote--;
                    item.setVote_count(String.valueOf(vote));
                    voteCount.setText(Functions.getSuffix(String.valueOf(vote)));
                    item.setIs_vote("0");
                    VoteVideo();
                }
                break;

            case R.id.share:
                ShareVideo();
                break;

            case R.id.createVideoBtn:
                createVideoDialog.openCreateVideoDialog(getContext());
                break;

        }


    }
    private void VoteVideo() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("video_id",String.valueOf(item.getId()).trim());

        Call<voteVideoResponseModel> call = ApiClient.getUserService().voteVideo(hashMap);
        call.enqueue(new Callback<voteVideoResponseModel>() {
            @Override
            public void onResponse(Call<voteVideoResponseModel> call, Response<voteVideoResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body()!= null){
                        if(response.body().isResult()){
                            if(response.body().getMessage().trim().equals("Voting Successfully")){

                               // Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();


                            }else {

                                Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {

                    Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<voteVideoResponseModel> call, Throwable t) {
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LikeVideo() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("video_id",String.valueOf(item.getId()).trim());
        Call<likeVideoResponse> call = ApiClient.getUserService().likeVideo(hashMap);
        call.enqueue(new Callback<likeVideoResponse>() {
            @Override
            public void onResponse(Call<likeVideoResponse> call, Response<likeVideoResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        if(response.body().getMessage().trim().equals("Liked Successfully")){
                          //  likeBtn.setImageResource(R.drawable.ic_baseline_favorite_24_filled);
                            //Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }else {
                            ///likeBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            //Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }else {

                        Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<likeVideoResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void ReportVideo() {
        reportDialog.openReportDialog(getContext(),String.valueOf(item.getId()).trim());
    }

    private void FollowUser() {
        String token = loginResponsePref.getInstance(getContext()).getToken().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("follower_id",String.valueOf(item.getUser_id()).trim());

        Call<FollowUserResponseModel> call = ApiClient.getUserService().FollowUser(hashMap);
        call.enqueue(new Callback<FollowUserResponseModel>() {
            @Override
            public void onResponse(Call<FollowUserResponseModel> call, Response<FollowUserResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult()){
                        Toast.makeText(getContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        if(response.body().getMessage().trim().equals("Follow Successfully")){
                            Toast.makeText(getContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                         /*   Drawable buttonDrawable = followBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable, Color.TRANSPARENT);
                            followBtn.setBackground(buttonDrawable);
                            followBtn.setText("Following");*/

                        }else {
                            Toast.makeText(getContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                           /* Drawable buttonDrawable = followBtn.getBackground();
                            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                            DrawableCompat.setTint(buttonDrawable,  Color.parseColor("#ED4D4D"));
                            followBtn.setBackground(buttonDrawable);
                            followBtn.setText("Follow");*/
                        }

                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<FollowUserResponseModel> call, Throwable t) {
                Toast.makeText(context,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private void ShareVideo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out the video on RimZhim app at: "+ item.getVideo() + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoplayer != null) {
            exoplayer.setPlayWhenReady(false);
            playerView.findViewById(R.id.exo_play).setAlpha(1);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (exoplayer != null) {
            exoplayer.setPlayWhenReady(false);
            playerView.findViewById(R.id.exo_play).setAlpha(1);
        }
    }

    public void releasePriviousPlayer() {
        if (exoplayer != null) {
            exoplayer.removeListener(this);
            exoplayer.release();
            exoplayer = null;
        }
    }


    @Override
    public void onDestroy() {
        releasePriviousPlayer();
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        exoplayer.setPlayWhenReady(true);

    }

}
