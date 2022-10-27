package com.android.main_stay.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.android.main_stay.R;
import com.android.main_stay.adapter.ApplicationAdapter;
import com.android.main_stay.models.ApplicationModel;
import com.android.main_stay.models.LoginModel;
import com.android.main_stay.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Application extends AppCompatActivity {


    private JSONArray jarray;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private RecyclerView recyclerView;
    private ApplicationAdapter mAdapter;
    private ArrayList<ApplicationModel> ApplicationList = new ArrayList<>();
    public static Context mContext;
    LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPreferenceHelper = new PreferenceHelper(Application.this);
        gson = new Gson();
        recyclerView = findViewById(R.id.applications_list_recycler_view);
        mLayoutManager = new LinearLayoutManager(Application.this);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetStudentApplicationsList();
            }
        });

        pd = new ProgressDialog(Application.this, R.style.MyTheme);
        //pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        GetStudentApplicationsList();

    }

    public void GetStudentApplicationsList() {

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetApplicationList.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("NonStop", "Applications List Response: " + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_application_list");
                    if (jsonobj.getString("status").equals("success")) {
                        jarray = jsonobj.getJSONArray("data");

                        if (jarray.length() > 0) {
                            Type type = new TypeToken<ArrayList<ApplicationModel>>() {
                            }.getType();
                            ApplicationList = new Gson().fromJson(jarray.toString(), type);
                            Log.d("NonStop", "In Assignment List Size: " + ApplicationList.size());
                        } else {
                            //  Toasty.error(getActivity(), getResources().getString(R.string.no_notification), Toast.LENGTH_SHORT).show();
                        }

                        mAdapter = new ApplicationAdapter(ApplicationList, Application.this);
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
                Toasty.warning(Application.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetStudentApplicationList");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    public void onResume() {

        GetStudentApplicationsList();

        super.onResume();
    }


}
