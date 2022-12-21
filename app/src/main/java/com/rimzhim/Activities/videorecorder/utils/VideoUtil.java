package com.rimzhim.Activities.videorecorder.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.util.Size;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

final public class VideoUtil {

    private static final String TAG = "ClipUtil";

    @NotNull
    public static Size getDimensions(String path) {
        int width = 0, height = 0;
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            String w = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String h = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            if (w != null && h != null) {
                width = Integer.parseInt(w);
                height = Integer.parseInt(h);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to extract thumbnail from " + path, e);
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }

        return new Size(width, height);
    }

    public static long getDuration(Context context, Uri uri) {
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (duration != null) {
                return Long.parseLong(duration);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to extract duration from " + uri, e);
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }

        return 0;
    }

    @Nullable
    public static Bitmap getFrameAtTime(String path, long micros) {
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (duration != null) {
                long millis = Long.parseLong(duration);
                if (micros > TimeUnit.MILLISECONDS.toMicros(millis)) {
                    return mmr.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(millis));
                }
                return mmr.getFrameAtTime(micros);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to extract thumbnail from " + path, e);
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }

        return null;
    }
}
