package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.WatchProfileVideos.WatchVideoListF;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleVideoActivity extends AppCompatActivity implements Player.EventListener {
    String videoLink;
    SimpleExoPlayer exoplayer;
    PlayerView playerView;
    SimpleExoPlayer exoPlayer;
    ProgressBar progressBar;
    ImageView backBtn;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.blackStatusBar(SingleVideoActivity.this);
        setContentView(R.layout.activity_single_video);
        Functions.blackStatusBar(this);
        context = SingleVideoActivity.this;
        Intent intent = getIntent();
        videoLink = intent.getStringExtra("videoLink");
        playerView = findViewById(R.id.playerview);
        progressBar = findViewById(R.id.p_bar);
        backBtn = findViewById(R.id.changebackIV);

       // setUpPlayer();
        initializePlayer();


    }

   /* private void setUpPlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
            Uri videouri = Uri.parse(videoLink);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
            LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(loopingSource);
            exoPlayer.setPlayWhenReady(true);


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


    }*/

    private void initializePlayer() {
        if(exoplayer==null && videoLink!=null){

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
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), context.getString(R.string.app_name));
                        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoLink));
                        //  exoplayer.setThrowsWhenUsingWrongThread(false);

                        exoplayer.prepare(videoSource);
                        exoplayer.addListener(SingleVideoActivity.this);
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

                    SingleVideoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playerView = findViewById(R.id.playerview);
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