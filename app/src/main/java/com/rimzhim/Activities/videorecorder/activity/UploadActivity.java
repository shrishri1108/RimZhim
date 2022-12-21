package com.rimzhim.Activities.videorecorder.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.rimzhim.Activities.videorecorder.utils.VideoUtil;
import com.rimzhim.Activities.videorecorder.workers.GeneratePreviewWorker;
import com.rimzhim.Activities.videorecorder.workers.UploadClipWorker;
import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Functions;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;



public class UploadActivity extends AppCompatActivity {

    public static final String EXTRA_SONG = "song";
    public static final String EXTRA_VIDEO = "video";
    public static final String TAG = "PostClipActivity";

    private UploadActivityViewModel mModel;
    private int mSong;
    private String mClip;
    ImageView backBtn;
    Button postBtn;
    EditText tag;
    private int intCount = 0, initialStringLength = 0;
    private String strText = "";
    int counter = -1;
    CheckBox checkBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Functions.whiteStatusBar(this);
        backBtn =(ImageView)findViewById(R.id.changebackIV);
        tag =(EditText)findViewById(R.id.tag);
        postBtn =(Button)findViewById(R.id.postBtn);
        checkBox =(CheckBox)findViewById(R.id.checkbox_meat);
        backBtn.setOnClickListener(view -> { finish(); });
       // ImageButton close = findViewById(R.id.header_back);
        /*close.setImageResource(R.drawable.ic_baseline_close_24);
        close.setOnClickListener(view -> finish());*/
     //   TextView title = findViewById(R.id.header_title);
       // title.setText(R.string.upload_label);
        //ImageButton done = findViewById(R.id.header_more);
       /* done.setImageResource( R.drawable.ic_baseline_check_24);
        done.setOnClickListener(v -> uploadToServer());*/
        postBtn.setOnClickListener(view -> {
            if(!checkBox.isChecked()){
                Toast.makeText(getApplicationContext(),"Please Accept the Responsibility of this Content", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadToServer();


        });

        mModel = new ViewModelProvider(this).get(UploadActivityViewModel.class);
        mSong = getIntent().getIntExtra(EXTRA_SONG, 0);
        mClip = getIntent().getStringExtra(EXTRA_VIDEO);
        EditText description = findViewById(R.id.description);
        description.setText(mModel.description);
        description.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                mModel.description = editable.toString();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.language_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  /*      Spinner language = findViewById(R.id.language);
        language.setAdapter(adapter);
        List<String> codes = Arrays.asList(
                getResources().getStringArray(R.array.language_codes)
        );
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mModel.language = codes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });*/
     /*   if (TextUtils.isEmpty(mModel.language)) {
            LocaleListCompat locales =
                    ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
            String locale = locales.get(0).getISO3Language();
            if (codes.contains(locale)) {
                mModel.language = locale;
            } else {
                mModel.language = codes.get(0);
            }
        }
        language.setSelection(codes.indexOf(mModel.language));*/
        Bitmap image = VideoUtil.getFrameAtTime(mClip, TimeUnit.SECONDS.toMicros(3));
        ImageView thumbnail = findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(image);
       /* SwitchMaterial private2 = findViewById(R.id.private2);
        private2.setChecked(mModel.private2);
        private2.setOnCheckedChangeListener((button, checked) -> mModel.private2 = checked);
        SwitchMaterial comments = findViewById(R.id.comments);
        comments.setChecked(mModel.comments);
        comments.setOnCheckedChangeListener((button, checked) -> mModel.comments = checked);*/

        tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              /*  String strET = tag.getText().toString();
                String[] str = strET.split(" ");
                int cnt = 0;
                if (tag.length() != initialStringLength && tag.length() != 0) {
                    if (!strET.substring(strET.length() - 1).equals(" ")) {
                        initialStringLength = tag.length();
                        cnt = intCount;
                        for (int j = 0; j < str.length; j++)
                            if (str[j].charAt(0) == '#')
                                strText = strText + " " + "<font color='#ED4D4D'>" + str[j] + "</font>";


                            else
                                strText = strText + " " + str[j];
                    }
                    if (intCount == cnt) {
                        intCount = str.length;
                        tag.setText(Html.fromHtml(strText));
                        tag.setSelection(tag.getText().toString().length());
                    }
                } else {
                    strText = "";
                }


*/

                if (tag.length() > counter) {
                    counter = tag.length();
                    if (tag.length() > 0) {
                        String lastChar = charSequence.toString().substring(charSequence.length() - 1);
                        if (lastChar.equals(" ")) {
                            //
                            tag.setTextColor(Color.parseColor("#FF000000"));

                        } else if (lastChar.equals("#")) {
                          ///
                            tag.setTextColor(Color.parseColor("#ED4D4D"));

                        }

                        String hash_tags = tag.getText().toString();
                        String[] separated = hash_tags.split("#");
                        for (String item : separated) {
                            if (item != null && !item.equals("")) {
                                if (item.contains(" ")) {
                                    //stop calling api
                                } else {
                                    String string1 = item.replace("#", "");
                                    //pageCount=0;
                                    //callApiForHashTag(string1);
                                }
                            }
                        }

                    } else {
                        //findViewById(R.id.hashtag_layout).setVisibility(View.GONE);
                        tag.setTextColor(Color.parseColor("#FF000000"));

                    }
                } else {
                    if (tag.length() == 1) {
                        counter = -1;
                    } else {
                        counter--;
                    }
                }

              //  aditionalDetailsTextCount.setText(descriptionEdit.getText().length() + "/" + Constants.VIDEO_DESCRIPTION_CHAR_LIMIT);


            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });

    }





    private void uploadToServer() {
        String tags = tag.getText().toString().trim();
        String tagsString = "";
        if(!TextUtils.isEmpty(tags)){
            tagsString  = Functions.convertHastTagString(tag.getText().toString());
            Log.d("tags=======", tagsString);
        }


        File preview = new File(getFilesDir(), UUID.randomUUID().toString());
        File screenshot = new File(getFilesDir(), UUID.randomUUID().toString());
        Data data1 = new Data.Builder()
                .putString(GeneratePreviewWorker.KEY_INPUT, mClip)
                .putString(GeneratePreviewWorker.KEY_SCREENSHOT, screenshot.getAbsolutePath())
                .putString(GeneratePreviewWorker.KEY_PREVIEW, preview.getAbsolutePath())
                .build();
        OneTimeWorkRequest request1 = new OneTimeWorkRequest.Builder(GeneratePreviewWorker.class)
                .setInputData(data1)
                .build();
        Data data2 = new Data.Builder()
                //.putInt(UploadClipWorker.KEY_SONG, mSong)
                .putString(UploadClipWorker.KEY_VIDEO, mClip)
              //  .putString(UploadClipWorker.KEY_SCREENSHOT, screenshot.getAbsolutePath())
               // .putString(UploadClipWorker.KEY_PREVIEW, preview.getAbsolutePath())
                .putString(UploadClipWorker.KEY_DESCRIPTION, mModel.description)
                .putString(UploadClipWorker.KEY_TAGS, tagsString)
                .putString(UploadClipWorker.KEY_LANGUAGE, mModel.language)
              //  .putBoolean(UploadClipWorker.KEY_PRIVATE, mModel.private2)
              //  .putBoolean(UploadClipWorker.KEY_COMMENTS, mModel.comments)
                .build();
        OneTimeWorkRequest request2 = new OneTimeWorkRequest.Builder(UploadClipWorker.class)
                .setInputData(data2)
                .build();
        WorkManager.getInstance(this).beginWith(request1).then(request2).enqueue();
        Toast.makeText(this, R.string.uploading_message, Toast.LENGTH_SHORT).show();

        finish();
    }

    public static class UploadActivityViewModel extends ViewModel {
        public String description = "";
        public String language = null;
        public boolean private2 = false;
        public boolean comments = true;
    }
}
