package com.android.main_stay.Classes;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.android.main_stay.adapter.BookmarksListAdapter;
import com.android.main_stay.models.FeedsModel;
import com.android.main_stay.models.LoginModel;
import com.android.main_stay.utils.PreferenceHelper;
import com.android.main_stay.utils.WebUtil;
import com.android.main_stay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class BookmarksList extends AppCompatActivity {

    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    ProgressDialog pd;
    private boolean noMoreData = false;
    private int currentPage = 0;
    private RelativeLayout progressLay, mainrelay;
    LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private BookmarksListAdapter mAdapter;
    private ArrayList<FeedsModel> feedsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.bookmark_recycler_view);
        gson = new Gson();
        mPreferenceHelper = new PreferenceHelper(BookmarksList.this);
        progressLay = findViewById(R.id.progressLay);

        swipeContainer = findViewById(R.id.swipeContainer);

        mLayoutManager = new LinearLayoutManager(BookmarksList.this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                Log.d("Nonstop", "OnScrolled");

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    Log.d("Nonstop", "OnScrolledInpagination1. NoMoreData: " + noMoreData);
                    if (!noMoreData) {
                        Log.d("Nonstop", "OnScrolledInpagination2");
                        currentPage++;
                        Log.d("pageno", "Page No.: " + currentPage);
                        getPaginatedBookmarks(currentPage);
                    }
                }
            }

        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("NonStop", "Swiped to REFRESH!!!!");
                currentPage = 1;
                noMoreData = false;
                GetBookmarksList();
            }
        });

        currentPage = 1;

        pd = new ProgressDialog(BookmarksList.this, R.style.MyTheme);
        //pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        GetBookmarksList();

    }

    public void GetBookmarksList() {

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        loginModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        loginModel.setPage_no("1");

        String dataString = gson.toJson(loginModel, LoginModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetBookmark.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "Bookmarks Listresponse  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_bookmarks");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        JSONArray jarray = jsonobj.getJSONArray("data");

                        if (jarray.length() > 0) {
                            Type type = new TypeToken<ArrayList<FeedsModel>>() {
                            }.getType();
                            feedsList = new Gson().fromJson(jarray.toString(), type);

                        } else {
                            Toasty.error(BookmarksList.this, getResources().getString(R.string.no_bookmarks), Toast.LENGTH_SHORT).show();
                        }

                        mAdapter = new BookmarksListAdapter(feedsList, BookmarksList.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

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
                Toasty.warning(BookmarksList.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetBookmarksList");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    private void getPaginatedBookmarks(final int currentPage) {

        new AsyncTask<Object, Object, String>() {

            @Override
            protected void onPreExecute() {
                progressLay.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Object... objects) {
          /*      if (isNetworkAvailable(getActivity())) {*/
                try {
                    String txtSource = R2Values.Web.GetBookmark.SERVICE_URL;

                    JSONObject params = new JSONObject();
                    params.put("email", mPreferenceHelper.getString(R2Values.Commons.EMAIL));
                    params.put("password", mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
                    params.put("page_no", "" + currentPage);

                    String result = WebUtil.callInBackground(BookmarksList.this, txtSource,
                            params.toString());

                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
                /*} else {
                    return null;
                }*/
            }

            @Override
            protected void onPostExecute(String output) {
                super.onPostExecute(output);
                progressLay.setVisibility(View.GONE);
                if (output != null) {
                    JSONObject responseObj = null;
                    try {
                        Log.d("pagination", "output---------------" + output);
                        responseObj = new JSONObject(output);
                        Log.d("NonStop", "response: " + responseObj);
                        JSONObject jsonobj = responseObj.getJSONObject("get_bookmarks");

                        if (responseObj.has("get_bookmarks")) {
                            JSONArray msgObj = jsonobj.getJSONArray("data");
                            if (msgObj.length() > 0) {
                                setList(msgObj);
                            } else {
                                Log.d("NonStop", "No more data for pagination!");
                                noMoreData = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute((Object[]) null);
    }

    public void setList(JSONArray msgJsonArray) {
        ArrayList<FeedsModel> postInfos = new ArrayList<>();

        if (msgJsonArray.length() > 0) {
            Type type = new TypeToken<ArrayList<FeedsModel>>() {
            }.getType();
            postInfos = new Gson().fromJson(msgJsonArray.toString(), type);
        }

        for (int postid = 0; postid < postInfos.size(); postid++) {
            feedsList.add(postInfos.get(postid));
        }
        Log.d("NonStop", "In Homescreen setList eventinfo Size: " + feedsList.size());
        mAdapter.notifyDataSetChanged();
    }

}
