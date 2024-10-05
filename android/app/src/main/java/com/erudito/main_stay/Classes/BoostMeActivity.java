package com.erudito.main_stay.Classes;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.android.main_stay.R;
import com.erudito.main_stay.models.LoginModel;
import com.erudito.main_stay.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.erudito.main_stay.Classes.MainFragment.mTabHost;

import androidx.appcompat.app.AppCompatActivity;

public class BoostMeActivity extends AppCompatActivity {

    private ProgressDialog pd;
    private JSONArray jarray;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    ImageView img_background;
    ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_me);

        ImageView close = findViewById(R.id.btnback);
        img_background = findViewById(R.id.img_background);

        mPreferenceHelper = new PreferenceHelper(this);
        gson = new Gson();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pd = new ProgressDialog(BoostMeActivity.this, R.style.MyTheme);
        //pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        BoostmeInfo();

        TextView btn1= findViewById(R.id.goToQuizList);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("","Go To Feeds View");
                TabActivity.mViewPager.setCurrentItem(2);
                finish();
            }
        });

        TextView btn= findViewById(R.id.goToAssignmentTextView);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("","Go To Assignment View");
//                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//                TabLayout.Tab tab = tabLayout.getTabAt(2);
//                tab.select();
                TabActivity.mViewPager.setCurrentItem(2);
                mTabHost.setCurrentTab(0);
                finish();

            }
        });

        TextView btn3= findViewById(R.id.goToFeedTextView);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("","Go To Feeds View");
                TabActivity.mViewPager.setCurrentItem(1);
                finish();
            }
        });
        TextView btn4= findViewById(R.id.goToexpertquiz);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("","Go To Expert Quiz");
                TabActivity.mViewPager.setCurrentItem(2);
                finish();
            }
        });

    }

    public void BoostmeInfo() {

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        loginModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));

        String dataString = gson.toJson(loginModel, LoginModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.BoostmeInfo.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("NonStop", "Boostme Info Response: " + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("boost_me_info");

                    JSONObject boostmeinfo = jsonobj.getJSONObject("data");

                    profilepic = findViewById(R.id.profilepic);
                    TextView student_name = findViewById(R.id.student_name);
                    TextView score = findViewById(R.id.score);
                    TextView tvrank = findViewById(R.id.tvrank);

                    if (boostmeinfo.getString("student_name") != null) {
                    student_name.setText(boostmeinfo.getString("student_name"));}

                    if (boostmeinfo.getString("score") != null) {
                    score.setText(boostmeinfo.getString("score") + " Points");}

                    if (boostmeinfo.getString("rank") != null) {
                    tvrank.setText(boostmeinfo.getString("rank"));}

                    if (boostmeinfo.getString("company_image") != null) {

                        Picasso.with(getApplicationContext()).cancelRequest(img_background);
                        Picasso.with(getApplicationContext())
                                .load(R2Values.Web.BASE_URL + boostmeinfo.getString("company_image"))
                                //*.placeholder(R.drawable.default_post_image) // optional
                                //  .error(R.drawable.default_post_image)         // optional*//*
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                        //Set it in the ImageView
                                        img_background.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                    }
                                });
                    }

                    if (boostmeinfo.getString("student_image") != null) {
                        Picasso.with(BoostMeActivity.this).cancelRequest(profilepic);
                        Picasso.with(BoostMeActivity.this)
                                .load(R2Values.Web.BASE_URL + boostmeinfo.getString("student_image"))
                                .placeholder(R.drawable.default_image) // optional
                                .error(R.drawable.default_image)         // optional
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                                        profilepic.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                    }
                                }); //imgprofilepic);
                    }

                    Log.d("Pooja Data",boostmeinfo.getString("rank"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toasty.warning(BoostMeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "BoostmeInfo");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
