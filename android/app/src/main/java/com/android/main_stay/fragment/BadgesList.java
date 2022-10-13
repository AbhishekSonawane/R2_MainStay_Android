package com.android.main_stay.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.android.main_stay.R;
import com.android.main_stay.adapter.BadgesListAdapter;
import com.android.main_stay.models.BadgeModel;
import com.android.main_stay.utils.PreferenceHelper;

import org.json.JSONArray;

import java.util.ArrayList;


public class BadgesList extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private JSONArray jarray;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private RecyclerView recyclerView;
    private BadgesListAdapter mAdapter;
    public static Context mContext;
    LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog pd;
    private Object String;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BadgesList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pd = new ProgressDialog(getActivity(), R.style.MyTheme);
        //pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mPreferenceHelper = new PreferenceHelper(getActivity());
        gson = new Gson();
        recyclerView = view.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        swipeContainer = view.findViewById(R.id.swipeContainer);

        GetStudentBadgeList();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //GetStudentBadgesList();
                GetStudentBadgeList();
            }
        });

        pd.show();
     //   GetStudentBadgesList();

        Log.d("","$$ in Badges List $$");

        return view;
    }


   /* public void GetStudentBadgesList() {

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,R2Values.Web.GetBadgeList.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("NonStop", "Badges List Response: " + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_badge_list");
                    if (jsonobj.getString("status").equals("success")) {
                        jarray = jsonobj.getJSONArray("badge_list");

                            Type type = new TypeToken<ArrayList<BadgeModel>>() {
                            }.getType();
                            BadgesList = new Gson().fromJson(jarray.toString(), type);
                            Log.d("NonStop", "In Badge List Size: " + BadgesList.size());

                        mAdapter = new BadgesListAdapter(BadgesList,getActivity());
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

        App.getInstance().addToRequestQueue(request, "GetStudentBadgesList");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }*/


    public void GetStudentBadgeList(){

        ArrayList<BadgeModel> list= new ArrayList<BadgeModel>();

        BadgeModel badgeModel = new BadgeModel();
        badgeModel.setName("master mind");
        badgeModel.setDesc("completed 1 quiz");
        badgeModel.setImage(R.drawable.trophy_1);
        list.add(badgeModel);


        BadgeModel badgeModel1 = new BadgeModel();
        badgeModel1.setName("Quick Learner");
        badgeModel1.setDesc("Got first leaderboard");
        badgeModel1.setImage(R.drawable.trophy_2);
        list.add(badgeModel1);

        BadgeModel badgeModel2 = new BadgeModel();
        badgeModel2.setName("Super Learner");
        badgeModel2.setDesc("completed more than five quizes");
        badgeModel2.setImage(R.drawable.trophy_3);
        list.add(badgeModel2);

        BadgeModel badgeModel3 = new BadgeModel();
        badgeModel3.setName("The Achievwer");
        badgeModel3.setDesc("Ranked number 1");
        badgeModel3.setImage(R.drawable.trophy_4);
        list.add(badgeModel3);

        BadgeModel badgeModel4 =  new BadgeModel();
        badgeModel4.setName("Locked batch");
        badgeModel4.setDesc("unlock to see the details");
        badgeModel4.setImage(R.drawable.trophy_locked);
        list.add(badgeModel4);





        mAdapter = new BadgesListAdapter(list,getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        swipeContainer.setRefreshing(false);

    }

}

