package com.erudito.mainstaypeople.Classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.reflect.TypeToken;
import com.android.main_stay.R;
import com.erudito.mainstaypeople.adapter.CommentsAdapter;
import com.erudito.mainstaypeople.models.CommentsModel;
import com.erudito.mainstaypeople.models.LikeModel;
import com.erudito.mainstaypeople.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Comments extends AppCompatActivity {

    private View view;
    private ArrayList<CommentsModel> commentsList;
    public static RecyclerView recyclerView;
    private CommentsAdapter mAdapter;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private ImageView btncomment;
    private EditText txtComment;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog pd;
    Boolean scrollflag;
    public int commentCount, likecount,holderPosition;
    public String is_liked, post_bookmarked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("r2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("to_comments", false);
        editor.commit();


        commentCount = getIntent().getIntExtra("comment_count", 0);
        likecount = getIntent().getIntExtra("likecount", 0);
        holderPosition = getIntent().getIntExtra("holder_position", 0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("r2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
               // mPreferenceHelper.addBoolean("from_comments", true);
                editor.putBoolean("from_comments", true);
                editor.putInt("holder_position", holderPosition);
                editor.putInt("comment_count", commentCount);
                editor.putInt("likecount",likecount);
             //   editor.putInt("likecount", Integer.parseInt(CommentsAdapter.commentsList.get(holderPosition).getLike_count()));
                editor.putString("like_clicked", is_liked);
                editor.putString("post_bookmarked",post_bookmarked);

                Log.d("NonStop", "fromComments in comments: " + sharedPref.getBoolean("from_comments", false));

                editor.commit();
                finish();
            }
        });

        gson = new Gson();
        mPreferenceHelper = new PreferenceHelper(Comments.this);
        commentsList = new ArrayList<>();
        btncomment = findViewById(R.id.btncomment);
        txtComment = findViewById(R.id.txtComment);
        scrollflag = false;

        pd = new ProgressDialog(Comments.this, R.style.MyTheme);
        //    pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        GetComments();

        recyclerView = findViewById(R.id.comment_recycler_view);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pd.show();
                GetComments();
            }
        });

        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("NS", "textcomment value" + txtComment.getText().toString());

                if ((txtComment.getText().toString().contentEquals("")) || (txtComment.getText().toString() == null) || txtComment.getText().toString().isEmpty()) {
                    Toasty.error(Comments.this, getResources().getString(R.string.comment_text_empty_msg), Toast.LENGTH_LONG).show();
                } else if (txtComment.getText().toString().trim().length() != 0) {
                    pd.show();
                    AddComment();
                    btncomment.setVisibility(View.INVISIBLE);
                    txtComment.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtComment.getWindowToken(), 0);
                }
            }
        });

        txtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0)
                    btncomment.setVisibility(View.VISIBLE);
                else
                    btncomment.setVisibility(View.INVISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    btncomment.setVisibility(View.VISIBLE);
                else
                    btncomment.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void GetComments() {

        LikeModel likeModel = new LikeModel();
        likeModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        likeModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        likeModel.setPost_id(getIntent().getStringExtra("post_id"));

        String dataString = gson.toJson(likeModel, LikeModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetComments.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_comments");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        JSONArray jarray = jsonobj.getJSONArray("comments");

                        commentCount = Integer.parseInt(jarray.getJSONObject(0).getString("comment_count"));

                        likecount = Integer.parseInt(jarray.getJSONObject(0).getString("like_count"));
                        post_bookmarked = jarray.getJSONObject(0).getString("is_bookmarked");
                        is_liked = jarray.getJSONObject(0).getString("is_liked");

                        if (jarray.length() > 0) {
                            Type type = new TypeToken<ArrayList<CommentsModel>>() {
                            }.getType();
                            commentsList = new Gson().fromJson(jarray.toString(), type);

                            Log.d("Pooja Data", "commentsList" + commentsList.size());
                        }

                        mAdapter = new CommentsAdapter(commentsList, Comments.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Comments.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);

                        if (scrollflag == true) {
                            scrollflag = false;
                            recyclerView.scrollToPosition(jarray.length() - 1);
                        }

                        swipeContainer.setRefreshing(false);

                    }

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
                Toasty.warning(Comments.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetComments");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }


    public void AddComment() {

        LikeModel likeModel = new LikeModel();
        likeModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        likeModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        likeModel.setPost_id(getIntent().getStringExtra("post_id"));

        likeModel.setComment_text(txtComment.getText().toString());

        String dataString = gson.toJson(likeModel, LikeModel.class);


        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.AddComment.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("add_comment");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {
                        txtComment.setText("");
                        scrollflag = true;
                        GetComments();
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
                Toasty.warning(Comments.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetComments");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("Nonstop", "holder position in comments" + holderPosition);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("r2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("from_comments", true);
        editor.putInt("holder_position", holderPosition);
        editor.putInt("comment_count", commentCount);
        editor.putInt("likecount", likecount);
     //   editor.putInt("likecount", Integer.parseInt(CommentsAdapter.commentsList.get(holderPosition).getLike_count()));
        editor.putString("like_clicked", is_liked);
        editor.putString("post_bookmarked",post_bookmarked);
        editor.commit();
        finish();
    }
}
