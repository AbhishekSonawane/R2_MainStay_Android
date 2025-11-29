package com.erudito.mainstaypeople.Classes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.android.main_stay.R;
import com.erudito.mainstaypeople.models.LoginModel;
import com.erudito.mainstaypeople.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

import static com.erudito.mainstaypeople.utils.Utils.getYoutubeVideoId;

public class ViewAssignment extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private TextView txtassignmenttitle, txtquestion;
    private ImageView post_image,imgclose;
    private YouTubePlayerView youTubeView;
    String videoId;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private TextView txtSend;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

        gson = new Gson();
        mPreferenceHelper = new PreferenceHelper(this);
        txtassignmenttitle = findViewById(R.id.txtassignmenttitle);
        txtquestion = findViewById(R.id.txtquestion);
        youTubeView = findViewById(R.id.youtube_view);
        post_image = findViewById(R.id.post_image);
        txtSend = findViewById(R.id.txtSend);
        imgclose = findViewById(R.id.imgclose);

        txtSend.setMovementMethod(new ScrollingMovementMethod());

        pd = new ProgressDialog(ViewAssignment.this, R.style.MyTheme);
        //    pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewAssignmentAnswer();
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = "There was an error initializing the YouTubePlayer";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically

            Log.d("nonstop", "videoid in webviewonInitializationSuccess" + videoId);
            player.cueVideo(videoId);

            // Hiding player controls
            //   player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize("AIzaSyASo9rv7zaO2cRpz0l37rQIQeAzrBloj8w", this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }


    public void ViewAssignmentAnswer() {


        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        loginModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        loginModel.setAss_id(getIntent().getStringExtra("AssignmentID"));

        String dataString = gson.toJson(loginModel, LoginModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.ViewAssignmentAnswer.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                try {

                    JSONObject jsonobj = response.getJSONObject("view_assignment_ans");
                    if (jsonobj.getString("status").equals("success")) {
                        JSONArray jarray = jsonobj.getJSONArray("data");

                        if (jarray.getJSONObject(0).getString("title") != null)
                            txtassignmenttitle.setText(jarray.getJSONObject(0).getString("title"));

                        if (jarray.getJSONObject(0).getString("text") != null)
                            txtquestion.setText(Html.fromHtml(jarray.getJSONObject(0).getString("text")), TextView.BufferType.SPANNABLE);

                        if (jarray.getJSONObject(0).getString("has_video").equals("True")) {

                            youTubeView.setVisibility(View.VISIBLE);
                            youTubeView.initialize("AIzaSyASo9rv7zaO2cRpz0l37rQIQeAzrBloj8w", ViewAssignment.this);
                            post_image.setVisibility(View.GONE);

                            videoId = getYoutubeVideoId(jarray.getJSONObject(0).getString("video_url"));

                            Log.d("nonstop", "videoid in take assignment" + videoId);

                        } else if (jarray.getJSONObject(0).getString("has_img").equals("True")) {

                            Picasso.with(ViewAssignment.this).cancelRequest(post_image);
                            Picasso.with(ViewAssignment.this)
                                    .load(R2Values.Web.BASE_URL + jarray.getJSONObject(0).getString("img_url"))
                                    .placeholder(R.drawable.default_post_image) // optional
                                    .error(R.drawable.default_post_image)         // optional
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                            //Set it in the ImageView
                                            post_image.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                        }
                                    });
                        } else {
                            post_image.setVisibility(View.GONE);
                        }

                        if (jarray.getJSONObject(0).getString("student_answer") != null)
                            txtSend.setText(jarray.getJSONObject(0).getString("student_answer"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {


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
                Toasty.warning(ViewAssignment.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "view_assignment_ans");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
