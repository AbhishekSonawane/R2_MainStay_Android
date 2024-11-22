package com.erudito.mainstaypeople.Classes;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cunoraz.gifview.library.GifView;
import com.android.main_stay.R;

public class BoostMeGifActivity extends AppCompatActivity {

    GifView gf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_me_gif);

        gf = findViewById(R.id.gif1);
        gf.setVisibility(View.VISIBLE);
        gf.setGifResource(R.drawable.sample);
        gf.play();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(BoostMeGifActivity.this, BoostMeActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);

//        gf.pause();
    }
}
