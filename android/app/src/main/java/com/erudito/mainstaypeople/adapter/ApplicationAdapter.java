package com.erudito.mainstaypeople.adapter;

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
import com.erudito.mainstaypeople.Classes.TakeApplication;
import com.google.gson.Gson;

import com.erudito.mainstaypeople.models.ApplicationModel;
import com.erudito.mainstaypeople.utils.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by nonstop on 9/2/18.
 */

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.MyViewHolder> {

    private ArrayList<ApplicationModel> ApplicationList;
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


    public ApplicationAdapter(ArrayList<ApplicationModel> ApplicationList, Context mContext) {
        this.ApplicationList = ApplicationList;
        ApplicationAdapter.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_quiz_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ApplicationModel ApplicationModel = ApplicationList.get(position);

        int pos = position + 1;

        txtnumber.setText(String.valueOf(pos));

        if (ApplicationModel.getTitle() != null)
            txtDescription.setText(ApplicationModel.getTitle());

        if (ApplicationModel.getCreated_at() != null)
            txtTimestamp.setText(ApplicationModel.getCreated_at());

        if (ApplicationModel.getStatus() != null) {
            if(ApplicationModel.getStatus().equals("take_now")) {
                btnreview.setBackgroundResource(R.drawable.chip_take_now);
            } else if (ApplicationModel.getStatus().equals("submitted")) {
                btnreview.setVisibility(View.GONE);
                btnsubmitted.setVisibility(View.VISIBLE);
                btnviewanswers.setVisibility(View.VISIBLE);
                txtScore.setVisibility(View.VISIBLE);
            } else if (ApplicationModel.getStatus().equals("pending_review")) {
                btnreview.setBackgroundResource(R.drawable.chip_pending);
            }
        }

        txtScore.setText(Html.fromHtml("You have secured " + "<b>" + ApplicationModel.getScore() + "</b>" + " points in this quiz"));

        btnreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ApplicationList.get(position).getStatus().equals("take_now")) {
                    Intent intent = new Intent(mContext, TakeApplication.class);
                    intent.putExtra("ApplicationID", ApplicationList.get(position).getTopic_id());
                    intent.putExtra("ApplicationTopicName", ApplicationList.get(position).getTitle());
                    intent.putExtra("ApplicationTopicDescription", ApplicationList.get(position).getTopic_desc());
                    mContext.startActivity(intent);
                }
            }
        });

        btnviewanswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, TakeApplication.class);
                intent.putExtra("fromview_Answers", "view_answers");
                intent.putExtra("ApplicationID", ApplicationList.get(position).getTopic_id());
                intent.putExtra("ApplicationTopicName", ApplicationList.get(position).getTitle());
                mContext.startActivity(intent);

            }
        });

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return ApplicationList.size();
    }

}
