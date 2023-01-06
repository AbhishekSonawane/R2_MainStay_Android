package com.erudito.main_stay.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.erudito.main_stay.Classes.R2Values;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.erudito.main_stay.BuildConfig;
import com.erudito.main_stay.Classes.App;
import com.android.main_stay.R;
import com.erudito.main_stay.adapter.FeedsAdapter;
import com.erudito.main_stay.models.FeedsModel;
import com.erudito.main_stay.models.LoginModel;
import com.erudito.main_stay.utils.PreferenceHelper;
import com.erudito.main_stay.utils.WebUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Feed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Feed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;
    private ArrayList<FeedsModel> feedsList2;
    private ArrayList<FeedsModel> feedsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedsAdapter mAdapter;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private SwipeRefreshLayout swipeContainer;
    private boolean noMoreData = false;
    private int currentPage = 0;
    private RelativeLayout progressLay;
    public static Boolean isOnLoad = false;
    private ProgressDialog pd;
    LinearLayoutManager mLayoutManager;
    public static SharedPreferences sharedPref;
    public static int index = -1;
    public static int top = -1;


    public Feed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feed.
     */
    // TODO: Rename and change types and number of parameters
    public static Feed newInstance(String param1, String param2) {
        Feed fragment = new Feed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPage = 1;

        pd = new ProgressDialog(getActivity(), R.style.MyTheme);
        //     pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);


        sharedPref = getContext().getApplicationContext().getSharedPreferences("r2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("homescreen_resume", false);
        editor.putBoolean("from_comments", false).apply();
        editor.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        init();

        return view;
    }

    @Override
    public void onPause() {

        super.onPause();

        boolean toComments;
        toComments = sharedPref.getBoolean("to_comments", false);

        Log.d("NonStop", "In Homescreen toComments: " + toComments );

        if (toComments) {

            index = this.mLayoutManager.findFirstVisibleItemPosition();
            View v = this.recyclerView.getChildAt(0);
            top = (v == null) ? 0 : (v.getTop() - this.recyclerView.getPaddingTop());
            Log.d("NonStop", "In Feeds onPause-> Index: " + index + " --- Top: " + top);

            sharedPref.edit().putBoolean("to_comments", false).apply();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onResume() {

        super.onResume();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sharedPref = getContext().getSharedPreferences("r2", Context.MODE_PRIVATE);
        boolean fromComments = sharedPref.getBoolean("from_comments", false);
        boolean homeResume = sharedPref.getBoolean("homescreen_resume", false);
        boolean fromNotifs = sharedPref.getBoolean("from_notifications", false);

        Log.d("NonStop", "onResume fromComments: " + fromComments);

        int commentCount = sharedPref.getInt("comment_count", 0);
        int likecount = sharedPref.getInt("likecount", 0);
        int holderPosition = sharedPref.getInt("holder_position", 0);
        String like_clicked = sharedPref.getString("like_clicked", "2");
        String post_bookmarked =sharedPref.getString("post_bookmarked","4");

        if (feedsList2 != null) {
            feedsList = new ArrayList<FeedsModel>(feedsList2);
            Log.d("NonStop", "feedsList is not NULL");
        }

        Log.d("NonStop", "In Homescreen eventinfo Size onResume: " + feedsList.size());
        if (fromComments) {
            Log.d("NonStop", "Index Val: " + index + " --- Top Val: " + top);
            if (index != -1) {
                Log.d("NonStop", "Child position: " + holderPosition + "--- Comment Count: " + commentCount);
                FeedsAdapter.MyViewHolder v = (FeedsAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(holderPosition);
                if (v != null) {
                    v.txtcommentscount.setText(String.valueOf(commentCount));
                    v.txtlike_count.setText(String.valueOf(likecount));

                    if (post_bookmarked.contentEquals("1")) {
                        FeedsAdapter.MyViewHolder.imgbookmark.setImageDrawable(getActivity().getDrawable(R.drawable.btn_bookmark_unfilled));
                    } else {
                        FeedsAdapter.MyViewHolder.imgbookmark.setImageDrawable(getActivity().getDrawable(R.drawable.btn_bookmark_filled));
                    }

                    if (like_clicked.contentEquals("1")) {
                        FeedsAdapter.MyViewHolder.imglike.setImageDrawable(getActivity().getDrawable(R.drawable.btn_like_filled));
                    } else {
                        FeedsAdapter.MyViewHolder.imglike.setImageDrawable(getActivity().getDrawable(R.drawable.btn_like_unfilled));
                    }


                } else {
                    Log.d("NonStop", "View was null");
                }

                feedsList.get(holderPosition).setComment_count(String.valueOf(commentCount));
                feedsList.get(holderPosition).setLike_count(String.valueOf(likecount));
                feedsList.get(holderPosition).setIs_liked(like_clicked);
                feedsList.get(holderPosition).setIs_bookmarked(post_bookmarked);

                mAdapter = new FeedsAdapter(feedsList, getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mPreferenceHelper.addBoolean("adapterFixed", true);
                mLayoutManager.scrollToPositionWithOffset(index, top);

                sharedPref.edit().putBoolean("homescreen_resume", true).apply();
                sharedPref.edit().putBoolean("from_comments", false).apply();
            }
        }

        else {
            init();
            Log.d("NonStop", "In Homescreen HomeResume is true");
        }
    }

    private void init() {
        recyclerView = view.findViewById(R.id.feed_recycler_view);
        gson = new Gson();
        mPreferenceHelper = new PreferenceHelper(getActivity());
        progressLay = view.findViewById(R.id.progressLay);

        swipeContainer = view.findViewById(R.id.swipeContainer);

        mLayoutManager = new LinearLayoutManager(getActivity());

        SetAppVersion(BuildConfig.VERSION_NAME);

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
                        getPaginatedPosts(currentPage);
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
                GetPosts();
            }
        });

        pd.show();
        GetPosts();

    }

    public void GetPosts() {

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetPosts.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_posts");

                    if (jsonobj.getString("status").equals("fail")) {
                        Toasty.error(getActivity(), jsonobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        JSONArray jarray = jsonobj.getJSONArray("data");

                        if (jarray.length() > 0) {
                            Type type = new TypeToken<ArrayList<FeedsModel>>() {
                            }.getType();
                            feedsList = new Gson().fromJson(jarray.toString(), type);
                            Log.d("NonStop", "In Homescreen getPosts Size: " + feedsList.size());
                        } else {
                            Toasty.error(getActivity(), getResources().getString(R.string.no_posts), Toast.LENGTH_SHORT).show();
                        }

                        mAdapter = new FeedsAdapter(feedsList, getActivity());
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
                Toasty.warning(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetPosts");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    private void getPaginatedPosts(final int currentPage) {

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
                    String txtSource = R2Values.Web.GetPosts.SERVICE_URL;

                    JSONObject params = new JSONObject();
                    params.put("email", mPreferenceHelper.getString(R2Values.Commons.EMAIL));
                    params.put("password", mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
                    params.put("page_no", "" + currentPage);

                    String result = WebUtil.callInBackground(getActivity(), txtSource,
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
                        JSONObject jsonobj = responseObj.getJSONObject("get_posts");

                        if (responseObj.has("get_posts")) {
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

    public void SetAppVersion(final String currentVersion) {

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        loginModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        loginModel.setApp_version(currentVersion);
        loginModel.setOs_version(Build.VERSION.RELEASE);

        String dataString = gson.toJson(loginModel, LoginModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.SetVersion.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                try {

                    JSONObject jsonobj = response.getJSONObject("set_version");

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
                Toasty.warning(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "SetAppVersion");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

    }

}
