package com.erudito.mainstaypeople.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erudito.mainstaypeople.Classes.R2Values;
import com.android.main_stay.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by nonstop on 13/12/17.
 */

public class MyBatchAdapter extends BaseAdapter {

    private Context mContext;
    JSONArray jarray;

    public MyBatchAdapter(Context context,
                          JSONArray jarray) {
        mContext = context;
        this.jarray = jarray;
    }

    @Override
    public int getCount() {
        return jarray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jarray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder mHolder;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        mHolder = new Holder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_my_batch, parent, false);
            convertView.setTag(mHolder);

            mHolder.student_name = convertView.findViewById(R.id.student_name);
            mHolder.student_image = convertView.findViewById(R.id.student_image);

            try {
                if (jarray.getJSONObject(position).getString("student_first_name") != null) {
                    mHolder.student_name.setText(jarray.getJSONObject(position).getString("student_first_name") + " " + jarray.getJSONObject(position).getString("student_last_name"));
                }

                //  mHolder.student_image.setImageUrl(R2Values.Web.BASE_URL + jarray.getJSONObject(position).getString("student_picture_url"), App.getInstance().getImageLoader());

                if (jarray.getJSONObject(position).getString("student_picture_url") != null) {

                    Picasso.with(mContext).cancelRequest(mHolder.student_image);
                    Picasso.with(mContext)
                            .load(R2Values.Web.BASE_URL + jarray.getJSONObject(position).getString("student_picture_url"))
                            .placeholder(R.drawable.default_image) // optional
                            .error(R.drawable.default_image)         // optional
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    //Set it in the ImageView
                                    mHolder.student_image.setImageBitmap(bitmap);
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                }
                            });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    static class Holder {
        private TextView student_name;
        private ImageView student_image;
    }

    //This indeed provides you with the same view type for every row.

    @Override
    public int getViewTypeCount() {
        if (getCount() > 0) {
            return getCount();
        } else {
            return super.getViewTypeCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}