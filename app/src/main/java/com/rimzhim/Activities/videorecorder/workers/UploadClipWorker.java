package com.rimzhim.Activities.videorecorder.workers;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.rimzhim.Activities.videorecorder.models.Clip;
import com.rimzhim.Activities.videorecorder.models.Wrappers;
import com.rimzhim.Activities.videorecorder.utils.VideoUtil;
import com.rimzhim.ModelClasses.uploadVideoResponseModel;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.loginResponsePref;

import java.io.File;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class UploadClipWorker extends Worker {

   // public static final String KEY_COMMENTS = "comments";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TAGS = "tags";
    //public static final String KEY_PREVIEW = "preview";
    //public static final String KEY_PRIVATE = "private";
   // public static final String KEY_SCREENSHOT = "screenshot";
   // public static final String KEY_SONG = "song";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_TAG = "tags";
    public static final String KEY_TOKEN = "video";
    public static final String KEY_LANGUAGE = "language";
    public static final int NOTIFICATION_ID = 60600 + 2;
    public static final String TAG = "PostClipWorker";

    public UploadClipWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    private ForegroundInfo createForegroundInfo(Context context) {
        String cancel = context.getString(R.string.cancel_button);
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        Notification notification =
                new NotificationCompat.Builder(
                        context, context.getString(R.string.notification_channel_id))
                        .setContentTitle(context.getString(R.string.notification_upload_title))
                        .setTicker(context.getString(R.string.notification_upload_title))
                        .setContentText(context.getString(R.string.notification_upload_description))
                        .setSmallIcon(R.drawable.ic_baseline_publish_24)
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .addAction(R.drawable.ic_baseline_close_24, cancel, intent)
                        .build();
        return new ForegroundInfo(NOTIFICATION_ID, notification);
    }

    @NonNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Result doWork() {
        setForegroundAsync(createForegroundInfo(getApplicationContext()));
        String video = getInputData().getString(KEY_VIDEO);
        Log.v(TAG, "Uploading " + video);
      //  String screenshot = getInputData().getString(KEY_SCREENSHOT);
      //  String preview = getInputData().getString(KEY_PREVIEW);
       /* Integer song = getInputData().getInt(KEY_SONG, 0);
        if (song <= 0) {
            song = null;
        }*/
        String tokenT =  loginResponsePref.getInstance(getApplicationContext()).getToken();
        String description = getInputData().getString(KEY_DESCRIPTION);
        String tags = getInputData().getString(KEY_TAGS);
        String language = getInputData().getString(KEY_LANGUAGE);
        long duration = VideoUtil.getDuration(
                getApplicationContext(), Uri.fromFile(new File(video)));
       // boolean _private = getInputData().getBoolean(KEY_PRIVATE, false);
       // boolean comments = getInputData().getBoolean(KEY_COMMENTS, false);
     /*   REST rest = MainApplication.getContainer().get(REST.class);
        Call<Wrappers.Single<Clip>> call = rest.clipsCreate(
                MultipartBody.Part.createFormData("video", "video.mp4", RequestBody.create(null, new File(video))),
                MultipartBody.Part.createFormData("screenshot", "screenshot.png", RequestBody.create(null, new File(screenshot))),
                MultipartBody.Part.createFormData("preview", "preview.gif", RequestBody.create(null, new File(preview))),
                song != null ? RequestBody.create(null, song + "") : null,
                description != null ? RequestBody.create(null, description) : null,
                RequestBody.create(null, language),
                RequestBody.create(null, _private ? "1" : "0"),
                RequestBody.create(null, comments ? "1" : "0"),
                RequestBody.create(null, duration + "")
        );*/

        Call<uploadVideoResponseModel> call = ApiClient.getUserService().uploadVideo(
                tokenT != null ? RequestBody.create(null, tokenT) : null,
                tokenT != null ? RequestBody.create(null, tags) : null,
                MultipartBody.Part.createFormData("video", "video.mp4", RequestBody.create(null, new File(video)))
        );

        Response<uploadVideoResponseModel> response = null;
        try {
            response = call.execute();
        } catch (Exception e) {
            Log.e(TAG, "Failed when updating device token with server.", e);
        }
        if (response != null && response.isSuccessful()) {
            return Result.success();
        }
        return Result.failure();
    }
}
