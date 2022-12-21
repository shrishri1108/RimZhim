package com.rimzhim.Activities.videorecorder;

import java.util.concurrent.TimeUnit;

final public class SharedConstants {

    public static final long MAX_DURATION = TimeUnit.SECONDS.toMillis(30);
    public static final long MIN_DURATION = TimeUnit.SECONDS.toMillis(5);

    public static final String PREF_DEVICE_ID = "device_id";
    public static final String PREF_FCM_TOKEN = "fcm_token";
    public static final String PREF_FCM_TOKEN_SYNCED_AT = "fcm_token_synced_at";
    public static final String PREF_INTRO_SHOWN = "intro_shown";
    public static final String PREF_SEEN_UNTIL = "seen_until";
    public static final String PREF_SERVER_TOKEN = "server_token";

    public static final int REQUEST_CODE_LOGIN_GOOGLE = 60600 + 1;
    public static final int REQUEST_CODE_LOGIN_PHONE = 60600 + 2;
    public static final int REQUEST_CODE_PICK_SONG = 60600 + 3;
    public static final int REQUEST_CODE_READ_STORAGE = 60600 + 4;
}
