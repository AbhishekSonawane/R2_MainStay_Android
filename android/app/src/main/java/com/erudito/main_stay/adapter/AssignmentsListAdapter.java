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


import com.android.main_stay.R;
import com.erudito.main_stay.Classes.TakeAssignment;
import com.erudito.main_stay.Classes.ViewAssignment;
import com.google.gson.Gson;
import com.erudito.main_stay.models.AssignmentModel;
import com.erudito.main_stay.utils.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by nonstop on 25/1/18.
 */

public class AssignmentsListAdapter extends RecyclerView.Adapter<AssignmentsListAdapter.MyViewHolder> {

    private ArrayList<AssignmentModel> AssignmentList;
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


    public AssignmentsListAdapter(ArrayList<AssignmentModel> AssignmentList, Context mContext) {
        this.AssignmentList = AssignmentList;
        AssignmentsListAdapter.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_quiz_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final AssignmentModel assignmentModel = AssignmentList.get(position);

        int pos = position + 1;

        txtnumber.setText(String.valueOf(pos));

        if (assignmentModel.getTitle() != null)
            txtDescription.setText(assignmentModel.getTitle());

        if (assignmentModel.getCreated_at() != null)
            txtTimestamp.setText(assignmentModel.getCreated_at());

        if (assignmentModel.getStatus() != null) {
            if(assignmentModel.getStatus().equals("take_now")) {
                btnreview.setBackgroundResource(R.drawable.chip_take_now);
            } else if (assignmentModel.getStatus().equals("submitted")) {
                btnreview.setVisibility(View.GONE);
                btnsubmitted.setVisibility(View.VISIBLE);
                btnviewanswers.setVisibility(View.VISIBLE);
                txtScore.setVisibility(View.VISIBLE);
            } else if (assignmentModel.getStatus().equals("pending_review")) {
                btnreview.setBackgroundResource(R.drawable.chip_pending);
            }
        }

        txtScore.setText(Html.fromHtml("You have secured " + "<b>" + assignmentModel.getScore() + "</b>" + " points in this quiz"));

        btnreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AssignmentList.get(position).getStatus().equals("take_now")) {
                    Intent intent = new Intent(mContext, TakeAssignment.class);
                    intent.putExtra("AssignmentID", AssignmentList.get(position).getAssign_id());
                    mContext.startActivity(intent);
                }
            }
        });

        btnviewanswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ViewAssignment.class);
                intent.putExtra("AssignmentID", AssignmentList.get(position).getAssign_id());
                mContext.startActivity(intent);

            }
        });

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return AssignmentList.size();
    }

}
