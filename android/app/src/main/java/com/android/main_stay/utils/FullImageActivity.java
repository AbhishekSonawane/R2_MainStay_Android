package com.android.main_stay.utils;

/**
 * Created by nonstop on 8/3/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.main_stay.Classes.R2Values;
import com.android.main_stay.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;

public class FullImageActivity extends AppCompatActivity {


    TouchImageView fullimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("pride", Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean("from_img_fullscreen", true).apply();
    }

    private void init() {
        Picasso.with(FullImageActivity.this).setLoggingEnabled(true);
        String imageName = R2Values.Web.BASE_URL + getIntent().getStringExtra("imageName");
        if (imageName == null) {
            Toast.makeText(getApplicationContext(), "Please Click on Get Image Button", Toast.LENGTH_LONG).show();
        } else {
            try {
                Log.d("NonStop", "Image URL in FullImageActivity: " + imageName);
                final TouchImageView touchImageView = new TouchImageView(FullImageActivity.this);
                Picasso.with(FullImageActivity.this).cancelRequest(touchImageView);
                Picasso.with(FullImageActivity.this)
                        .load(imageName)
                        .placeholder(R.drawable.default_post_image) // optional
                        .error(R.drawable.default_post_image)         // optional
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                //Set it in the ImageView
                                touchImageView.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }
                        });
                touchImageView.setMaxZoom(4f);
                setContentView(touchImageView);

            } catch (Exception e) {
                Log.d("NonStop", "Exception: " + e.toString());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

