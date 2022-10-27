package com.android.main_stay.Classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.android.main_stay.R;
import com.android.main_stay.models.ApplicationAnswers;
import com.android.main_stay.utils.InputValidation;
import com.android.main_stay.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class TakeApplication extends AppCompatActivity {

    TextView toolbar_title;
    String topicID,topicDE;
    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    private ProgressDialog pd;
    private EditText topicTitle,topicDesc, instWhere, effect, well, notexpected, reflection, different;
    private TextView topicDate,totalSore;
    private DatePickerDialog dateofbirthDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Boolean isFromAnswer;
    public static Button SubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_application);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferenceHelper = new PreferenceHelper(this);
        gson = new Gson();

        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar_title = findViewById(R.id.toolbar_title);

        pd = new ProgressDialog(TakeApplication.this, R.style.MyTheme);
        //pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//        pd.show();
        isFromAnswer = getIntent().hasExtra("fromview_Answers");


        toolbar_title.setText(getIntent().getStringExtra("ApplicationTopicName"));
        topicID = getIntent().getStringExtra("ApplicationID");
        Log.d("TopicID: ", "TopicID:" + topicID);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        topicDate = findViewById(R.id.datetexTextView);
        instWhere = findViewById(R.id.wheretexEditText);
        effect = findViewById(R.id.effecttexEditText);
        well = findViewById(R.id.welltexEditText);
        notexpected = findViewById(R.id.notExpectedEditText);
        reflection = findViewById(R.id.reflectionEditText);
        different = findViewById(R.id.dodifferentEditText);
        topicTitle = findViewById(R.id.topictexEditText);
        topicDesc = findViewById(R.id.topicdescEditText);

        topicTitle.setText(getIntent().getStringExtra("ApplicationTopicName"));
        topicDesc.setText(getIntent().getStringExtra("ApplicationTopicDescription"));
       // topicTitle.setBackgroundColor(Color.LTGRAY);

        topicTitle.setEnabled(false);
//        findViewById(R.id.topictexEditText)
//                .setBackgroundColor(Color.LTGRAY);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        Calendar newCalendar = Calendar.getInstance();

        dateofbirthDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                topicDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        topicDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //    showfromdate();
                dateofbirthDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dateofbirthDatePickerDialog.show();
            }
        });

        topicDate.setInputType(InputType.TYPE_NULL);
        SubmitButton = findViewById(R.id.btnsubmitApplication);
        totalSore =  findViewById(R.id.totalScoreTextView);

        if (isFromAnswer) {
            Log.d("isFromAnswer: ", "isFromAnswer:" + isFromAnswer);

            gatApplicationAnswersByID();

            SubmitButton.setVisibility(View.GONE);

            topicTitle.setFocusable(false);
            topicTitle.setClickable(false);

            topicDesc.setFocusable(false);
            topicDesc.setClickable(false);

            topicDate.setEnabled(false);
            topicDate.setClickable(false);
            instWhere.setEnabled(false);
            instWhere.setClickable(false);
            effect.setEnabled(false);
            effect.setClickable(false);
            well.setFocusable(false);
            well.setEnabled(false);
            notexpected.setFocusable(false);
            notexpected.setEnabled(false);
            reflection.setFocusable(false);
            reflection.setEnabled(false);
            different.setFocusable(false);
            different.setEnabled(false);

        } else {

            totalSore.setVisibility(View.GONE);

            topicTitle.setClickable(true);
            topicDesc.setClickable(true);

            topicDate.setEnabled(true);
            topicDate.setClickable(true);
            instWhere.setEnabled(true);
            instWhere.setClickable(true);
            effect.setEnabled(true);
            effect.setClickable(true);
            well.setFocusable(true);
            well.setEnabled(true);
            notexpected.setFocusable(true);
            notexpected.setEnabled(true);
            reflection.setFocusable(true);
            reflection.setEnabled(true);
            different.setFocusable(true);
            different.setEnabled(true);
            Log.d("isFromAnswer: ", "isFromAnswer:" + isFromAnswer);
        }

    }

    public void SubmitApplication(View view) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(TakeApplication.this);
        builder1.setMessage("Would You Like to Submit this Application.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isValid()) {
                            submitApplicationByID();
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    void submitApplicationByID() {

        ApplicationAnswers applicationAnswers = new ApplicationAnswers();
        applicationAnswers.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        applicationAnswers.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        applicationAnswers.setTopic_id(topicID);
        applicationAnswers.setTopic_desc(topicDE);

        applicationAnswers.setApp_attempt("1");
        applicationAnswers.setApp_seen("1");
        applicationAnswers.setDifferent(different.getText().toString());
        applicationAnswers.setEffect(effect.getText().toString());
        applicationAnswers.setInstance(instWhere.getText().toString());
        applicationAnswers.setUnexpected(notexpected.getText().toString());
        applicationAnswers.setWell(well.getText().toString());
        applicationAnswers.setDate_applied(topicDate.getText().toString());
        applicationAnswers.setReflection(reflection.getText().toString());

        String dataString = gson.toJson(applicationAnswers, ApplicationAnswers.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.SubmitApplication.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop submit application", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("submit_application");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(TakeApplication.this);
                        builder1.setMessage("Application Submited Successfully.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

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
                Toasty.warning(TakeApplication.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetApplicationList");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    void gatApplicationAnswersByID() {

        ApplicationAnswers applicationAnswers = new ApplicationAnswers();
        applicationAnswers.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        applicationAnswers.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        applicationAnswers.setTopic_id(topicID);

        String dataString = gson.toJson(applicationAnswers, ApplicationAnswers.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.viewApplicationAnswer.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop get application application", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject jsonobj = response.getJSONObject("view_app_answers");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        JSONArray jsonArray = jsonobj.getJSONArray("data");

                        if (jsonArray.getJSONObject(0).getString("date_applied") != null) {
                            topicDate.setText(jsonArray.getJSONObject(0).getString("date_applied"));
                            instWhere.setText(jsonArray.getJSONObject(0).getString("instance"));
                            different.setText(jsonArray.getJSONObject(0).getString("different"));
                            notexpected.setText(jsonArray.getJSONObject(0).getString("unexpected"));
                            reflection.setText(jsonArray.getJSONObject(0).getString("reflection"));
                            well.setText(jsonArray.getJSONObject(0).getString("well"));
                            effect.setText(jsonArray.getJSONObject(0).getString("effect"));
                            totalSore.setText("Score : "+jsonArray.getJSONObject(0).getString("total_marks"));

                        }
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
                Toasty.warning(TakeApplication.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetApplicationAnswers");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }


    public boolean isValid() {

        if (!InputValidation.hasText(instWhere)) {
            return false;
        }

        if (!InputValidation.TxtviewhasText(topicDate)) {
            return false;
        }

        if (!InputValidation.hasText(effect)) {
            return false;
        }

        if (!InputValidation.hasText(well)) {
            return false;
        }

        if (!InputValidation.hasText(notexpected)) {
            return false;
        }

        if (!InputValidation.hasText(reflection)) {
            return false;
        }

        return InputValidation.hasText(different);
    }

}
