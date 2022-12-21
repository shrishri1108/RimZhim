package com.rimzhim.SharedPres;

import android.content.Context;
import android.content.SharedPreferences;

public class onBoardingPref {
    private static final String SHARED_PREF_NAME = "FirstOpen";
    private static onBoardingPref mInstance;
    private static Context ctx;


    private onBoardingPref(Context context) {
        ctx = context;
    }
    public static synchronized onBoardingPref getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new onBoardingPref(context);
        }
        return mInstance;
    }

    public void save(Boolean data) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("save", data);
        editor.apply();
    }

    public Boolean getAct() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return   sharedPreferences.getBoolean("save",false);
    }

}
