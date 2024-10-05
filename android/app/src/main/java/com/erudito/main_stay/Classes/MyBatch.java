package com.erudito.main_stay.Classes;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.main_stay.R;
import com.erudito.main_stay.adapter.MyBatchAdapter;
import com.erudito.main_stay.models.LikeModel;
import com.erudito.main_stay.models.MyBatchModel;
import com.erudito.main_stay.utils.PreferenceHelper;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MyBatch extends AppCompatActivity {

    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    ProgressDialog pd;
    private GridView grid_view;
    private MyBatchAdapter mAdapter;
    private ArrayList<MyBatchModel> mybatchList = new ArrayList<>();
    private JSONArray jarray;
    private TextView batch_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_batch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPreferenceHelper = new PreferenceHelper(this);
        gson = new Gson();
        grid_view = findViewById(R.id.grid_view);
        batch_name = findViewById(R.id.batch_name);

        pd = new ProgressDialog(MyBatch.this, R.style.MyTheme);
        // pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        GetBatchUsers();

    }

    public void GetBatchUsers() {

        LikeModel likeModel = new LikeModel();
        likeModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        likeModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));

        String dataString = gson.toJson(likeModel, LikeModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetBatchStudents.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "get_batch_students Post response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_batch_students");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        batch_name.setText("Batch Title: " + jsonobj.getString("batch_name"));
                        jarray = jsonobj.getJSONArray("data");

                        mAdapter = new MyBatchAdapter(MyBatch.this, jarray);
                        grid_view.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
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
                Toasty.warning(MyBatch.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetBatchStudents");

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
