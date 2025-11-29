package com.erudito.mainstaypeople.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.erudito.mainstaypeople.Classes.App;
import com.erudito.mainstaypeople.Classes.R2Values;
import com.android.volley.toolbox.NetworkImageView;
import com.android.main_stay.R;
import com.erudito.mainstaypeople.models.BadgeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nonstop on 6/4/18.
 */


public class BadgesListAdapter extends RecyclerView.Adapter<BadgesListAdapter.ViewHolder> {

    private final List<BadgeModel > mValues;
    Context mContext;

    public BadgesListAdapter(ArrayList<BadgeModel > items, Context mContext) {
        mValues = items;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_badge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final BadgeModel  BadgeModel  = mValues.get(position);

        if (BadgeModel .getName() != null)
            holder.mContentView.setText(BadgeModel.getName());

        if (BadgeModel .getDesc() != null)
            holder.txtScore.setText(BadgeModel.getDesc());

        if (BadgeModel.getImage() != null)
            holder.user_img.setImageUrl(R2Values.Web.BASE_URL + "media/"+BadgeModel.getImage() , App.getInstance().getImageLoader());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtScore,mContentView;
        public final NetworkImageView user_img;

        //     public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.txtDescription);
            user_img = view.findViewById(R.id.user_img);
            txtScore = view.findViewById(R.id.txtScore);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
