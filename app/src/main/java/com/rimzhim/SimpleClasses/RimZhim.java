package com.rimzhim.SimpleClasses;



import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.drawee.backends.pipeline.Fresco;

import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.pixplicity.easyprefs.library.Prefs;
import com.rimzhim.Activities.NoInternetA;
import com.rimzhim.BuildConfig;
import com.rimzhim.Interfaces.InternetCheckCallback;
import com.rimzhim.R;
import com.smartnsoft.backgrounddetector.BackgroundDetectorCallback;
import com.smartnsoft.backgrounddetector.BackgroundDetectorHandler;

import java.io.File;
import java.util.HashMap;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;


/**
 * Created by qboxus on 3/18/2019.
 */

public class RimZhim extends Application implements Application.ActivityLifecycleCallbacks, BackgroundDetectorHandler.OnVisibilityChangedListener {

    public static Context appLevelContext;
    public static SimpleCache simpleCache = null;
    public static LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = null;
    public static ExoDatabaseProvider exoDatabaseProvider = null;
    public static Long exoPlayerCacheSize = (long) (90 * 1024 * 1024);
    private HttpProxyCacheServer proxy;
    private BackgroundDetectorHandler backgroundDetectorHandler;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        appLevelContext=getApplicationContext();
        registerActivityLifecycleCallbacks(this);
        backgroundDetectorHandler = new BackgroundDetectorHandler(new BackgroundDetectorCallback(BackgroundDetectorHandler.ON_ACTIVITY_RESUMED, this));
       /* Fresco.initialize(this,getDefaultImagePipelineConfig(this));
        Paper.init(this);
        FirebaseApp.initializeApp(this);
        addFirebaseToken();
        setUserOnline();*/

        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(this);
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(getCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
            if (simpleCache.getCacheSpace() >= 400207768) {
                freeMemory();
            }

        }

       // initCrashActivity();
      //  initConfig();
        Functions.createNoMediaFile(getApplicationContext());

        Fresco.initialize(this);
        if (BuildConfig.DEBUG) {
     /*       RequestConfiguration configuration = new RequestConfiguration.Builder()
                    .setTestDeviceIds(
                            Collections.singletonList(getString(R.string.admob_test_device_id)))
                    .build();
            MobileAds.setRequestConfiguration(configuration);*/
        }

        new Prefs.Builder()
                .setContext(this)
                .setUseDefaultSharedPreference(true)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(
                    getString(R.string.notification_channel_id),
                    getString(R.string.notification_channel_name),
                    Notification.VISIBILITY_PUBLIC,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
        }

    }



   /* public static HashMap<String, UserOnlineModel> allOnlineUser=new HashMap<>();
    ChildEventListener onlineEventListener;
    DatabaseReference rootref;
    private void setUserOnline() {
        rootref = FirebaseDatabase.getInstance().getReference();
        addOnlineListener();
    }*/

 /*   public void addOnlineListener(){
        if (onlineEventListener==null)
        {
            addOnlineStatus();
            onlineEventListener =new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (!(TextUtils.isEmpty(snapshot.getValue().toString())))
                    {
                        UserOnlineModel item=snapshot.getValue(UserOnlineModel.class);
                        allOnlineUser.put(item.getUserId(),item);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    if (!(TextUtils.isEmpty(snapshot.getValue().toString())))
                    {
                        UserOnlineModel item=snapshot.getValue(UserOnlineModel.class);
                        allOnlineUser.remove(item.getUserId());
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            rootref.child(Variables.onlineUser).addChildEventListener(onlineEventListener);
        }
    }*/

  /*  public void removeOnlineListener() {
        if (rootref!=null && onlineEventListener != null) {
            removeOnlineStatus();
            rootref.child(Variables.onlineUser).removeEventListener(onlineEventListener);
            onlineEventListener=null;
        }
    }

    private void removeOnlineStatus() {
        if (Functions.getSharedPreference(getApplicationContext()).getBoolean(Variables.IS_LOGIN,false))
        {
            rootref.child(Variables.onlineUser).child(Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_ID,"0"))
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(com.qboxus.musictok.Constants.tag,"removeOnlineStatus: "+Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_ID,"0"));
                };
            });

        }
    }
*/
   /* private void addOnlineStatus() {
        if (Functions.getSharedPreference(getApplicationContext()).getBoolean(Variables.IS_LOGIN,false))
        {
            UserOnlineModel onlineModel=new UserOnlineModel();
            onlineModel.setUserId(Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_ID,"0"));
            onlineModel.setUserName(Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_NAME,""));
            onlineModel.setUserPic(Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_PIC,""));

            rootref.child(Variables.onlineUser).child(Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_ID,"0")).onDisconnect().removeValue();
            rootref.child(Variables.onlineUser).child(Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_ID,"0")).keepSynced(true);
            rootref.child(Variables.onlineUser).child(Functions.getSharedPreference(getApplicationContext()).getString(Variables.U_ID,"0")).setValue(onlineModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(com.qboxus.musictok.Constants.tag,"addOnlineStatus: "+onlineModel.getUserId());
                        };
                    });

        }
    }*/





    // below code is for cache the videos in local
    public static HttpProxyCacheServer getProxy(Context context) {
        RimZhim app = (RimZhim) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)
                .maxCacheFilesCount(20)
                .cacheDirectory(new File(Functions.getAppFolder(this)+"videoCache"))
                .build();
    }




    // check how much memory is available for cache video
    public void freeMemory() {
        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    // delete the cache if it is full
    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }





  /*  private RtcEngine mRtcEngine;
    private EngineConfig mGlobalConfig = new EngineConfig();
    private AgoraEventHandler mHandler = new AgoraEventHandler();
    private StatsManager mStatsManager = new StatsManager();*/

  /*  private void initConfig() {

        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), getString(R.string.agora_app_id), mHandler);
            mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences pref = PrefManager.getPreferences(getApplicationContext());
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX));

        boolean showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, false);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);

        mGlobalConfig.setMirrorLocalIndex(pref.getInt(Constants.PREF_MIRROR_LOCAL, 0));
        mGlobalConfig.setMirrorRemoteIndex(pref.getInt(Constants.PREF_MIRROR_REMOTE, 0));
        mGlobalConfig.setMirrorEncodeIndex(pref.getInt(Constants.PREF_MIRROR_ENCODE, 0));
    }

    public EngineConfig engineConfig() {
        return mGlobalConfig;
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public StatsManager statsManager() {
        return mStatsManager;
    }

    public void registerEventHandler(EventHandler handler) {
        mHandler.addHandler(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        mHandler.removeHandler(handler);
    }
*/



   /* public void initCrashActivity(){
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
                .enabled(true)
                .showErrorDetails(true)
                .showRestartButton(true)
                .logErrorOnRestart(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                .restartActivity(CustomErrorActivity.class)
                .errorActivity(CustomErrorActivity.class)
                .apply();
    }
*/


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {

    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityStarted(Activity activity)
    {

    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        backgroundDetectorHandler.onActivityResumed(activity);
        Functions.RegisterConnectivity(activity, new InternetCheckCallback() {
            @Override
            public void GetResponse(String requestType, String response) {
                if(response.equalsIgnoreCase("disconnected")) {
                   // removeOnlineListener();
                    activity.startActivity(new Intent(activity, NoInternetA.class));
                    activity.overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_top);
                }
                else
                {
                    //addOnlineListener();
                }
            }
        });
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
        backgroundDetectorHandler.onActivityPaused(activity);
        Functions.unRegisterConnectivity(activity);
    }

    @Override
    public void onActivityStopped(Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {

    }

    @Override
    public void onAppGoesToBackground(Context context)
    {
       /* Log.d(com.rimzhim.Constants.tag,"onAppGoesToBackground");
        removeOnlineListener();*/
    }

    @Override
    public void onAppGoesToForeground(Context context)
    {
       /* Log.d(com.qboxus.musictok.Constants.tag,"onAppGoesToForeground");
        addOnlineListener();*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressWarnings("SameParameterValue")
    private void createChannel(String id, String name, int visibility, int importance) {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.enableLights(true);
        channel.setLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
        channel.setLockscreenVisibility(visibility);
        if (importance == NotificationManager.IMPORTANCE_LOW) {
            channel.setShowBadge(false);
        }

        NotificationManager nm =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(channel);
    }


}
