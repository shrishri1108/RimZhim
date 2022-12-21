package com.rimzhim.SimpleClasses;


import static android.content.Context.CONNECTIVITY_SERVICE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rimzhim.Activities.OtherUserProfileActivity;
import com.rimzhim.Interfaces.FragmentCallBack;
import com.rimzhim.Interfaces.InternetCheckCallback;
import com.rimzhim.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by qboxus on 2/20/2019.
 */

public class Functions {


    // change the color of status bar into black
    public static void blackStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();

        int flags = view.getSystemUiVisibility();
        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        activity.getWindow().setStatusBarColor(Color.BLACK);
    }


    public static void PrintHashKey(Context context) {
        try {
            final PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d(Constants.tag, "KeyHash : " + hashKey);
            }
        } catch (Exception e) {
            Log.e(Constants.tag, "error:", e);
        }
    }

    // change the color of status bar into white
    public static void whiteStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        int flags = view.getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        view.setSystemUiVisibility(flags);
        activity.getWindow().setStatusBarColor(Color.WHITE);
    }


    // close the keybord
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    // open the keyboard
    public static void showKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    // retun the sharepref instance
    public static SharedPreferences getSharedPreference(Context context) {
        if (Variables.sharedPreferences != null)
            return Variables.sharedPreferences;
        else {
            Variables.sharedPreferences = context.getSharedPreferences(Variables.PREF_NAME, Context.MODE_PRIVATE);
            return Variables.sharedPreferences;
        }

    }

    // print any kind of log
    public static void printLog(String title, String text) {
        if (!Constants.IS_SECURE_INFO) {
            if (title != null && text != null)
                Log.d(title, text);
        }

    }

    // get the audio file duration that is store in our directory
    public static long getfileduration(Context context, Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Functions.parseInterger(durationStr);

            return file_duration;
        } catch (Exception e) {
            Log.d(Constants.tag, "Exception: " + e);
        }
        return 0;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // change string value to integer
    public static int parseInterger(String value) {
        if (value != null && !value.equals("")) {
            return Integer.parseInt(value);
        } else
            return 0;
    }

    // format the count value
    public static String getSuffix(String value) {
        try {

            if (value != null && (!value.equals("") && !value.equalsIgnoreCase("null"))) {
                long count = Long.parseLong(value);
                if (count < 1000)
                    return "" + count;
                int exp = (int) (Math.log(count) / Math.log(1000));
                return String.format(Locale.ENGLISH, "%.1f %c",
                        count / Math.pow(1000, exp),
                        "kMBTPE".charAt(exp - 1));
            } else {
                return "0";
            }
        } catch (Exception e) {
            return value;
        }

    }


    // return  the rundom string of given length
    public static String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    public static String removeSpecialChar(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }

    // show loader of simple messages
    public static void showAlert(Context context, String title, String Message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Message)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    // dialog for show loader for showing dialog with title and descriptions
  /*  public static void showAlert(Context context, String title, String description, final CallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (callBack != null)
                    callBack.getResponse("alert", "OK");
            }
        });
        builder.create();
        builder.show();

    }*/


    // dialog for show any kind of alert
 /*   public static void showAlert(Context context, String title, String Message, String postivebtn, String negitivebtn, final Callback callback) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Message)
                .setNegativeButton(negitivebtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //callback.onResponce("no");
                    }
                })
                .setPositiveButton(postivebtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                       // callback.onResponce("yes");

                    }
                }).show();
    }


*/


    // initialize the loader dialog and show
    public static Dialog dialog;


    public static void cancelLoader() {
        try {
            if (dialog != null || dialog.isShowing()) {
                dialog.cancel();
            }
        } catch (Exception e) {
            Log.d(Constants.tag, "Exception : " + e);
        }
    }


    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    // format the username
    public static String showUsername(String username) {
        if (username != null && username.contains("@"))
            return username;
        else
            return "@" + username;
    }


    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }


    public static boolean checkTimeDiffernce(Calendar current_cal, String date) {
        try {


            Calendar date_cal = Calendar.getInstance();

            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);
            Date d = null;
            try {
                d = f.parse(date);
                date_cal.setTime(d);
            } catch (Exception e) {
                e.printStackTrace();
            }

            long difference = (current_cal.getTimeInMillis() - date_cal.getTimeInMillis()) / 1000;


            Log.d(Constants.tag, "Tag : " + difference);

            if (difference < 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }


    }


    public static String changeDateTodayYesterday(Context context, String date) {
        try {
            Calendar current_cal = Calendar.getInstance();

            Calendar date_cal = Calendar.getInstance();

            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);
            Date d = null;
            try {
                d = f.parse(date);
                date_cal.setTime(d);
            } catch (Exception e) {
                e.printStackTrace();
            }


            long difference = (current_cal.getTimeInMillis() - date_cal.getTimeInMillis()) / 1000;

            if (difference < 86400) {
                if (current_cal.get(Calendar.DAY_OF_YEAR) - date_cal.get(Calendar.DAY_OF_YEAR) == 0) {

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    return sdf.format(d);
                } else
                    return context.getString(R.string.yesterday);
            } else if (difference < 172800) {
                return context.getString(R.string.yesterday);
            } else
                return (difference / 86400) + context.getString(R.string.day_ago);

        } catch (Exception e) {
            return date;
        }


    }


    public static String bitmapToBase64(Bitmap imagebitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

    public static Bitmap base64ToBitmap(String base_64) {
        Bitmap decodedByte = null;
        try {

            byte[] decodedString = Base64.decode(base_64, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {

        }
        return decodedByte;
    }


    public static boolean isShowContentPrivacy(Context context, String string_case, boolean isFriend) {
        if (string_case == null)
            return true;
        else {
            string_case = stringParseFromServerRestriction(string_case);

            if (string_case.equalsIgnoreCase("Everyone")) {
                return true;
            } else if (string_case.equalsIgnoreCase("Friends") &&
                    Functions.getSharedPreference(context).getBoolean(Variables.IS_LOGIN, false) && isFriend) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String stringParseFromServerRestriction(String res_string) {
        res_string = res_string.toUpperCase();
        res_string = res_string.replace("_", " ");
        return res_string;
    }

    public static String stringParseIntoServerRestriction(String res_string) {
        res_string = res_string.toLowerCase();
        res_string = res_string.replace(" ", "_");
        return res_string;
    }


    // make the directory on specific path
    public static void makeDirectry(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }


    // return the random string of 10 char
    public static String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    public static long getFileDuration(Context context, Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Functions.parseInterger(durationStr);

            return file_duration;
        } catch (Exception e) {

        }
        return 0;
    }

    // getCurrent Date
    public static String getCurrentDate(String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Calendar date = Calendar.getInstance();
        return format.format(date.getTime());
    }

    public static String getAppFolder(Context activity) {
        return activity.getExternalFilesDir(null).getPath() + "/";
    }


    // Bottom is all the Apis which is mostly used in app we have add it
    // just one time and whenever we need it we will call it


    public static Dialog indeterminantDialog;

 /*   public static void showIndeterminentLoader(Context context, boolean outside_touch, boolean cancleable) {

        indeterminantDialog = new Dialog(context);
        indeterminantDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        indeterminantDialog.setContentView(R.layout.item_indeterminant_progress_layout);
        indeterminantDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));


        if (!outside_touch)
            indeterminantDialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            indeterminantDialog.setCancelable(false);

        indeterminantDialog.show();

    }*/

/*

    public static void showIndeterminentLoader(Context context,String title, boolean outside_touch, boolean cancleable) {

        indeterminantDialog = new Dialog(context);
        indeterminantDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        indeterminantDialog.setContentView(R.layout.item_indeterminant_progress_layout);
        indeterminantDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));
        TextView tvTitle=indeterminantDialog.findViewById(R.id.tvTitle);
        if (title!=null && TextUtils.isEmpty(title))
        {
            tvTitle.setText(title);
        }
        if (!outside_touch)
            indeterminantDialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            indeterminantDialog.setCancelable(false);

        indeterminantDialog.show();

    }

*/

    public static void cancelIndeterminentLoader() {
        if (indeterminantDialog != null) {
            indeterminantDialog.cancel();
        }
    }


   /* public static void showDeterminentLoader(Context context, boolean outside_touch, boolean cancleable) {

        determinant_dialog = new Dialog(context);
        determinant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        determinant_dialog.setContentView(R.layout.item_determinant_progress_layout);
        determinant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));

        determinant_progress = determinant_dialog.findViewById(R.id.pbar);

        if (!outside_touch)
            determinant_dialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            determinant_dialog.setCancelable(false);

        determinant_dialog.show();

    }*/




    //store single account record

    // use this method for lod muliple account in case one one account logout and other one can logout


    // these function are remove the cache memory which is very helpfull in memmory managmet
    public static void deleteCache(Context context) {


        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static boolean deleteDir(File dir) {
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

    public static void copyFile(File sourceFile, File destFile) throws Exception {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    public static void showToast(Context context, String msg) {
        if (Constants.IS_TOAST_ENABLE) {
            Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
        }
    }

    // use for image loader and return controller for image load

    // use for image loader and return controller for image load


    public static String getFollowButtonStatus(String button, Context context) {
        String userStatus = button;
        if (userStatus.equalsIgnoreCase("following")) {
            return context.getString(R.string.following);
        } else if (userStatus.equalsIgnoreCase("friends")) {
            return context.getString(R.string.friends_);
        } else if (userStatus.equalsIgnoreCase("follow back")) {
            return context.getString(R.string.follow_back);
        } else {
            return context.getString(R.string.follow);
        }
    }


    public static boolean isNotificaitonShow(String userStatus) {
        if (userStatus.equalsIgnoreCase("following")) {
            return true;
        } else if (userStatus.equalsIgnoreCase("friends")) {
            return true;
        } else if (userStatus.equalsIgnoreCase("follow back")) {
            return true;
        } else {
            return false;
        }
    }


    //    app language change
    public static void setLocale(String lang, Activity context, Class<?> className, boolean isRefresh) {

        String[] languageArray = context.getResources().getStringArray(R.array.app_language_code);
        List<String> languageCode = Arrays.asList(languageArray);
        if (languageCode.contains(lang)) {
            Locale myLocale = new Locale(lang);
            Resources res = context.getBaseContext().getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = new Configuration();
            conf.setLocale(myLocale);
            res.updateConfiguration(conf, dm);
            context.onConfigurationChanged(conf);

            if (isRefresh) {
                updateActivity(context, className);
            }
        }
    }

    public static void updateActivity(Activity context, Class<?> className) {
        Intent intent = new Intent(context, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    //use to get Directory Storage Used Capacity
    public static String getDirectorySize(String path) {

        File dir = new File(path);

        if (dir.exists()) {
            long bytes = getFolderSize(dir);
            if (bytes < 1024) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            String pre = ("KMGTPE").charAt(exp - 1) + "";

            return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
        }

        return "0";
    }

    private static long getFolderSize(File dir) {
        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if (fileList[i].isDirectory()) {
                    result += getFolderSize(fileList[i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }






    //check rational permission status
    public static String getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return "blocked";
            }
            return "denied";
        }
        return "granted";
    }

    //show permission setting screen
    public static void showPermissionSetting(Context context, String message) {
        showDoubleButtonAlert(context, context.getString(R.string.permission_alert), message,
                context.getString(R.string.cancel_), context.getString(R.string.settings), false, new FragmentCallBack() {
                    @Override
                    public void onResponce(Bundle bundle) {
                        if (bundle.getBoolean("isShow", false)) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivity(intent);
                        }
                    }
                });
    }

    public static void showDoubleButtonAlert(Context context, String title, String message, String negTitle, String posTitle, boolean isCancelable, FragmentCallBack callBack) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(isCancelable);
        dialog.setContentView(R.layout.show_double_button_new_popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView tvtitle, tvMessage, tvPositive, tvNegative;
        tvtitle = dialog.findViewById(R.id.tvtitle);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvNegative = dialog.findViewById(R.id.tvNegative);
        tvPositive = dialog.findViewById(R.id.tvPositive);


        tvtitle.setText(title);
        tvMessage.setText(message);
        tvNegative.setText(negTitle);
        tvPositive.setText(posTitle);

        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShow", false);
                callBack.onResponce(bundle);
            }
        });
        tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isShow", true);
                callBack.onResponce(bundle);
            }
        });
        dialog.show();
    }


   // public static ProgressDialog progressDialog;
  public static KProgressHUD mProgress;

    public static void showProgressDialog(Context context/*, String msg*/) {
       /* progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);*/
        mProgress  = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(context.getString(R.string.progress_title))
                .setCancellable(false)
                .show();

    }

    public static void dismissProgressDialog() {
        //progressDialog.dismiss();
        mProgress.dismiss();
    }


    //    check app is exist or not
    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public static File getBitmapToUri(Context inContext, Bitmap bitmap, String fileName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File file = new File(Functions.getAppFolder(inContext) + fileName);
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    public static int showProgress(int totalCont, int left) {
        double progress = (Double.valueOf(left) * 100) / Double.valueOf(totalCont);
        return convert(progress);
    }

    public static int convert(double DoubleValue) {
        double tempD = DoubleValue;
        int i = (int) tempD * 100;
        int d = i / 100;
        return 100 - d;
    }

    public static void OpenOtherProfile(Context context, String userId) {
        Intent intent = new Intent(context, OtherUserProfileActivity.class);
        intent.putExtra("userId",userId);
        context.startActivity(intent);
    }

    public static Dialog determinant_dialog;
    public static ProgressBar determinant_progress;

    public static void showDeterminentLoader(Context context, boolean outside_touch, boolean cancleable) {

        determinant_dialog = new Dialog(context);
        determinant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        determinant_dialog.setContentView(R.layout.item_determinant_progress_layout);
        determinant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));

        determinant_progress = determinant_dialog.findViewById(R.id.pbar);

        if (!outside_touch)
            determinant_dialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            determinant_dialog.setCancelable(false);

        determinant_dialog.show();

    }

    public static void showLoadingProgress(int progress) {
        if (determinant_progress != null) {
            determinant_progress.setProgress(progress);

        }
    }

    public static void cancelDeterminentLoader() {
        if (determinant_dialog != null) {
            determinant_progress = null;
            determinant_dialog.cancel();
        }
    }

    public static BroadcastReceiver broadcastReceiver;
    public static IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    public static void unRegisterConnectivity(Context mContext) {
        try {
            if (broadcastReceiver != null)
                mContext.unregisterReceiver(broadcastReceiver);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void RegisterConnectivity(Context context, final InternetCheckCallback callback) {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnectedToInternet(context)) {
                    callback.GetResponse("alert", "connected");
                } else {
                    callback.GetResponse("alert", "disconnected");
                }
            }
        };

        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static Boolean isConnectedToInternet(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(Constants.tag, "Exception : "+e.getMessage());
            return false;
        }
    }


    public static void makeToast(Context context ,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static String convertHastTagString(String s){
        String result = "";
        for (int i=1; i <= s.length(); ++i) {
            char ch = s.charAt(s.length() - i);
            if (i % 3 == 1 && i > 1) {
                result = "," + result;
            }
            result = ch + result;
        }

        return result;
    }

    // use for image loader and return controller for image load


    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static void createNoMediaFile(Context context) {

        InputStream in = null;
        OutputStream out = null;

        try {

            //create output directory if it doesn't exist
            String path=getAppFolder(context)+"videoCache";
            Log.d(Constants.tag,"path: "+path);
            File dir = new File(path);
            Log.d(Constants.tag,"getAbsolutePath: "+dir.getAbsolutePath());
            File newFile = new File(dir, ".nomedia");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!newFile.exists()) {
                newFile.createNewFile();

                MediaScannerConnection.scanFile(context,
                        new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            }

        } catch (Exception e) {
            Log.e(Constants.tag, ""+e);
        }

    }

    public static String getTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    // use for image loader and return controller for image load
    public static DraweeController frescoImageLoad(String url, SimpleDraweeView simpleDrawee, boolean isGif)
    {
        if (!url.contains(Variables.http)) {
            String uriString ="https://rimjhim.appmantra.live/api/api/";
            url = uriString+url;
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .build();
        DraweeController controller;
        if (isGif)
        {
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDrawee.getController())
                    .setAutoPlayAnimations(true)
                    .build();
        }
        else
        {
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(simpleDrawee.getController())
                    .build();
        }



        return controller;
    }


}
