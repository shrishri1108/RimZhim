package com.rimzhim.Activities.videorecorder.workers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.beak.gifmakerlib.GifMaker;
import com.rimzhim.Activities.videorecorder.utils.VideoUtil;
import com.rimzhim.R;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;



public class GeneratePreviewWorker extends Worker {

    public static final String KEY_INPUT = "input";
    public static final String KEY_PREVIEW = "preview";
    public static final String KEY_SCREENSHOT = "screenshot";
    public static final int NOTIFICATION_ID = 60600 + 1;
    public static final String TAG = "GeneratePreviewWorker";

    public GeneratePreviewWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    private ForegroundInfo createForegroundInfo(Context context) {
        String cancel = context.getString(R.string.cancel_button);
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());
        Notification notification =
                new NotificationCompat.Builder(
                        context, context.getString(R.string.notification_channel_id))
                        .setContentTitle(context.getString(R.string.notification_preview_title))
                        .setTicker(context.getString(R.string.notification_preview_title))
                        .setContentText(context.getString(R.string.notification_preview_description))
                        .setSmallIcon(R.drawable.ic_baseline_movie_filter_24)
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .addAction(R.drawable.ic_baseline_close_24, cancel, intent)
                        .build();
        return new ForegroundInfo(NOTIFICATION_ID, notification);
    }

    @NonNull
    @Override
    public Result doWork() {
        setForegroundAsync(createForegroundInfo(getApplicationContext()));
        String input = getInputData().getString(KEY_INPUT);
        String screenshot = getInputData().getString(KEY_SCREENSHOT);
        OutputStream os = null;
        try {
            Bitmap frame = VideoUtil.getFrameAtTime(input, TimeUnit.SECONDS.toMicros(1));
            os = new FileOutputStream(screenshot);
            //noinspection ConstantConditions
            frame.compress(Bitmap.CompressFormat.PNG, 75, os);
            frame.recycle();
        } catch (Exception e) {
            Log.e(TAG, "Unable to extract thumbnail from " + input, e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignore) {
                }
            }
        }

        String preview = getInputData().getString(KEY_PREVIEW);
        GifMaker gif = new GifMaker(2);
        boolean ok = gif.makeGifFromVideo(
                input, 1000, 3000, 250, preview);
        return ok ? Result.success() : Result.failure();
    }
}
