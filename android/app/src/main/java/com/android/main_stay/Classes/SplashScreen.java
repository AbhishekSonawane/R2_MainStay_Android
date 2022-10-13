package com.android.main_stay.Classes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.main_stay.R;
import com.android.main_stay.utils.PreferenceHelper;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private PreferenceHelper mPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mPreferenceHelper = new PreferenceHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("NonStop", "On Resume");
        if (!mPreferenceHelper.getBoolean(R2Values.Commons.ISUSER_LOGGEDIN)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intprof = new Intent(SplashScreen.this, TabActivity.class);
                    startActivity(intprof);
                    finish();
                }
            }, 1000);
        }
    }

}
