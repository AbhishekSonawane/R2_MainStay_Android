package com.android.main_stay.Classes;

import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class TakeQuiz extends AppCompatActivity {

    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private TextView txtquizname, txtquestion, txtchoice1, txtchoice2, txtchoice3, txtchoice4, txtnext, txtprev, txtquestionno;
    private ArrayList<GetQuestionsModel> getquestionsList;
    private ScrollView linlaymcq;
    private EditText edtessay;
    private int mQuestionNumber = 0;
    GetQuestionsModel currentQ;
    Button txtsubmit;
    private List<QuizAnswers> answersList = new ArrayList<>();
    private List<QuizAnswers> tempList;
    private ImageView close;
    int prevqid = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

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
        txtsubmit = findViewById(R.id.txtsubmit);
        close = findViewById(R.id.close);


        txtquestion.setMovementMethod(new ScrollingMovementMethod());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GetQuizQuestions();

        edtessay.setSelection(0);

        txtchoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswerBackgroundColor();
                txtchoice1.setBackgroundResource(R.drawable.textview_background);
                addSelectedAnsToAnsArray(1);
            }
        });


        txtchoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswerBackgroundColor();
                txtchoice2.setBackgroundResource(R.drawable.textview_background);
                addSelectedAnsToAnsArray(2);
            }
        });


        txtchoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswerBackgroundColor();
                txtchoice3.setBackgroundResource(R.drawable.textview_background);
                addSelectedAnsToAnsArray(3);

            }
        });

        txtchoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnswerBackgroundColor();
                txtchoice4.setBackgroundResource(R.drawable.textview_background);
                addSelectedAnsToAnsArray(4);

            }
        });


        txtprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                currentQ = getquestionsList.get(mQuestionNumber);
                Log.d("Pooja Data", "On click of next");
                updateprevQuestion();
            }
        });

        txtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                currentQ = getquestionsList.get(mQuestionNumber);
                Log.d("Pooja Data", "On click of next");
            /*    if(getquestionsList.size() != mQuestionNumber -1 ) {*/
                updateQuestion();
                /*}else{
                   txtsubmit.setVisibility(View.VISIBLE);
                }*/
            }
        });

        txtsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject POST_DATA = new JSONObject();
                JSONArray answers = new JSONArray();

                Log.d("question id", String.valueOf(mQuestionNumber));

                if (getquestionsList.get(mQuestionNumber).getQuestion_type().equals("Essay")) {
                    addEssayAnsArray();
                }
//                        CAMP_OFFICE= "Address-Camp Office";
                for (QuizAnswers quizanswers : answersList) {
                    JSONObject tempItem = new JSONObject();
                    try {
                        tempItem.put("question_id", quizanswers.getQuestion_id());
                        tempItem.put("answer_text", quizanswers.getAnswer_text());
                        tempItem.put("attempt", quizanswers.getAttempt());
                        tempItem.put("seen", quizanswers.getSeen());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    answers.put(tempItem);
                }
                try {
                    POST_DATA.put("quiz_id", getIntent().getStringExtra(getApplicationContext().getString(R.string.quizidkey)));
                    POST_DATA.put("quiz_rating", "5");
                    POST_DATA.put("email", mPreferenceHelper.getString(R2Values.Commons.EMAIL));
                    POST_DATA.put("password", mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
                    POST_DATA.put("answers", answers);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SubmitQuiz(POST_DATA);
            }
        });

    }

    public void GetQuizQuestions() {

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


      //  StringRequest request = new StringRequest(Request.Method.POST, R2Values.Web.GetQuizQuestions.SERVICE_URL,jsonObject, new Response.Listener<String>() {
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,R2Values.Web.GetQuizQuestions.SERVICE_URL ,jsonObject, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                try {

                    JSONObject jsonobj = response.getJSONObject("get_quiz_questions");
                    if (jsonobj.getString("status").equals("success")) {
                        JSONArray jarray = jsonobj.getJSONArray("data");

                        if (jarray.length() > 0) {
                            Type type = new TypeToken<ArrayList<GetQuestionsModel>>() {
                            }.getType();
                            getquestionsList = new Gson().fromJson(jarray.toString(), type);
                            Log.d("NonStop", "questions array: " + getquestionsList.size() + " " + jarray.length());
                        }
                        for (GetQuestionsModel getquestions : getquestionsList) {
                            QuizAnswers answers = new QuizAnswers(getquestions.getQuestion_id(), "", "1", "0");
                            answersList.add(answers);

                            Log.d("Pooja Data", " answerlist init " + answersList.toString());

                        }

                        txtquizname.setText(jsonobj.getString("quiz_title"));

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
                            txtsubmit.setVisibility(View.VISIBLE);
                        } else {
                            txtnext.setVisibility(View.VISIBLE);
                            txtsubmit.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {


                Log.e("Error", volleyError.getMessage());

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
                Toasty.warning(TakeQuiz.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetQuizQuestions");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    private void updateQuestion() {

        if (mQuestionNumber != getquestionsList.size() - 1) {

            mQuestionNumber = mQuestionNumber + 1;

            int qid = mQuestionNumber + 1;

            prevqid = mQuestionNumber -1 ;

            txtquestionno.setText("Question " + qid + "/" + getquestionsList.size());

            txtquestion.setText(getquestionsList.get(mQuestionNumber).getQuestion_text());

            if (getquestionsList.get(mQuestionNumber).getQuestion_type().equals("Essay")) {
                linlaymcq.setVisibility(View.GONE);
                edtessay.setVisibility(View.VISIBLE);
             //   addEssayToAnsArray();

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
                txtsubmit.setVisibility(View.VISIBLE);
            } else {
                txtnext.setVisibility(View.VISIBLE);
                txtsubmit.setVisibility(View.GONE);
            }

            setAnswerBackgroundColor();

            tempList = new ArrayList<>(answersList);

            if (tempList.get(mQuestionNumber).getAnswer_text() != "") {
                String ans = tempList.get(mQuestionNumber).getAnswer_text();

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

            if (getquestionsList.get(prevqid).getQuestion_type().equals("Essay")) {
                addEssayToAnsArray();
            }

        }
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
                txtsubmit.setVisibility(View.VISIBLE);
            } else {
                txtnext.setVisibility(View.VISIBLE);
                txtsubmit.setVisibility(View.GONE);
            }

            setAnswerBackgroundColor();

            tempList = new ArrayList<>(answersList);

            if (tempList.get(mQuestionNumber).getAnswer_text() != "") {
                String ans = tempList.get(mQuestionNumber).getAnswer_text();

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

        }
    }

    private void checkVisibility() {

    }

    private void addSelectedAnsToAnsArray(Integer optionnumber) {

        String selectedAns = "";

        switch (optionnumber) {
            case 1:
                selectedAns = "A";
                break;
            case 2:
                selectedAns = "B";
                break;
            case 3:
                selectedAns = "C";
                break;
            case 4:
                selectedAns = "D";
                break;
          /*  case 5:
                selectedAns = (self.QuizeQueObj["op_a"] as? String)!*/
            default:
                selectedAns = "";
        }

        QuizAnswers answers = new QuizAnswers(getquestionsList.get(mQuestionNumber).getQuestion_id(), selectedAns, "1", "1");
        answersList.set(mQuestionNumber, answers);

        for (QuizAnswers quizanswers : answersList) {
            Log.d("Pooja Data answersarray", quizanswers.getQuestion_id() + " " + quizanswers.getAnswer_text());
        }

    }

    private void addEssayAnsArray() {

        QuizAnswers answers = new QuizAnswers(getquestionsList.get(mQuestionNumber).getQuestion_id(), edtessay.getText().toString(), "1", "1");
        answersList.set(mQuestionNumber, answers);

        edtessay.setText("");

        for (QuizAnswers quizanswers : answersList) {
            Log.d("Pooja Data answersarray", quizanswers.getQuestion_id() + " " + quizanswers.getAnswer_text());
        }

    }

    private void addEssayToAnsArray() {

        QuizAnswers answers = new QuizAnswers(getquestionsList.get(prevqid).getQuestion_id(), edtessay.getText().toString(), "1", "1");
        answersList.set(prevqid, answers);

        edtessay.setText("");

        for (QuizAnswers quizanswers : answersList) {
            Log.d("Pooja Data answersarray", quizanswers.getQuestion_id() + " " + quizanswers.getAnswer_text());
        }

    }

    public void SubmitQuiz(final JSONObject POST_DATA) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("data",POST_DATA);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.SubmitQuiz.SERVICE_URL, jsonObject,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);

                try {


                    JSONObject jsonobj = response.getJSONObject("submit_quiz");

                    if (jsonobj.getString("status").equals("fail")) {
                        Toasty.error(TakeQuiz.this, jsonobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        finish();

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
                Toasty.warning(TakeQuiz.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "SubmitQuiz");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    private void setAnswerBackgroundColor() {

        txtchoice1.setBackgroundResource(R.drawable.common_answer_background);

        txtchoice2.setBackgroundResource(R.drawable.common_answer_background);

        txtchoice3.setBackgroundResource(R.drawable.common_answer_background);

        txtchoice4.setBackgroundResource(R.drawable.common_answer_background);
    }
}
