package com.rimzhim.Activities.videorecorder.activity;

import static net.alhazmy13.mediapicker.Video.VideoPicker.VIDEO_PICKER_REQUEST_CODE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import com.coremedia.iso.boxes.Container;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.segmentedprogressbar.ProgressBarListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.filters.BrightnessFilter;
import com.otaliastudios.cameraview.filters.GammaFilter;
import com.otaliastudios.cameraview.filters.SharpnessFilter;
import com.rimzhim.Activities.SoundListActivity;
import com.rimzhim.Activities.VideoRecording.VideoRecoderA;
import com.rimzhim.Activities.videorecorder.SharedConstants;
import com.rimzhim.Activities.videorecorder.common.FilterAdapter;
import com.rimzhim.Activities.videorecorder.common.VideoFilter;
import com.rimzhim.Activities.videorecorder.filters.ExposureFilter;
import com.rimzhim.Activities.videorecorder.filters.HazeFilter;
import com.rimzhim.Activities.videorecorder.filters.MonochromeFilter;
import com.rimzhim.Activities.videorecorder.filters.PixelatedFilter;
import com.rimzhim.Activities.videorecorder.filters.SolarizeFilter;
import com.rimzhim.Activities.videorecorder.utils.BitmapUtil;
import com.rimzhim.Activities.videorecorder.utils.TextFormatUtil;
import com.rimzhim.Activities.videorecorder.utils.VideoUtil;
import com.rimzhim.Activities.videorecorder.workers.MergeAudioVideoWorker;
import com.rimzhim.Activities.videorecorder.workers.MergeVideosWorker;
import com.rimzhim.Activities.videorecorder.workers.VideoSpeedWorker;
import com.rimzhim.Chats.ChatActivity;
import com.rimzhim.R;
import com.example.segmentedprogressbar.SegmentedProgressBar;
import com.rimzhim.SimpleClasses.Constants;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.PermissionUtils;
import com.rimzhim.SimpleClasses.Variables;


import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import info.hoang8f.android.segmented.SegmentedGroup;


public class RecorderActivity extends AppCompatActivity implements ProgressBarListener {

    public static final String EXTRA_AUDIO = "audio";
    private static final String TAG = "RecorderActivity";

    private CameraView mCamera;
    private Handler mHandler = new Handler();
    private MediaPlayer mMediaPlayer;
    private RecorderActivityViewModel mModel;
    private KProgressHUD mProgress;
    private YoYo.YoYoString mPulse;
    private ImageButton mRecordButton;
    private Runnable mStopper = this::stopRecording;
    ImageButton upload;
    PermissionUtils takePermissionUtils;
    List<String> videopaths = new ArrayList<>();



  /*  ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                    Log.d(TAG, "Trimmed path:: " + uri);

                } else
                    LogMessage.v("videoTrimResultLauncher data is null");
            });*/

    ActivityResultLauncher<Intent> resultCallbackForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        // Log.d("=======>>>>>>>>>>>", selectedImage.toString());
                        String first = String.valueOf(Functions.getPathFromUri(getApplicationContext(),selectedImage));
                        Log.v(TAG, "User chose video file: " + first);
                        Intent intent = new Intent(RecorderActivity.this,TrimmerActivity.class);
                        intent.putExtra(TrimmerActivity.EXTRA_VIDEO, first);
                        startActivity(intent);
                        //finish();

                      /*  TrimVideo.activity(String.valueOf(first))
//        .setCompressOption(new CompressOption()) //empty constructor for default compress option
                                .setHideSeekBar(true)
                                .start(RecorderActivity.this,startForResult);*/
                    }
                }
            });
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.v(TAG, "Received request: " + requestCode + ", result: " + resultCode + ".");
        if (requestCode == VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> selection = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            if (selection != null && !selection.isEmpty()) {
                String first = selection.get(0);
                Log.v(TAG, "User chose video file: " + first);
                Intent intent = new Intent(this,TrimmerActivity.class);
                intent.putExtra(TrimmerActivity.EXTRA_VIDEO, first);
                startActivity(intent);
                finish();
            }
        } else if (requestCode == SharedConstants.REQUEST_CODE_PICK_SONG && resultCode == RESULT_OK && data != null) {
        /*    int id = data.getIntExtra(SongPickerActivity.EXTRA_SONG_ID, 0);
            String name = data.getStringExtra(SongPickerActivity.EXTRA_SONG_NAME);
            Uri audio = data.getParcelableExtra(SongPickerActivity.EXTRA_SONG_FILE);
            if (!TextUtils.isEmpty(name) && audio != null) {
                Log.v(TAG, "User chose audio file: " + audio);
                TextView sound = findViewById(R.id.sound);
                sound.setText(name);
                mModel.audio = audio;
                mModel.song = id;
                mMediaPlayer = MediaPlayer.create(this, audio);
                mMediaPlayer.setOnCompletionListener(mp -> mMediaPlayer = null);
            }
        }*/
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        takePermissionUtils=new PermissionUtils(RecorderActivity.this,mPermissionResult);

        mModel = new ViewModelProvider(this).get(RecorderActivityViewModel.class);
        Uri audio = getIntent().getParcelableExtra(EXTRA_AUDIO);
        if (audio != null) {
            Log.v(TAG, "User chose audio file: " + audio);
            TextView sound = findViewById(R.id.sound);
            sound.setText(getString(R.string.audio_from_clip));
            mModel.audio = audio;
            mMediaPlayer = MediaPlayer.create(this, audio);
            mMediaPlayer.setOnCompletionListener(mp -> mMediaPlayer = null);
        }

        mCamera = findViewById(R.id.camera);


        mCamera.setLifecycleOwner(this);
        mCamera.setMode(Mode.VIDEO);

        findViewById(R.id.close).setOnClickListener(view -> confirmClose());
        View sheet = findViewById(R.id.timer_sheet);
        final BottomSheetBehavior<View> bsb = BottomSheetBehavior.from(sheet);
        ImageButton close = sheet.findViewById(R.id.header_back);
        close.setImageResource(R.drawable.ic_baseline_close_24);
        close.setOnClickListener(view -> bsb.setState(BottomSheetBehavior.STATE_COLLAPSED));
        TextView title = sheet.findViewById(R.id.header_title);
        title.setText(R.string.timer_label);
        ImageButton start = sheet.findViewById(R.id.header_more);
        start.setImageResource(R.drawable.ic_baseline_check_24);
        start.setOnClickListener(view -> {
            bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            startTimer();
        });
        findViewById(R.id.done).setOnClickListener(view -> {
            if (mCamera.isTakingVideo()) {
                Toast.makeText(this, R.string.recorder_error_in_progress, Toast.LENGTH_SHORT)
                        .show();
            } else if (mModel.segments.isEmpty()) {
                Toast.makeText(this, R.string.recorder_error_no_clips, Toast.LENGTH_SHORT)
                        .show();
            } else {
                commitRecordings();
            }
        });
        findViewById(R.id.flip).setOnClickListener(view -> {
            if (mCamera.isTakingVideo()) {
                Toast.makeText(this, R.string.recorder_error_in_progress, Toast.LENGTH_SHORT)
                        .show();
            } else {
                mCamera.toggleFacing();
            }
        });
        findViewById(R.id.flash).setOnClickListener(view -> {
            if (mCamera.isTakingVideo()) {
                Toast.makeText(this, R.string.recorder_error_in_progress, Toast.LENGTH_SHORT)
                        .show();
            } else {
                mCamera.setFlash(mCamera.getFlash() == Flash.OFF ? Flash.TORCH : Flash.OFF);
            }
        });
        SegmentedGroup speeds = findViewById(R.id.speeds);
        findViewById(R.id.speed).setOnClickListener(view -> {
            if (mCamera.isTakingVideo()) {
                Toast.makeText(this, R.string.recorder_error_in_progress, Toast.LENGTH_SHORT)
                        .show();
            } else {
                speeds.setVisibility(
                        speeds.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        RadioButton speed05x = findViewById(R.id.speed05x);
        RadioButton speed075x = findViewById(R.id.speed075x);
        RadioButton speed1x = findViewById(R.id.speed1x);
        RadioButton speed15x = findViewById(R.id.speed15x);
        RadioButton speed2x = findViewById(R.id.speed2x);
        speed05x.setChecked(mModel.speed == .5f);
        speed075x.setChecked(mModel.speed == .75f);
        speed1x.setChecked(mModel.speed == 1);
        speed15x.setChecked(mModel.speed == 1.5f);
        speed2x.setChecked(mModel.speed == 2);
        speeds.setOnCheckedChangeListener((group, checked) -> {
            float speed = 1;
            if (checked != R.id.speed05x) {
                speed05x.setChecked(false);
            } else {
                speed = .5f;
            }

            if (checked != R.id.speed075x) {
                speed075x.setChecked(false);
            } else {
                speed = .75f;
            }

            if (checked != R.id.speed1x) {
                speed1x.setChecked(false);
            }

            if (checked != R.id.speed15x) {
                speed15x.setChecked(false);
            } else {
                speed = 1.5f;
            }

            if (checked != R.id.speed2x) {
                speed2x.setChecked(false);
            } else {
                speed = 2;
            }

            mModel.speed = speed;
        });
        findViewById(R.id.timer).setOnClickListener(view -> {
            if (mCamera.isTakingVideo()) {
                Toast.makeText(this, R.string.recorder_error_in_progress, Toast.LENGTH_SHORT)
                        .show();
            } else {
                bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        RecyclerView filters = findViewById(R.id.filters);
        findViewById(R.id.filter).setOnClickListener(view -> {
            if (mCamera.isTakingVideo()) {
                Toast.makeText(this, R.string.recorder_error_in_progress, Toast.LENGTH_SHORT)
                        .show();
            } else if (filters.getVisibility() == View.VISIBLE) {
                filters.setAdapter(null);
                filters.setVisibility(View.GONE);
            } else {
                mProgress = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel(getString(R.string.progress_title))
                        .setCancellable(false)
                        .show();
                mCamera.takePictureSnapshot();
            }
        });
        TextView maximum = findViewById(R.id.maximum);
        mRecordButton = findViewById(R.id.record);
        mRecordButton.setOnClickListener(view -> {
            if (mCamera.isTakingVideo()) {
                stopRecording();
            } else {
                startRecording();
            }
        });
        View sound = findViewById(R.id.sound);
        sound.setOnClickListener(view -> {
            Intent intent = new Intent(RecorderActivity.this, SoundListActivity.class);
            startActivityForResult(intent, SharedConstants.REQUEST_CODE_PICK_SONG);
        });
        Slider selection = findViewById(R.id.selection);
        selection.setLabelFormatter(value -> TextFormatUtil.toMMSS((long)value));

       // View upload = findViewById(R.id.upload);
        upload = (ImageButton)findViewById(R.id.upload);
        upload.setOnClickListener(view -> {
            if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                showVideoPicker();
            }else {
                takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
            }
        });
        bsb.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onSlide(@NonNull View v, float offset) { }

            @Override
            public void onStateChanged(@NonNull View v, int state) {
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    long max;
                    max = SharedConstants.MAX_DURATION - mModel.recorded();
                    max = TimeUnit.MILLISECONDS.toSeconds(max);
                    max = TimeUnit.SECONDS.toMillis(max);
                    selection.setValue(0);
                    selection.setValueTo(max);
                    selection.setValue(max);
                    maximum.setText(TextFormatUtil.toMMSS(max));
                }
            }
        });
        SegmentedProgressBar segments = findViewById(R.id.segments);
        segments.enableAutoProgressView(SharedConstants.MAX_DURATION);
        segments.setDividerColor(Color.BLACK);
        segments.setDividerEnabled(true);
        segments.setDividerWidth(2f);
        segments.setListener(this);
/*        segments.listener(l -> { *//* eaten *//* });*/
        segments.setShader(new int[]{
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorAccent),
        });
        mCamera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                result.toBitmap(bitmap -> {
                    if (bitmap != null) {
                        Bitmap square = BitmapUtil.getSquareThumbnail(bitmap, 250);
                        bitmap.recycle();
                        Bitmap rounded = BitmapUtil.addRoundCorners(square, 25);
                        square.recycle();
                        FilterAdapter adapter =
                                new FilterAdapter(RecorderActivity.this, rounded);
                        adapter.setListener(RecorderActivity.this::applyPreviewFilter);
                        RecyclerView filters = findViewById(R.id.filters);
                        filters.setAdapter(adapter);
                        filters.setVisibility(View.VISIBLE);
                    }
                    mProgress.dismiss();
                });
            }

            @Override
            public void onVideoRecordingEnd() {
                Log.v(TAG, "Video recording has ended.");
                segments.pause();
                segments.addDivider();
                mRecordButton.setSelected(false);
                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                }

                mHandler.postDelayed(() -> processCurrentRecording(), 500);
            }

            @Override
            public void onVideoRecordingStart() {
                Log.v(TAG, "Video recording has started.");
                mRecordButton.setSelected(true);
                if (mMediaPlayer != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        float speed = 1f;
                        if (mModel.speed == .5f) {
                            speed = 2f;
                        } else if (mModel.speed == .75f) {
                            speed = 1.5f;
                        } else if (mModel.speed == 1.5f) {
                            speed = .75f;
                        } else if (mModel.speed == 2f) {
                            speed = .5f;
                        }

                        PlaybackParams params = new PlaybackParams();
                        params.setSpeed(speed);
                        mMediaPlayer.setPlaybackParams(params);
                    }

                    mMediaPlayer.start();
                }

                segments.resume();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }

            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SharedConstants.REQUEST_CODE_READ_STORAGE) {
            boolean ok = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                showVideoPicker();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void applyPreviewFilter(VideoFilter filter) {
        switch (filter) {
            case BRIGHTNESS: {
                BrightnessFilter glf = (BrightnessFilter) Filters.BRIGHTNESS.newInstance();
                glf.setBrightness(1.2f);
                mCamera.setFilter(glf);
                break;
            }
            case EXPOSURE:
                mCamera.setFilter(new ExposureFilter());
                break;
            case GAMMA: {
                GammaFilter glf = (GammaFilter) Filters.GAMMA.newInstance();
                glf.setGamma(2);
                mCamera.setFilter(glf);
                break;
            }
            case GRAYSCALE:
                mCamera.setFilter(Filters.GRAYSCALE.newInstance());
                break;
            case HAZE: {
                HazeFilter glf = new HazeFilter();
                glf.setSlope(-0.5f);
                mCamera.setFilter(glf);
                break;
            }
            case INVERT:
                mCamera.setFilter(Filters.INVERT_COLORS.newInstance());
                break;
            case MONOCHROME:
                mCamera.setFilter(new MonochromeFilter());
                break;
            case PIXELATED: {
                PixelatedFilter glf = new PixelatedFilter();
                glf.setPixel(5);
                mCamera.setFilter(glf);
                break;
            }
            case POSTERIZE:
                mCamera.setFilter(Filters.POSTERIZE.newInstance());
                break;
            case SEPIA:
                mCamera.setFilter(Filters.SEPIA.newInstance());
                break;
            case SHARP: {
                SharpnessFilter glf = (SharpnessFilter) Filters.SHARPNESS.newInstance();
                glf.setSharpness(0.25f);
                mCamera.setFilter(glf);
                break;
            }
            case SOLARIZE:
                mCamera.setFilter(new SolarizeFilter());
                break;
            case VIGNETTE:
                mCamera.setFilter(Filters.VIGNETTE.newInstance());
                break;
            default:
                mCamera.setFilter(Filters.NONE.newInstance());
                break;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void applyVideoSpeed(File file, float speed, long duration) {
        File output = new File(getCacheDir(), UUID.randomUUID().toString());
        mProgress = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.progress_title))
                .setCancellable(false)
                .show();
        Data data = new Data.Builder()
                .putString(VideoSpeedWorker.KEY_INPUT, file.getAbsolutePath())
                .putString(VideoSpeedWorker.KEY_OUTPUT, output.getAbsolutePath())
                .putFloat(VideoSpeedWorker.KEY_SPEED, speed)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(VideoSpeedWorker.class)
                .setInputData(data)
                .build();
        WorkManager wm = WorkManager.getInstance(this);
        wm.enqueue(request);
        wm.getWorkInfoByIdLiveData(request.getId())
                .observe(this, info -> {
                    boolean ended = info.getState() == WorkInfo.State.CANCELLED
                            || info.getState() == WorkInfo.State.FAILED;
                    if (info.getState() == WorkInfo.State.SUCCEEDED) {
                        mProgress.dismiss();
                        RecordSegment segment = new RecordSegment();
                        segment.file = output;
                        segment.duration = duration;
                        mModel.segments.add(segment);
                        file.delete();
                    } else if (ended) {
                        mProgress.dismiss();
                        file.delete();
                    }
                });
    }

    private void closeFinally(File video) {
        Log.v(TAG, "Finalised recorded video at " + video);
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra(FilterActivity.EXTRA_SONG, mModel.song);
        intent.putExtra(FilterActivity.EXTRA_VIDEO, video.getAbsolutePath());
        startActivity(intent);
        finish();
    }



    private void commitRecordings() {
        mProgress = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(getString(R.string.progress_title)).setCancellable(false).show();
        List<String> videos = new ArrayList<>();
        for (RecordSegment segment : mModel.segments) {
            videos.add(segment.file.getAbsolutePath());
        }

        File merged1 = new File(getCacheDir(), UUID.randomUUID().toString());
        Data data1 = new Data.Builder()
                .putStringArray(MergeVideosWorker.KEY_VIDEOS, videos.toArray(new String[0]))
                .putString(MergeVideosWorker.KEY_OUTPUT, merged1.getAbsolutePath())
                .build();
        OneTimeWorkRequest request1 = new OneTimeWorkRequest.Builder(MergeVideosWorker.class)
                .setInputData(data1)
                .build();
        WorkManager wm = WorkManager.getInstance(RecorderActivity.this);
        if (mModel.audio != null) {
            File merged2 = new File(getCacheDir(), UUID.randomUUID().toString());
            Data data2 = new Data.Builder()
                    .putString(MergeAudioVideoWorker.KEY_AUDIO, mModel.audio.getPath())
                    .putString(MergeAudioVideoWorker.KEY_VIDEO, merged1.getAbsolutePath())
                    .putString(MergeAudioVideoWorker.KEY_OUTPUT, merged2.getAbsolutePath())
                    .build();
            OneTimeWorkRequest request2 = new OneTimeWorkRequest.Builder(MergeAudioVideoWorker.class).setInputData(data2).build();
            wm.beginWith(request1).then(request2).enqueue();
            wm.getWorkInfoByIdLiveData(request2.getId())
                    .observe(this, info -> {
                        boolean ended = info.getState() == WorkInfo.State.CANCELLED
                                || info.getState() == WorkInfo.State.FAILED;
                        if (info.getState() == WorkInfo.State.SUCCEEDED) {
                            mProgress.dismiss();
                            closeFinally(merged2);
                        } else if (ended) {
                            mProgress.dismiss();
                        }
                    });
        } else {
            wm.enqueue(request1);
            wm.getWorkInfoByIdLiveData(request1.getId())
                    .observe(this, info -> {
                        boolean ended = info.getState() == WorkInfo.State.CANCELLED
                                || info.getState() == WorkInfo.State.FAILED;
                        if (info.getState() == WorkInfo.State.SUCCEEDED) {
                            mProgress.dismiss();
                            closeFinally(merged1);
                        } else if (ended) {
                            mProgress.dismiss();
                        }
                    });
        }
    }

    private void confirmClose() {
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.confirmation_close_recording)
                .setNegativeButton(R.string.cancel_button, (dialog, i) -> dialog.cancel())
                .setPositiveButton(R.string.close_button, (dialog, i) -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }

    private void processCurrentRecording() {
        long duration = VideoUtil.getDuration(this, Uri.fromFile(mModel.video));
        if (mModel.speed != 1) {
            applyVideoSpeed(mModel.video, mModel.speed, duration);
        } else {
            RecordSegment segment = new RecordSegment();
            segment.file = mModel.video;
            segment.duration = duration;
            mModel.segments.add(segment);
        }

        mModel.video = null;
    }

    private void showVideoPicker() {
       /* new VideoPicker.Builder(RecorderActivity.this)
                .mode(VideoPicker.Mode.GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();*/

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        resultCallbackForGallery.launch(intent);

    }

    private void startRecording() {
        findViewById(R.id.speed).setVisibility(View.GONE);
        findViewById(R.id.timer).setVisibility(View.GONE);
        findViewById(R.id.filter).setVisibility(View.GONE);
        findViewById(R.id.sound).setVisibility(View.GONE);

        if(findViewById(R.id.speeds).getVisibility() == View.VISIBLE){
            findViewById(R.id.speeds).setVisibility(View.GONE);
        }

        if(findViewById(R.id.filters).getVisibility() == View.VISIBLE){
            findViewById(R.id.filters).setVisibility(View.GONE);
        }

        long recorded = mModel.recorded();
        if (recorded >= SharedConstants.MAX_DURATION) {

            Toast.makeText(RecorderActivity.this, R.string.recorder_error_maxed_out, Toast.LENGTH_SHORT).show();

        } else {

            mModel.video = new File(getCacheDir(), UUID.randomUUID().toString());
            mCamera.takeVideoSnapshot(
                    mModel.video, (int)(SharedConstants.MAX_DURATION - recorded));
            mPulse = YoYo.with(Techniques.Pulse)
                    .repeat(YoYo.INFINITE)
                    .playOn(mRecordButton);
        }
    }

    @SuppressLint("SetTextI18n")
    private void startTimer() {
        View countdown = findViewById(R.id.countdown);
        TextView count = findViewById(R.id.count);
        count.setText(null);
        Slider selection = findViewById(R.id.selection);
        long duration = (long)selection.getValue();
        CountDownTimer timer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long remaining) {
                mHandler.post(() -> count.setText(TimeUnit.MILLISECONDS.toSeconds(remaining) + 1 + ""));
            }

            @Override
            public void onFinish() {
                mHandler.post(() -> countdown.setVisibility(View.GONE));
                startRecording();
                mHandler.postDelayed(mStopper, duration);
            }
        };
        countdown.setOnClickListener(v -> {
            timer.cancel();
            countdown.setVisibility(View.GONE);
        });
        countdown.setVisibility(View.VISIBLE);
        timer.start();
    }

    private void stopRecording() {
        /*findViewById(R.id.speed).setVisibility(View.VISIBLE);
        findViewById(R.id.timer).setVisibility(View.VISIBLE);
        findViewById(R.id.filter).setVisibility(View.VISIBLE);
        findViewById(R.id.sound).setVisibility(View.VISIBLE);*/
        mCamera.stopVideo();
        mHandler.removeCallbacks(mStopper);
        mPulse.stop();
    }

    @Override
    public void TimeinMill(long l) {

    }

    public static class RecorderActivityViewModel extends ViewModel {

        public Uri audio;
        public List<RecordSegment> segments = new ArrayList<>();
        public int song = 0;
        public float speed = 1;
        public File video;

        public long recorded() {
            long recorded = 0;
            for (RecordSegment segment : segments) {
                recorded += segment.duration;
            }

            return recorded;
        }
    }

    private static class RecordSegment {
        public File file;
        public long duration;
    }

    private ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    boolean allPermissionClear=true;
                    List<String> blockPermissionCheck=new ArrayList<>();
                    for (String key : result.keySet())
                    {
                        if (!(result.get(key)))
                        {
                            allPermissionClear=false;
                            blockPermissionCheck.add(Functions.getPermissionStatus(RecorderActivity.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked"))
                    {
                        Functions.showPermissionSetting(getApplicationContext(),getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    }
                    else
                    if (allPermissionClear)
                    {
                        showVideoPicker();
                    }

                }
            });







}
