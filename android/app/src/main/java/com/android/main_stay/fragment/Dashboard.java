package com.android.main_stay.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
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
import com.android.main_stay.BuildConfig;
import com.android.main_stay.Classes.App;
import com.android.main_stay.Classes.Application;
import com.android.main_stay.Classes.BoostMeGifActivity;
import com.android.main_stay.Classes.R2Values;
import com.android.main_stay.Classes.TabActivity;
import com.android.main_stay.Classes.Leaderboard;
import com.android.main_stay.R;
import com.android.main_stay.models.LoginModel;
import com.android.main_stay.utils.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import es.dmoral.toasty.Toasty;
import me.pushy.sdk.Pushy;

import static com.android.main_stay.Classes.MainFragment.mTabHost;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Dashboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private WebView graphwebview;
    PreferenceHelper mPreferenceHelper;
    Gson gson;

    boolean versionCheck=false;

    TextView quizCount,assignmentCount,applicationCount,rankText;
    RelativeLayout relativeLayout_quiz,relativeLayout_assignment,relativeLayout_rank,relativeLayout_application;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Achievements.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Pushy.listen(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_achievements, container, false);

        TextView btn= view.findViewById(R.id.boostMeTV);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //DO you work here
                Log.d("R", "Boost me!!");
                Intent intent = new Intent(getActivity(), BoostMeGifActivity.class);
//                Intent intent = new Intent(getActivity(), BoostMeGifFullscreenActivity.class);
                startActivity(intent);
            }
        });

        graphwebview = view.findViewById(R.id.graphwebview);
        mPreferenceHelper = new PreferenceHelper(getActivity());
        WebSettings ws = graphwebview.getSettings();

        gson = new Gson();
        //To fit the webpage based on the device screen size
        graphwebview.getSettings().setLoadWithOverviewMode(true);
        graphwebview.getSettings().setUseWideViewPort(true);

        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            try {
                Log.d("Pooja", "Enabling HTML5-Features");
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", Boolean.TYPE);
                m1.invoke(ws, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", Boolean.TYPE);
                m2.invoke(ws, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", String.class);
                m3.invoke(ws, "/data/data/" + getActivity().getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", Long.TYPE);
                m4.invoke(ws, 1024 * 1024 * 8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", String.class);
                m5.invoke(ws, "/data/data/" + getActivity().getPackageName() + "/cache/");

                Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", Boolean.TYPE);
                m6.invoke(ws, Boolean.TRUE);

                Log.d("Pooja", "Enabled HTML5-Features");
            } catch (NoSuchMethodException e) {
                Log.e("Pooja", "Reflection fail", e);
            } catch (InvocationTargetException e) {
                Log.e("Pooja", "Reflection fail", e);
            } catch (IllegalAccessException e) {
                Log.e("Pooja", "Reflection fail", e);
            }
        }

        graphwebview.loadUrl(R2Values.Web.BASE_URL + "ranking_graph/" + mPreferenceHelper.getString(R2Values.Commons.STUDENT_ID) + "/");

        GetDashboardCounts();

        applicationCount = view.findViewById(R.id.pending_application_text);
        assignmentCount = view.findViewById(R.id.pending_assignment_text);
        rankText = view.findViewById(R.id.rank_text);
        quizCount = view.findViewById(R.id.pending_quiz_text);
        relativeLayout_quiz = view.findViewById(R.id.relativeLayout_quiz);
        relativeLayout_application = view.findViewById(R.id.relativeLayout_application);
        relativeLayout_assignment = view.findViewById(R.id.relativeLayout_assignment);
        relativeLayout_rank = view.findViewById(R.id.relativeLayout_rank);

        relativeLayout_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.mViewPager.setCurrentItem(2);
                mTabHost.setCurrentTab(0);
            }
        });

        relativeLayout_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity.mViewPager.setCurrentItem(2);
                mTabHost.setCurrentTab(1);
            }
        });

        relativeLayout_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Application.class);
                startActivity(intent);
            }
        });

        relativeLayout_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Leaderboard.class);
                startActivity(intent);
            }
        });

        return view;
    }


    public void GetDashboardCounts() {

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetDashboradCount.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop GetDashboardCounts", "response  ---------------" + response);

                try {

                    JSONObject jsonobj = response.getJSONObject("get_dashboard_count");
                    quizCount.setText(Html.fromHtml("Quizzes" + "<br>" + "<b>" + jsonobj.getString("pending_quiz") + "</b>" + " Pending<br>" + "<font color=#00BFFF>" + "<small><u>" + getString(R.string.takequizlabel) + "</u></small>" + "</font>"));
                    applicationCount.setText(Html.fromHtml("Applications<br>" + "<b>" + jsonobj.getString("pending_application") + "</b>" + " Pending<br>" + "<font color=#00BFFF>" + "<small><u>" + getString(R.string.applicationslabel) + "</u></small>" + "</font>"));
                    assignmentCount.setText(Html.fromHtml("Assignments<br>" + "<b>" + jsonobj.getString("pending_assignments") + "</b>" + " Pending<br>" + "<font color=#00BFFF>" + "<small><u>" + getString(R.string.assignmentslabel) + "</u></small>" + "</font>"));
                    rankText.setText(Html.fromHtml("My Rank<br>" + "<b>" + jsonobj.getString("rank") + "<br>" + "<font color=#00BFFF>" + "<small><u>" + getString(R.string.ranklabel) + "</u></small>" + "</font>"));

                    String latestVersion = jsonobj.getString("current_version");

                    if (!BuildConfig.VERSION_NAME.equals(latestVersion)&&!versionCheck) {
                        versionCheck=true;
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("New Version Available!");
                        builder.setMessage("Hey, we have released a new version of the R2 app. It contains exciting new features (and we have squashed few bugs too). Would you like to try the new app ? It wont take more than 2 mins");
          /*  builder.setMessage("Version " + latestVersion + " is available " +
                    "on Play Store, please update!");*/
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Click Update button action
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.r2.nexo&hl=en")));
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Later button action
                                dialog.dismiss();
                            }
                        });

                        builder.setCancelable(false);
                        builder.show();
                    }
                   /* assignmentCount.setText("Assignments\n" + jsonobj.getString("pending_assignments") + " Pending");
                    rankText.setText("My Rank\n" + jsonobj.getString("rank"));*/
                    // quizCount.setText("Quizzes\n" + jsonobj.getString("pending_quiz") + " Pending");

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

    public void onResume() {
        super.onResume();

        graphwebview.loadUrl(R2Values.Web.BASE_URL + "ranking_graph/" + mPreferenceHelper.getString(R2Values.Commons.STUDENT_ID) + "/");

        GetDashboardCounts();
    }
}
