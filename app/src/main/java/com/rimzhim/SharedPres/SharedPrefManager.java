package com.rimzhim.SharedPres;

import android.content.Context;
import android.content.SharedPreferences;

import com.rimzhim.ModelClasses.LoginResponse;


public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "userData";
    private static final String KEY_ID = "id";
    private static final String KEY_ROLE_ID = "roleId";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_STATE_ID = "stateId";
    private static final String KEY_CITY_ID = "cityId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_REFERRAL_CODE = "referralCode";
    private static final String DOB = "dob";
    private static final String LONGITUDE = "longitude";
    private static final String GENDER = "gender";
    private static final String WALLET = "wallet";
    private static final String REFER_USER_ID = "referUserId";
    private static final String LATITUDE = "latitude";
    private static final String STATUS = "status";
    private static final String BIO = "bio";
    private static final String WINNING = "winning";
    private static final String BACKGROUND_IMAGE = "backgroundImage";
    private static final String IMAGE = "image";
    private static final String IS_DELETE = "isDelete";
    private static final String ADDRESS = "address";
    private static final String CREATED_AT = "createdAt";
    private static final String UPDATED_AT = "updatedAt";
    private static final String TIME_OF_BIRTH = "timeOfBirth";
    private static final String SOCIAL_ID = "socialId";
    private static final String SOCIAL_TYPE = "socialType";
    private static SharedPrefManager mInstance;
    private static Context ctx;








    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(LoginResponse.User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putInt(KEY_ROLE_ID, user.getRoleId());
        editor.putString(KEY_NAME,user.getName());
        editor.putString(KEY_EMAIL,user.getEmail());
        editor.putString(KEY_PHONE,user.getPhone());
        editor.putString(KEY_STATE_ID,String.valueOf(user.getStateId()));
        editor.putString(KEY_CITY_ID,String.valueOf(user.getCityId()));
        editor.putString(KEY_USER_NAME,user.getUser_name());
        editor.putString(KEY_REFERRAL_CODE,user.getReferralCode());
        editor.putString(DOB,String.valueOf(user.getDob()));
        editor.putString(LONGITUDE,String.valueOf(user.getLongitude()));
        editor.putString(GENDER,user.getGender());
        editor.putString(WALLET,user.getWallet());
        editor.putString(REFER_USER_ID,user.getReferUserId());
        editor.putString(LATITUDE,String.valueOf(user.getLatitude()));
        editor.putInt(STATUS,user.getStatus());
        editor.putString(BIO,user.getBio());
        editor.putString(WINNING ,user.getBio());
        editor.putString(BACKGROUND_IMAGE ,user.getBackground_image());
        editor.putString(IMAGE ,user.getImage());
        editor.putInt(IS_DELETE ,user.getIsDelete());
        editor.putString(ADDRESS,user.getAddress());
        editor.putString(CREATED_AT,user.getCreatedAt());
        editor.putString(UPDATED_AT,user.getUpdatedAt());
        editor.putString(TIME_OF_BIRTH,user.getTimeOfBirth());
        editor.putString(SOCIAL_ID,String.valueOf(user.getSocialId()));
        editor.putString(SOCIAL_TYPE,String.valueOf(user.getSocialType()));
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    //this method will give the logged in user
    public LoginResponse.User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new LoginResponse.User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_ROLE_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_STATE_ID, null),
                sharedPreferences.getString(KEY_CITY_ID, null),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_REFERRAL_CODE, null),
                sharedPreferences.getString(DOB, null),
                sharedPreferences.getString(LONGITUDE, null),
                sharedPreferences.getString(GENDER, null),
                sharedPreferences.getString(WALLET, null),
                sharedPreferences.getString(REFER_USER_ID, null),
                sharedPreferences.getString(LATITUDE, null),
                sharedPreferences.getInt(STATUS, -1),
                sharedPreferences.getString(BIO, null),
                sharedPreferences.getString(WINNING, null),
                sharedPreferences.getString(BACKGROUND_IMAGE, null),
                sharedPreferences.getString(IMAGE, null),
                sharedPreferences.getInt(IS_DELETE, -1),
                sharedPreferences.getString(ADDRESS, null),
                sharedPreferences.getString(CREATED_AT, null),
                sharedPreferences.getString(UPDATED_AT, null),
                sharedPreferences.getString(TIME_OF_BIRTH, null),
                sharedPreferences.getString(SOCIAL_ID, null),
                sharedPreferences.getString(SOCIAL_TYPE, null)

        );
    }


    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
