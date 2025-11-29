package com.erudito.mainstaypeople.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.erudito.mainstaypeople.Classes.R2Values;
import com.android.main_stay.R;
import com.erudito.mainstaypeople.fragment.RankingsList;
import com.erudito.mainstaypeople.models.RankingsModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nonstopio on 28/03/18.
 */

public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.ViewHolder> {

    private final List<RankingsModel> mValues;
    private final RankingsList.OnListFragmentInteractionListener mListener;
    Context mContext;

    public BadgesAdapter(ArrayList<RankingsModel> items, RankingsList.OnListFragmentInteractionListener listener, Context mContext) {
        mValues = items;
        mListener = listener;
        this.mContext = mContext;
    }

    @Override
    public BadgesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new BadgesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BadgesAdapter.ViewHolder holder, int position) {

        final RankingsModel rankingsModel = mValues.get(position);

        int pos = position + 1;

        holder.mIdView.setText(String.valueOf(pos));

        if (rankingsModel.getUser_name() != null)
            holder.mContentView.setText(rankingsModel.getUser_name());

        if (rankingsModel.getUser_points() != null)
            holder.txtScore.setText(rankingsModel.getUser_points() + " Points");

        if(rankingsModel.getCurrent_user().equals("True") || position == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#9e9e9e"));
            holder.mIdView.setBackgroundResource(R.drawable.circular_layout);
            holder.mIdView.setTextColor(Color.WHITE);


        }


        if (rankingsModel.getUser_img() != null) {

            Picasso.with(mContext).cancelRequest(holder.user_img);
            Picasso.with(mContext)
                    .load(R2Values.Web.BASE_URL + rankingsModel.getUser_img())
                    .placeholder(R.drawable.default_image) // optional
                    .error(R.drawable.default_image)         // optional*/
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            //Set it in the ImageView
                            holder.user_img.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }



                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }


                    });
        }

     /* holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
*/
  /*      holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.rankingsModel);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView,txtScore,mContentView;
        public final ImageView user_img;

        //     public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
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