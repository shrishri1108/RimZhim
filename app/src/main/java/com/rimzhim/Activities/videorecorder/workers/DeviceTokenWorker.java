package com.rimzhim.Activities.videorecorder.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pixplicity.easyprefs.library.Prefs;
import com.rimzhim.Activities.videorecorder.SharedConstants;

import org.json.JSONObject;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DeviceTokenWorker extends Worker {

    private static final String TAG = "DeviceTokenWorker";

    public DeviceTokenWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String token = Prefs.getString(SharedConstants.PREF_FCM_TOKEN, null);
        int id = Prefs.getInt(SharedConstants.PREF_DEVICE_ID, 0);
/*        REST rest = MainApplication.getContainer().get(REST.class);
        Call<ResponseBody> call;
        if (id > 0) {
            call = rest.devicesUpdate(id, token);
        } else {
            call = rest.devicesCreate("android", "fcm", token);
        }
        Response<ResponseBody> response = null;
        try {
            response = call.execute();
            if (id <= 0 && response.isSuccessful()) {
                //noinspection ConstantConditions
                String content = response.body().string();
                JSONObject object = new JSONObject(content);
                id = object.getInt("id");
                Prefs.putInt(SharedConstants.PREF_DEVICE_ID, id);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed when updating device token with server.", e);
        }
        if (response != null && response.isSuccessful()) {
            return Result.success();
        }*/
        return Result.failure();
    }
}
