package com.erudito.mainstaypeople.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.main_stay.R;
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
import com.erudito.mainstaypeople.Classes.App;
import com.erudito.mainstaypeople.Classes.BookmarksList;
import com.erudito.mainstaypeople.Classes.EditProfile;
import com.erudito.mainstaypeople.Classes.Leaderboard;
import com.erudito.mainstaypeople.Classes.Login;
import com.erudito.mainstaypeople.Classes.MyBatch;
import com.erudito.mainstaypeople.Classes.R2Values;
import com.erudito.mainstaypeople.models.LoginModel;
import com.erudito.mainstaypeople.models.SendFeedbackModel;
import com.erudito.mainstaypeople.utils.PreferenceHelper;
import com.erudito.mainstaypeople.utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.BuildConfig;
import es.dmoral.toasty.Toasty;


public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private View view;
    private LinearLayout linlayedtpersonaldetails, linlaybookmarkslist, linlaymybatch, linlaylogout,linlayabout,linlayresults;
    private TextView student_name, txtbookmarkcount;
    private ImageView img_background, uploadprofilepic;
    private ProgressDialog pd;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pd = new ProgressDialog(getActivity(), R.style.MyTheme);
        //  pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        gson = new Gson();
        mPreferenceHelper = new PreferenceHelper(getActivity());
        linlayedtpersonaldetails = view.findViewById(R.id.linlayedtpersonaldetails);
        student_name = view.findViewById(R.id.student_name);
        img_background = view.findViewById(R.id.img_background);
        uploadprofilepic = view.findViewById(R.id.uploadprofilepic);
        linlaybookmarkslist = view.findViewById(R.id.linlaybookmarkslist);
        linlaymybatch = view.findViewById(R.id.linlaymybatch);
        txtbookmarkcount = view.findViewById(R.id.txtbookmarkcount);
        linlaylogout = view.findViewById(R.id.linlaylogout);
        linlayabout = view.findViewById(R.id.linlayabout);
        linlayresults= view.findViewById(R.id.linlayresults);
        pd.show();
        GetProfileInfo();


        linlayresults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Leaderboard.class);
                startActivity(intent);
            }
        });


        linlayedtpersonaldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        linlaybookmarkslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookmarksList.class);
                startActivity(intent);
            }
        });

        linlaymybatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyBatch.class);
                startActivity(intent);
            }
        });

        linlaylogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        linlayabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDialog(getActivity(),"App Version : "+ BuildConfig.VERSION_NAME);
            }
        });
        return view;
    }

    public void GetProfileInfo() {

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


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetProfileInfo.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Pooja", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("get_profile_info");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        JSONObject dataobj = jsonobj.getJSONObject("data");

                        Log.d("Pooja profile data", dataobj.toString());

                        if (dataobj.getString("student_first_name") != null)
                            student_name.setText(dataobj.getString("student_first_name") + " " + dataobj.getString("student_last_name"));

                        if (dataobj.getString("student_picture_url") != null) {

                          //  Picasso.with(getContext()).cancelRequest(img_background);
                            Picasso.with(getContext())
                                    .load(R2Values.Web.BASE_URL + dataobj.getString("company_image"))
                                    .placeholder(R.drawable.default_image) // optional
                                    .error(R.drawable.default_image)         // optional
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

                        if (dataobj.getString("bookmark_count") != "0") {
                            txtbookmarkcount.setVisibility(View.VISIBLE);
                            txtbookmarkcount.setText(dataobj.getString("bookmark_count"));
                        } else {
                            txtbookmarkcount.setVisibility(View.GONE);
                        }

                        if (dataobj.getString("student_picture_url") != null) {

                            Picasso.with(getActivity()).cancelRequest(uploadprofilepic);
                            Picasso.with(getActivity())
                                    .load(R2Values.Web.BASE_URL + dataobj.getString("student_picture_url"))
                                    //*.placeholder(R.drawable.default_image) // optional
                                    //  .error(R.drawable.default_image)         // optional*//*
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                            //Set it in the ImageView
                                            uploadprofilepic.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                        }
                                    });
                        }
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

        App.getInstance().addToRequestQueue(request, "GetProfileInfo");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    public void SendFeedback() {

        SendFeedbackModel sendFeedbackModel = new SendFeedbackModel();
        sendFeedbackModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        sendFeedbackModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        sendFeedbackModel.setText("Test Feedback");

        String dataString = gson.toJson(sendFeedbackModel, SendFeedbackModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.AddFeedback.SERVICE_URL,jsonObject ,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);
                try {

                    JSONObject jsonobj = response.getJSONObject("add_feedback");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        App.getInstance().addToRequestQueue(request, "GetProfileInfo");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    @Override
    public void onResume() {
        Log.d("Nonstop", "In Profile onResume");
        pd.show();
        GetProfileInfo();
        super.onResume();
    }

    public void Logout() {

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


        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,R2Values.Web.logout.SERVICE_URL ,jsonObject, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Pooja", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {
                    JSONObject jsonobj = response.getJSONObject("logout");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {
                        mPreferenceHelper.clear();
                        getActivity().getSharedPreferences("MainStay", 0).edit().clear().apply();
                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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

        App.getInstance().addToRequestQueue(request, "GetProfileInfo");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

}
