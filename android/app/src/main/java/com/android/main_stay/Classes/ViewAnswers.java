package com.android.main_stay.Classes;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.main_stay.models.GetQuestionsModel;
import com.android.main_stay.models.LoginModel;
import com.android.main_stay.models.QuizAnswers;
import com.android.main_stay.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ViewAnswers extends AppCompatActivity {

    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private TextView txtquizname, txtquestion, txtchoice1, txtchoice2, txtchoice3, txtchoice4, txtnext, txtprev, txtquestionno, txtscore;
    private ArrayList<GetQuestionsModel> getquestionsList;
    private ScrollView linlaymcq;
    private TextView edtessay;
    private int mQuestionNumber = 0;
    GetQuestionsModel currentQ;
    Button txtfinish;
    private List<QuizAnswers> answersList = new ArrayList<>();
    private List<QuizAnswers> tempList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answers);

        gson = new Gson();
        mPreferenceHelper = new PreferenceHelper(this);
        linlaymcq = findViewById(R.id.linlaymcq);
        edtessay = findViewById(R.id.edtessay);

        txtquizname = findViewById(R.id.txtquizname);
        txtquestion = findViewById(R.id.txtquestion);
        txtchoice1 = findViewById(R.id.txtchoice1);
        txtchoice2 = findViewById(R.id.txtchoice2);
        txtchoice3 = findViewById(R.id.txtchoice3);
        txtchoice4 = findViewById(R.id.txtchoice4);
        txtprev = findViewById(R.id.txtprev);
        txtnext = findViewById(R.id.txtnext);
        txtquestionno = findViewById(R.id.txtquestionno);
        txtfinish = findViewById(R.id.txtfinish);
        txtscore = findViewById(R.id.txtscore);

        txtquestion.setMovementMethod(new ScrollingMovementMethod());

        ImageView close = findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewQuizAnswers();

        txtprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Pooja Data", "On click of next");
                updateprevQuestion();
            }
        });

        txtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Pooja Data", "On click of next");
                updateQuestion();

            }
        });

        txtfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void ViewQuizAnswers() {

        LoginModel loginModel = new LoginModel();

        loginModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        loginModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        loginModel.setQuiz_id(getIntent().getStringExtra(getApplicationContext().getString(R.string.quizidkey)));

        String dataString = gson.toJson(loginModel, LoginModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.ViewQuizAnswers.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                try {

                    JSONObject jsonobj = response.getJSONObject("view_quiz_answers");
                    if (jsonobj.getString("status").equals("success")) {
                        JSONArray jarray = jsonobj.getJSONArray("data");

                        if (jarray.length() > 0) {
                            Type type = new TypeToken<ArrayList<GetQuestionsModel>>() {
                            }.getType();
                            getquestionsList = new Gson().fromJson(jarray.toString(), type);
                            Log.d("NonStop", "questions array: " + getquestionsList.size() + " " + jarray.length());
                        }

                        txtquizname.setText(jsonobj.getString("quiz_title"));

                        int qid = mQuestionNumber + 1;
                        txtquestionno.setText("Question " + qid + "/" + getquestionsList.size());

                        txtquestion.setText(getquestionsList.get(mQuestionNumber).getQuestion_text());
                        txtscore.setText("Total Score : " + jsonobj.getString("total_score"));

                        if (getquestionsList.get(mQuestionNumber).getQuestion_type().equals("Essay")) {
                            linlaymcq.setVisibility(View.GONE);
                            edtessay.setVisibility(View.VISIBLE);
                        } else {
                            linlaymcq.setVisibility(View.VISIBLE);
                            edtessay.setVisibility(View.GONE);

                            txtchoice1.setText(getquestionsList.get(mQuestionNumber).getOp_a());
                            txtchoice2.setText(getquestionsList.get(mQuestionNumber).getOp_b());
                            txtchoice3.setText(getquestionsList.get(mQuestionNumber).getOp_c());
                            txtchoice4.setText(getquestionsList.get(mQuestionNumber).getOp_d());
                        }

                        setTextViewBackground();

                        if (getquestionsList.size() == mQuestionNumber + 1) {
                            txtnext.setVisibility(View.GONE);
                            txtfinish.setVisibility(View.VISIBLE);
                        } else {
                            txtnext.setVisibility(View.VISIBLE);
                            txtfinish.setVisibility(View.GONE);
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
                Toasty.warning(ViewAnswers.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "ViewQuizAnswers");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    private void updateprevQuestion() {

        if (mQuestionNumber != 0) {

            mQuestionNumber = mQuestionNumber - 1;

            int qid = mQuestionNumber + 1;

            txtquestionno.setText("Question " + qid + "/" + getquestionsList.size());

            txtquestion.setText(getquestionsList.get(mQuestionNumber).getQuestion_text());

            if (getquestionsList.get(mQuestionNumber).getQuestion_type().equals("Essay")) {
                linlaymcq.setVisibility(View.GONE);
                edtessay.setVisibility(View.VISIBLE);
            } else {
                linlaymcq.setVisibility(View.VISIBLE);
                edtessay.setVisibility(View.GONE);

                txtchoice1.setText(getquestionsList.get(mQuestionNumber).getOp_a());
                txtchoice2.setText(getquestionsList.get(mQuestionNumber).getOp_b());
                txtchoice3.setText(getquestionsList.get(mQuestionNumber).getOp_c());
                txtchoice4.setText(getquestionsList.get(mQuestionNumber).getOp_d());
            }

            if (getquestionsList.size() == mQuestionNumber + 1) {
                txtnext.setVisibility(View.GONE);
                txtfinish.setVisibility(View.VISIBLE);
            } else {
                txtnext.setVisibility(View.VISIBLE);
                txtfinish.setVisibility(View.GONE);
            }

            setTextViewBackground();

        }
    }

    private void updateQuestion() {

        if (mQuestionNumber != getquestionsList.size() - 1) {

            mQuestionNumber = mQuestionNumber + 1;

            int qid = mQuestionNumber + 1;

            txtquestionno.setText("Question " + qid + "/" + getquestionsList.size());

            txtquestion.setText(getquestionsList.get(mQuestionNumber).getQuestion_text());

            if (getquestionsList.get(mQuestionNumber).getQuestion_type().equals("Essay")) {
                linlaymcq.setVisibility(View.GONE);
                edtessay.setVisibility(View.VISIBLE);

                Log.d("Essay text", edtessay.getText().toString());

            } else {
                linlaymcq.setVisibility(View.VISIBLE);
                edtessay.setVisibility(View.GONE);

                txtchoice1.setText(getquestionsList.get(mQuestionNumber).getOp_a());
                txtchoice2.setText(getquestionsList.get(mQuestionNumber).getOp_b());
                txtchoice3.setText(getquestionsList.get(mQuestionNumber).getOp_c());
                txtchoice4.setText(getquestionsList.get(mQuestionNumber).getOp_d());
            }

            if (getquestionsList.size() == mQuestionNumber + 1) {
                txtnext.setVisibility(View.GONE);
                txtfinish.setVisibility(View.VISIBLE);
            } else {
                txtnext.setVisibility(View.VISIBLE);
                txtfinish.setVisibility(View.GONE);
            }

            setTextViewBackground();

        }
    }


    private void setAnswerBackgroundColor() {

        txtchoice1.setBackgroundResource(R.drawable.common_answer_background);

        txtchoice2.setBackgroundResource(R.drawable.common_answer_background);

        txtchoice3.setBackgroundResource(R.drawable.common_answer_background);

        txtchoice4.setBackgroundResource(R.drawable.common_answer_background);
    }

    private void setTextViewBackground() {

        setAnswerBackgroundColor();

        if (getquestionsList.get(mQuestionNumber).getStudent_ans() != "") {
            String ans = getquestionsList.get(mQuestionNumber).getStudent_ans();

            switch (ans) {
                case "A":
                    txtchoice1.setBackgroundResource(R.drawable.textview_background);
                    break;
                case "B":
                    txtchoice2.setBackgroundResource(R.drawable.textview_background);
                    break;
                case "C":
                    txtchoice3.setBackgroundResource(R.drawable.textview_background);
                    break;
                case "D":
                    txtchoice4.setBackgroundResource(R.drawable.textview_background);
                    break;

                default:
                    edtessay.setText(ans);
            }

        }

        if (getquestionsList.get(mQuestionNumber).getCorrect_ans() != "") {
            String ans = getquestionsList.get(mQuestionNumber).getCorrect_ans();

            switch (ans) {
                case "A":
                    txtchoice1.setBackgroundResource(R.drawable.correct_answer_background);
                    break;
                case "B":
                    txtchoice2.setBackgroundResource(R.drawable.correct_answer_background);
                    break;
                case "C":
                    txtchoice3.setBackgroundResource(R.drawable.correct_answer_background);
                    break;
                case "D":
                    txtchoice4.setBackgroundResource(R.drawable.correct_answer_background);
                    break;

                default:
                    edtessay.setText(ans);
            }

        }

    }

}
