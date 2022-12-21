package com.rimzhim.MainMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.rimzhim.R;
import com.rimzhim.SimpleClasses.Constants;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.SimpleClasses.Variables;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainMenuActivity;
    private MainMenuFragment mainMenuFragment;
    long mBackPressed;
    Context context;
    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        try { getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }catch (Exception e){}
        Functions.setLocale(Functions.getSharedPreference(MainActivity.this).getString(Variables.APP_LANGUAGE_CODE,Variables.DEFAULT_LANGUAGE_CODE)
                , this, MainActivity.class,false);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        mainMenuActivity = this;

        intent = getIntent();
       // chechDeepLink(intent);


       /* if (Functions.getSharedPreference(this).getBoolean(Variables.IS_LOGIN, false)) {
            getPublicIP();
        }
*/
       /* if(!Functions.getSharedPreference(this).getBoolean(Variables.IsExtended,false))
            checkLicence();

        if (savedInstanceState == null) {

            initScreen();

        } else {
            Functions.printLog(Constants.tag, "savedInstanceState : null "+getSupportFragmentManager().getFragments().get(0));
            mainMenuFragment = (MainMenuFragment) getSupportFragmentManager().getFragments().get(0);
        }*/


        initScreen();
       // Functions.printLog(Constants.tag, "savedInstanceState : null "+getSupportFragmentManager().getFragments().get(0));
      //  mainMenuFragment = (MainMenuFragment) getSupportFragmentManager().getFragments().get(0);


        Functions.makeDirectry(Functions.getAppFolder(this)+Variables.APP_HIDED_FOLDER);
        Functions.makeDirectry(Functions.getAppFolder(this)+Variables.DRAFT_APP_FOLDER);

      //  setIntent(null);

    }

    private void initScreen() {
        mainMenuFragment = new MainMenuFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mainMenuFragment)
                .commit();

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        if (!mainMenuFragment.onBackPressed()) {
            int count = this.getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                if (mBackPressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return;
                } else {
                    Functions.showToast(getBaseContext(), getString(R.string.tap_to_exist));
                    mBackPressed = System.currentTimeMillis();

                }
            } else {
                super.onBackPressed();
            }
        }

    }
}