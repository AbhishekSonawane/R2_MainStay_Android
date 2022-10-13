package com.android.main_stay.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.android.main_stay.Classes.App;
import com.android.main_stay.Classes.R2Values;
import com.android.main_stay.R;
import com.android.main_stay.adapter.AssignmentsListAdapter;
import com.android.main_stay.models.*;
import com.android.main_stay.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Assignments#newInstance} factory method to
 * create an instance of this fragment.*/

public class Assignments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private JSONArray jarray;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private View view;
    private RecyclerView recyclerView;
    private AssignmentsListAdapter mAdapter;
    private ArrayList<AssignmentModel> AssignmentList = new ArrayList<>();
    public static Context mContext;
    LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog pd;

    public Assignments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Assignments.
     */
    // TODO: Rename and change types and number of parameters
    public static Assignments newInstance(String param1, String param2) {
        Assignments fragment = new Assignments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd = new ProgressDialog(getActivity(), R.style.MyTheme);
        //pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_assignments, container, false);
        mPreferenceHelper = new PreferenceHelper(getActivity());
        gson = new Gson();
        recyclerView = view.findViewById(R.id.assignments_list_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetStudentAssignmentsList();
            }
        });

        pd.show();
        GetStudentAssignmentsList();

        return view;

    }


    public void GetStudentAssignmentsList() {

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


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetStudentAssignmentsList.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("NonStop", "Assignments List Response: " + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_student_assign_list");
                    if (jsonobj.getString("status").equals("success")) {
                        jarray = jsonobj.getJSONArray("data");


                        if (jarray.length() > 0) {
                            Type type = new TypeToken<ArrayList<AssignmentModel>>() {
                            }.getType();
                            AssignmentList = new Gson().fromJson(jarray.toString(), type);
                            Log.d("NonStop", "In Assignment List Size: " + AssignmentList.size());
                        } else {
                            //  Toasty.error(getActivity(), getResources().getString(R.string.no_notification), Toast.LENGTH_SHORT).show();
                        }

                        mAdapter = new AssignmentsListAdapter(AssignmentList, getActivity());
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

        App.getInstance().addToRequestQueue(request, "GetStudentAssignmentsList");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    public void onResume() {

        GetStudentAssignmentsList();
        super.onResume();
    }

}
