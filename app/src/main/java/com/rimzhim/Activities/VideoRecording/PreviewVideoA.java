package com.rimzhim.Activities.VideoRecording;


import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlFilterGroup;
import com.daasuu.gpuv.player.GPUPlayerView;
import com.daasuu.gpuv.player.PlayerScaleType;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;



import com.google.android.exoplayer2.PlaybackParameters;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;


import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import com.rimzhim.Adapters.FilterAdapter;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.AppCompatLocaleActivity;
import com.rimzhim.SimpleClasses.Constants;
import com.rimzhim.SimpleClasses.FilterType;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.Variables;


import java.io.File;
import java.util.List;

public class PreviewVideoA extends AppCompatLocaleActivity  {
    RelativeLayout editingOptions;
    LinearLayout effectBtn;
    TextView done_btn;
    Button nextBtn;


    String videoUrl, isSoundSelected;
    GPUPlayerView gpuPlayerView;
    public static int selectPostion = 0;
    List<FilterType> filterTypes = FilterType.createFilterList();
    FilterAdapter adapter;
    RecyclerView recylerview;

    String draftFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.setLocale(Functions.getSharedPreference(PreviewVideoA.this).getString(Variables.APP_LANGUAGE_CODE,Variables.DEFAULT_LANGUAGE_CODE)
                , this, PreviewVideoA.class,false);
        setContentView(R.layout.activity_preview_video);
       /* effectBtn =(LinearLayout)findViewById(R.id.effectLayout);
        nextBtn =(Button)findViewById(R.id.nextBtn);
        editingOptions =(RelativeLayout)findViewById(R.id.editingOptions);
        done_btn =(TextView) findViewById(R.id.done_btn);


        Intent intent = getIntent();
        if (intent != null) {
            String fromWhere = intent.getStringExtra("fromWhere");
            if (fromWhere != null && fromWhere.equals("video_recording")) {
                isSoundSelected = intent.getStringExtra("isSoundSelected");
                draftFile = intent.getStringExtra("draft_file");
            } else {
                draftFile = intent.getStringExtra("draft_file");
            }
        }


        selectPostion = 0;
        videoUrl = Functions.getAppFolder(this)+ Variables.outputfile2;
        findViewById(R.id.goBack).setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

        });


        done_btn.setOnClickListener(v -> {
            recylerview.setVisibility(View.GONE);
            done_btn.setVisibility(View.GONE);
            editingOptions.setVisibility(View.VISIBLE);

        if (selectPostion == 0) {
            try {
                    Functions.copyFile(new File(Functions.getAppFolder(this)+Variables.outputfile2), new File(Functions.getAppFolder(this)+Variables.output_filter_file));
                    gotopostScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                    Functions.printLog(Constants.tag, e.toString());
                    saveVideo(Functions.getAppFolder(this)+Variables.outputfile2, Functions.getAppFolder(this)+Variables.output_filter_file);
                }

            } else
                saveVideo(Functions.getAppFolder(this)+Variables.outputfile2, Functions.getAppFolder(this)+Variables.output_filter_file);


        });
        //setPlayer(videoUrl);
        if (isSoundSelected != null && isSoundSelected.equals("yes")) {
            Functions.printLog("resp", "isSoundSelected : " + isSoundSelected);
            preparedAudio();
        }


        recylerview = findViewById(R.id.recylerview);
        adapter = new FilterAdapter(this, filterTypes, (view, postion, item) -> {

            selectPostion = postion;
            gpuPlayerView.setGlFilter(FilterType.createGlFilter(filterTypes.get(postion), getApplicationContext()));
            adapter.notifyDataSetChanged();


        });
        recylerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recylerview.setAdapter(adapter);

        effectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editingOptions.setVisibility(View.GONE);
                recylerview.setVisibility(View.VISIBLE);
                done_btn.setVisibility(View.VISIBLE);


            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectPostion == 0) {

                    try {
                        Functions.copyFile(new File(Functions.getAppFolder(PreviewVideoA.this)+Variables.outputfile2), new File(Functions.getAppFolder(PreviewVideoA.this)+Variables.output_filter_file));
                        gotopostScreen();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Functions.printLog(Constants.tag, e.toString());
                        saveVideo(Functions.getAppFolder(PreviewVideoA.this)+Variables.outputfile2, Functions.getAppFolder(PreviewVideoA.this)+Variables.output_filter_file);
                    }

                } else
                    saveVideo(Functions.getAppFolder(PreviewVideoA.this)+Variables.outputfile2, Functions.getAppFolder(PreviewVideoA.this)+Variables.output_filter_file);

            }
        });*/

    }


   /* MediaPlayer audio;

    public void preparedAudio() {
        videoPlayer.setVolume(0);
        File file = new File(Functions.getAppFolder(this)+Variables.APP_HIDED_FOLDER + Variables.SelectedAudio_AAC);
        if (file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(Functions.getAppFolder(this)+Variables.APP_HIDED_FOLDER + Variables.SelectedAudio_AAC);
                audio.prepare();
                audio.setLooping(true);
                videoPlayer.seekTo(0);
                videoPlayer.setPlayWhenReady(true);
                audio.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // this function will set the player to the current video
    SimpleExoPlayer videoPlayer;

   *//* public void setPlayer(String path) {
        videoPlayer = new SimpleExoPlayer.Builder(PreviewVideoA.this).build();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, getString(R.string.app_name));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(path)));
        videoPlayer.setThrowsWhenUsingWrongThread(false);
        videoPlayer.addMediaSource(mediaSource);
        videoPlayer.prepare();
        videoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        videoPlayer.addListener(this);

        videoPlayer.setPlayWhenReady(true);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(C.USAGE_MEDIA)
                        .setContentType(C.CONTENT_TYPE_MOVIE)
                        .build();
                videoPlayer.setAudioAttributes(audioAttributes, true);
            }
        }
        catch (Exception e)
        {
            Log.d(Constants.tag,"Exception audio focus : "+e);
        }



        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            videoPlayer = ExoPlayerFactory.newSimpleInstance(PreviewVideoA.this, trackSelector);
            Uri videouri = Uri.parse(path);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
            LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
            videoPlayer.addListener((ExoPlayer.EventListener) this);
            videoPlayer.prepare(loopingSource);
            videoPlayer.setPlayWhenReady(true);
            videoPlayer.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });

        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.toString());
        }

        gpuPlayerView = new GPUPlayerView(this);

        Log.d(Constants.tag,"PAth : "+path+"   "+new File(path).exists());

        try {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(path);
            String rotation = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            if (rotation != null && rotation.equalsIgnoreCase("0")) {
                gpuPlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_FIT_WIDTH);
            } else
                gpuPlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_NONE);
        }
        catch (Exception e)
        {
            Log.d(Constants.tag,"Exception : "+e);
        }



        gpuPlayerView.setSimpleExoPlayer(videoPlayer);
        gpuPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).addView(gpuPlayerView);
        gpuPlayerView.onResume();

    }*//*


    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player

    @Override
    protected void onRestart() {
        super.onRestart();

        if (videoPlayer != null) {
            videoPlayer.setPlayWhenReady(true);
        }
        if (audio != null) {
            audio.start();
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (videoPlayer != null) {
            videoPlayer.setPlayWhenReady(true);
            filterTypes = FilterType.createFilterList();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            if (videoPlayer != null) {
                videoPlayer.setPlayWhenReady(false);
            }
            if (audio != null) {
                audio.pause();
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoPlayer != null) {
            videoPlayer.release();
        }

        if (audio != null) {
            audio.pause();
            audio.release();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (videoPlayer != null) {
            videoPlayer.setPlayWhenReady(true);
        }

    }


    // this function will add the filter to video and save that same video for post the video in post video screen

    public void saveVideo(String srcMp4Path, final String destMp4Path) {

        Functions.showDeterminentLoader(this, false, false);
        new GPUMp4Composer(srcMp4Path, destMp4Path)
                .filter(new GlFilterGroup(FilterType.createGlFilter(filterTypes.get(selectPostion), getApplicationContext())))
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Functions.printLog("resp", "" + (int) (progress * 100));
                        Functions.showLoadingProgress((int) (progress * 100));
                    }

                    @Override
                    public void onCompleted() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Functions.cancelDeterminentLoader();
                                gotopostScreen();
                            }
                        });
                    }

                    @Override
                    public void onCanceled() {
                        Functions.printLog("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Functions.printLog("resp", exception.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Functions.cancelDeterminentLoader();

                                    Functions.showToast(PreviewVideoA.this, "Try Again");
                                } catch (Exception e) {

                                }
                            }
                        });

                    }
                })
                .start();


    }


    // go to the post video screen from perview video screen
    public void gotopostScreen() {
*//*Intent intent = new Intent(PreviewVideoA.this, PostVideoActivity.class);
        intent.putExtra("draft_file", draftFile);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);*//*


    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }




    // handler that show the video play state
  *//* @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {

            videoPlayer.seekTo(0);
            videoPlayer.setPlayWhenReady(true);

            if (audio != null) {
                audio.seekTo(0);
                audio.start();
            }
        }

    }*//*
*/


}
