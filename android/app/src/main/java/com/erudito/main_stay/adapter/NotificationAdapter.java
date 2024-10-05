package com.erudito.main_stay.adapter;

/**
 * Created by nonstop on 27/11/17.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.erudito.main_stay.Classes.App;
import com.erudito.main_stay.Classes.Application;
import com.erudito.main_stay.Classes.Comments;
import com.erudito.main_stay.Classes.MainFragment;
import com.erudito.main_stay.Classes.R2Values;
import com.erudito.main_stay.Classes.TabActivity;
import com.google.gson.Gson;
import com.android.main_stay.R;
import com.erudito.main_stay.models.NotificationsModel;
import com.erudito.main_stay.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import androidx.recyclerview.widget.RecyclerView;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ArrayList<NotificationsModel> NotificationsList;
    public static Context mContext;
    ImageView notif_user_img;
    TextView txtDescription, txtTimestamp;
    LinearLayout notiflinlay;
    PreferenceHelper mPreferenceHelper;
    Gson gson;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

            notif_user_img = view.findViewById(R.id.notif_user_img);
            txtDescription = view.findViewById(R.id.txtDescription);
            txtTimestamp = view.findViewById(R.id.txtTimestamp);
            notiflinlay = view.findViewById(R.id.notiflinlay);
            mPreferenceHelper = new PreferenceHelper(mContext);
            gson = new Gson();

        }
    }


    public NotificationAdapter(ArrayList<NotificationsModel> NotificationsList, Context mContext) {
        this.NotificationsList = NotificationsList;
        NotificationAdapter.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notificationslist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final NotificationsModel notifications = NotificationsList.get(position);

        if (notifications.getText() != null)
            txtDescription.setText(Html.fromHtml(notifications.getText()), TextView.BufferType.SPANNABLE);

        if (notifications.getDate() != null)
            txtTimestamp.setText(notifications.getDate());

        if (notifications.getNotif_img() != null) {

            Picasso.with(mContext).cancelRequest(notif_user_img);
            Picasso.with(mContext)
                    .load(R2Values.Web.BASE_URL + notifications.getNotif_img())
                /*.placeholder(R.drawable.default_post_image) // optional
                    .error(R.drawable.default_post_image)         // optional*/
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            //Set it in the ImageView
                            notif_user_img.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }
                    });
        }

        if (notifications.getRead().equals("False")) {
            holder.itemView.setBackgroundColor(Color.parseColor("#CCECF8"));
        } else {
        }

        notiflinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadNotification(position);
            }
        });

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return NotificationsList.size();
    }


    public void ReadNotification(final int position) {

        NotificationsModel notificationsModel = new NotificationsModel();
        notificationsModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        notificationsModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        notificationsModel.setNotif_id(NotificationsList.get(position).getNotif_id());

        String dataString = gson.toJson(notificationsModel, NotificationsModel.class);
        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.ReadNotification.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "Read Notification response  ---------------" + response);
                try {

                    JSONObject jsonobj = response.getJSONObject("read_notification");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        String redirect_to = NotificationsList.get(position).getRedirect_to();

                        switch (redirect_to) {
                            case "Post":

                                Intent commentintent = new Intent(mContext, Comments.class);
                                commentintent.putExtra("post_id", NotificationsList.get(position).getRedirect_id());
                                mContext.startActivity(commentintent);
                                break;

                            case "Comment":
                                Intent intent = new Intent(mContext, Comments.class);
                                intent.putExtra("post_id", NotificationsList.get(position).getRedirect_id());
                                mContext.startActivity(intent);
                                break;

                            case "Quiz":
                                TabActivity.mViewPager.setCurrentItem(2);
                                MainFragment.mTabHost.setCurrentTab(0);
                                break;

                            case "Assignment":
                                TabActivity.mViewPager.setCurrentItem(2);
                                MainFragment.mTabHost.setCurrentTab(1);
                                break;

                            case "Application":
                                Intent applicationintent = new Intent(mContext, Application.class);
                                mContext.startActivity(applicationintent);
                                break;

                            default:
                                break;

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
                    message = mContext.getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = mContext.getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = mContext.getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toasty.warning(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "ReadNotification");

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }


}
