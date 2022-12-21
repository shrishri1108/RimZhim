package com.rimzhim.Activities.videorecorder.workers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class VideoTrimmerWorker extends Worker {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;
    public static final String KEY_INPUT = "input";
    public static final String KEY_OUTPUT = "output";
    public static final String KEY_START = "start";
    public static final String KEY_END = "end";
    private static final String TAG = "VideoTrimWorker";

    public VideoTrimmerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String input = getInputData().getString(KEY_INPUT);
        String output = getInputData().getString(KEY_OUTPUT);
        long start = getInputData().getLong(KEY_START, 0);
        long end = getInputData().getLong(KEY_END, 0);
        try {
            crop(input, output, start, end);
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Encountered error when trimming " + input, e);
        }
        return Result.failure();
    }


    @SuppressLint("WrongConstant")
    private static void crop(String src, String dest, long start, long end) throws IOException {
        MediaExtractor extractor = new MediaExtractor();
        extractor.setDataSource(src);
        int tracks = extractor.getTrackCount();
        MediaMuxer muxer = new MediaMuxer(dest, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        HashMap<Integer, Integer> indices = new HashMap<>(tracks);
        int size = -1;
        for (int i = 0; i < tracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            boolean use = false;
            //noinspection ConstantConditions
            if (mime.startsWith("audio/")) {
                use = true;
            } else if (mime.startsWith("video/")) {
                use = true;
            }
            if (use) {
                extractor.selectTrack(i);
                indices.put(i, muxer.addTrack(format));
                if (format.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
                    int revised = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                    size = Math.max(revised, size);
                }
            }
        }
        if (size < 0) {
            size = DEFAULT_BUFFER_SIZE;
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(src);
        String rotation = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if (rotation != null) {
            int degrees = Integer.parseInt(rotation);
            if (degrees >= 0) {
                muxer.setOrientationHint(degrees);
            }
        }
        if (start > 0) {
            extractor.seekTo(start * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        }
        int offset = 0;
        int i;
        ByteBuffer buffer = ByteBuffer.allocate(size);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        try {
            muxer.start();
            while (true) {
                info.offset = offset;
                info.size = extractor.readSampleData(buffer, offset);
                if (info.size < 0) {
                    Log.d(TAG, "Saw input EOS.");
                    info.size = 0;
                    break;
                } else {
                    info.presentationTimeUs = extractor.getSampleTime();
                    if (end > 0 && info.presentationTimeUs > (end * 1000)) {
                        Log.d(TAG, "The current sample is over the trim end time.");
                        break;
                    } else {
                        info.flags = extractor.getSampleFlags();
                        i = extractor.getSampleTrackIndex();
                        //noinspection ConstantConditions
                        muxer.writeSampleData(indices.get(i), buffer, info);
                        extractor.advance();
                    }
                }
            }
            muxer.stop();
        } catch (IllegalStateException e) {
            Log.w(TAG, "The source video file is malformed");
        } finally {
            muxer.release();
        }
    }
}
