package com.erudito.main_stay.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.erudito.main_stay.Classes.TakeQuiz;
import com.erudito.main_stay.Classes.ViewAnswers;
import com.google.gson.Gson;
import com.android.main_stay.R;
import com.erudito.main_stay.models.Quiz;
import com.erudito.main_stay.utils.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by nonstop on 2/1/18.
 */

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.MyViewHolder> {

    private ArrayList<Quiz> QuizList;
    public static Context mContext;
    TextView txtDescription, txtTimestamp, btnreview, btnsubmitted, btnviewanswers, txtScore, txtnumber;
    PreferenceHelper mPreferenceHelper;
    Gson gson;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

            txtDescription = view.findViewById(R.id.txtDescription);
            txtTimestamp = view.findViewById(R.id.txtTimestamp);
            btnviewanswers = view.findViewById(R.id.btnviewanswers);
            btnreview = view.findViewById(R.id.btnreview);
            btnsubmitted = view.findViewById(R.id.btnsubmitted);
            txtScore = view.findViewById(R.id.txtScore);
            txtnumber = view.findViewById(R.id.txtnumber);
            mPreferenceHelper = new PreferenceHelper(mContext);
            gson = new Gson();

        }
    }


    public QuizListAdapter(ArrayList<Quiz> QuizList, Context mContext) {
        this.QuizList = QuizList;
        QuizListAdapter.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_quiz_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Quiz quiz = QuizList.get(position);

        int pos = position + 1;

        txtnumber.setText(String.valueOf(pos));

        if (quiz.getQuiz_desc() != null)
            txtDescription.setText(quiz.getQuiz_desc());

        if (quiz.getQuiz_created_at() != null)
            txtTimestamp.setText(quiz.getQuiz_created_at());

        if (quiz.getQuiz_status() != null) {
            if (quiz.getQuiz_status().equals("take_now")) {
                btnreview.setBackgroundResource(R.drawable.chip_take_now);
            } else if (quiz.getQuiz_status().equals("submitted")) {
                btnreview.setVisibility(View.GONE);
                btnsubmitted.setVisibility(View.VISIBLE);
                btnviewanswers.setVisibility(View.VISIBLE);
                txtScore.setVisibility(View.VISIBLE);
            } else if (quiz.getQuiz_status().equals("pending_review")) {
                btnreview.setBackgroundResource(R.drawable.chip_pending);
            }
        }

        txtScore.setText(Html.fromHtml("You have secured " + "<b>" + quiz.getScore() + "</b>" + " points in this quiz"));

        btnreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (QuizList.get(position).getQuiz_status().equals("take_now")) {
                    Intent intent = new Intent(mContext, TakeQuiz.class);
                    intent.putExtra(mContext.getString(R.string.quizidkey), QuizList.get(position).getQuiz_id());
                    mContext.startActivity(intent);
                }
            }
        });

        btnviewanswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ViewAnswers.class);
                intent.putExtra(mContext.getString(R.string.quizidkey), QuizList.get(position).getQuiz_id());
                mContext.startActivity(intent);
            }
        });

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return QuizList.size();
    }

}
