package com.rimzhim.Activities.videorecorder.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rimzhim.Activities.videorecorder.SharedConstants;
import com.rimzhim.Activities.videorecorder.trimmer.TrimTimeBar;
import com.rimzhim.Activities.videorecorder.utils.TextFormatUtil;
import com.rimzhim.Activities.videorecorder.utils.VideoUtil;
import com.rimzhim.Activities.videorecorder.workers.VideoTrimmerWorker;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;



public class TrimmerActivity extends AppCompatActivity implements AnalyticsListener, TrimTimeBar.Listener {

    public static final String EXTRA_VIDEO = "video";
    public static final String TAG = "VideoTrimmerActivity";

    private int mDuration = 0;
    private Handler mHandler = new Handler();
    private SimpleExoPlayer mPlayer;
    private Runnable mProgress = new Runnable() {

        @Override
        public void run() {
            if (mPlayer != null && mPlayer.isPlaying()) {
                long position = mPlayer.getCurrentPosition();
                mTimeBar.setTime(
                        mTrimStartTime + (int) position,
                        mDuration,
                        mTrimStartTime,
                        mTrimEndTime
                );
            }
            mHandler.postDelayed(mProgress, 100);
        }
    };
    private TrimTimeBar mTimeBar;
    private int mTrimEndTime = 0;
    private int mTrimStartTime = 0;
    private String mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);
        Functions.whiteStatusBar(TrimmerActivity.this);
        Intent intent = getIntent();
        ImageButton close = findViewById(R.id.header_back);
        close.setImageResource(R.drawable.ic_baseline_close_24);
        close.setOnClickListener(view -> finish());
        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.trimmer_label);
        ImageButton done = findViewById(R.id.header_more);
        done.setImageResource(R.drawable.ic_baseline_check_24);
        done.setOnClickListener(view -> commitSelection());
        mVideo = intent.getStringExtra(EXTRA_VIDEO);
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        mPlayer.addAnalyticsListener(this);
        mPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        PlayerView player = findViewById(R.id.player);
        player.setPlayer(mPlayer);
        mDuration = (int) VideoUtil.getDuration(this, Uri.parse(mVideo));
        mTrimStartTime = 0;
        mTrimEndTime = Math.min(mDuration, (int) SharedConstants.MAX_DURATION);
        Log.v(TAG, "Duration of video is " + mDuration + "ms.");
        mTimeBar = new TrimTimeBar(this, this);
        mTimeBar.setTime(0, mDuration, 0, mTrimEndTime);
        LinearLayout wrapper = findViewById(R.id.trimmer);
        wrapper.addView(mTimeBar);
        startPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mProgress);
        mPlayer.setPlayWhenReady(false);
        if (mPlayer.isPlaying()) {
            mPlayer.stop(true);
        }
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onScrubbingStart() {
        mPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onScrubbingMove(int time) {
    }

    @Override
    public void onScrubbingEnd(int time, int start, int end) {
        Log.v(TAG, "Scrub position is " + start + "ms to " + end + "ms.");
        mTrimStartTime = start;
        mTrimEndTime = end;
        startPlayer();
    }

    private void closeFinally(File file) {
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra(FilterActivity.EXTRA_VIDEO, file.getAbsolutePath());
        startActivity(intent);
        finish();
    }

    private void commitSelection() {
        int duration = mTrimEndTime - mTrimStartTime;
        if (duration > SharedConstants.MAX_DURATION) {
            String message = getString(
                    R.string.message_trim_too_long,
                    TimeUnit.MILLISECONDS.toSeconds(SharedConstants.MAX_DURATION)
            );
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (duration < SharedConstants.MIN_DURATION) {
            String message = getString(
                    R.string.message_trim_too_short,
                    TimeUnit.MILLISECONDS.toSeconds(SharedConstants.MIN_DURATION)
            );
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            submitForTrim();
        }
    }

    private void startPlayer() {
        DefaultDataSourceFactory factory = new DefaultDataSourceFactory(this, getString(R.string.app_name));
        MediaSource source = new ProgressiveMediaSource.Factory(factory).createMediaSource(Uri.fromFile(new File(mVideo)));
        source = new ClippingMediaSource(source, TimeUnit.MILLISECONDS.toMicros(mTrimStartTime), TimeUnit.MILLISECONDS.toMicros(mTrimEndTime)
        );
        mPlayer.setPlayWhenReady(true);
        mPlayer.prepare(source);
        mHandler.removeCallbacks(mProgress);
        mHandler.postDelayed(mProgress, 100);
        TextView selection = findViewById(R.id.selection);
        selection.setText(TextFormatUtil.toMMSS(mTrimEndTime - mTrimStartTime));
    }

    private void submitForTrim() {
        mPlayer.setPlayWhenReady(false);
        KProgressHUD progress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.progress_title))
                .setCancellable(false)
                .show();
        WorkManager wm = WorkManager.getInstance(this);
        File trimmed = new File(getCacheDir(), UUID.randomUUID().toString());
        Data data = new Data.Builder()
                .putString(VideoTrimmerWorker.KEY_INPUT, mVideo)
                .putString(VideoTrimmerWorker.KEY_OUTPUT, trimmed.getAbsolutePath())
                .putLong(VideoTrimmerWorker.KEY_START, mTrimStartTime)
                .putLong(VideoTrimmerWorker.KEY_END, mTrimEndTime)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(VideoTrimmerWorker.class)
                .setInputData(data)
                .build();
        wm.enqueue(request);
        wm.getWorkInfoByIdLiveData(request.getId())
                .observe(this, info -> {
                    boolean ended = info.getState() == WorkInfo.State.CANCELLED
                            || info.getState() == WorkInfo.State.FAILED;
                    if (info.getState() == WorkInfo.State.SUCCEEDED) {
                        progress.dismiss();
                        closeFinally(trimmed);
                    } else if (ended) {
                        progress.dismiss();
                    }
                });
    }
}
