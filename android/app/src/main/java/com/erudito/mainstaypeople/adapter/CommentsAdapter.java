package com.erudito.mainstaypeople.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.erudito.mainstaypeople.Classes.App;
import com.erudito.mainstaypeople.Classes.R2Values;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.gson.Gson;
import com.android.main_stay.R;
import com.erudito.mainstaypeople.models.CommentsModel;
import com.erudito.mainstaypeople.models.LikeModel;
import com.erudito.mainstaypeople.utils.FullImageActivity;
import com.erudito.mainstaypeople.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.erudito.mainstaypeople.utils.Utils.getYoutubeVideoId;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by nonstop on 1/11/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    public static ArrayList<CommentsModel> commentsList;
    CommentsAdapter.MyViewHolder.RecyclerViewClickListener listener;
    static ImageView imglike, imgbookmark, imgshare, comment_user_img, post_image;
    public static Gson gson;
    public static PreferenceHelper mPreferenceHelper;
    public static Context mContext;
    public static LinearLayout postlinlay, linlaycomment;
    public static TextView txtstudent_name, comment_text, comment_created_at, txtcommentscount, txtlike_count;
    String post_id;
    static int like_count;
    static RelativeLayout relativeLayoutOverYouTubeThumbnailView;
    String videoId;
    static ImageView img_thumbnail, playButton;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView post_title, post_created_at, post_description;
        CommentsAdapter.MyViewHolder.RecyclerViewClickListener listener;
        View viewdivider, commentdivider;
        CardView card_view;
        android.webkit.WebView webview;

        public MyViewHolder(View view, CommentsAdapter.MyViewHolder.RecyclerViewClickListener listener) {
            super(view);
            post_title = view.findViewById(R.id.txttitle);
            post_created_at = view.findViewById(R.id.txtdate);
            post_description = view.findViewById(R.id.txtpostdescription);
            imglike = view.findViewById(R.id.imglike);
            imgbookmark = view.findViewById(R.id.imgbookmark);
            imgshare = view.findViewById(R.id.imgshare);
            gson = new Gson();
            mPreferenceHelper = new PreferenceHelper(mContext);
            postlinlay = view.findViewById(R.id.postlinlay);
            linlaycomment = view.findViewById(R.id.linlaycomment);
            comment_user_img = view.findViewById(R.id.comment_user_img);
            txtstudent_name = view.findViewById(R.id.txtstudent_name);
            comment_text = view.findViewById(R.id.comment_text);
            post_image = view.findViewById(R.id.post_image);
            comment_created_at = view.findViewById(R.id.comment_created_at);
            txtcommentscount = view.findViewById(R.id.txtcommentscount);
            txtlike_count = view.findViewById(R.id.txtlike_count);
            webview = view.findViewById(R.id.webview);

            //      youTubeView = (YouTubePlayerView) view.findViewById(R.id.youtube_view);
            relativeLayoutOverYouTubeThumbnailView = itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            img_thumbnail = view.findViewById(R.id.img_thumbnail);
            playButton = view.findViewById(R.id.btnYoutube_player);
            //  viewdivider =(View) view.findViewById(R.id.viewdivider);
            commentdivider = view.findViewById(R.id.commentdivider);
            card_view = view.findViewById(R.id.card_view);

            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClickView(this.getAdapterPosition());
        }

        public interface RecyclerViewClickListener {
            void onClickView(int activityPosition);
        }

    }


    public CommentsAdapter(ArrayList<CommentsModel> commentsList, Context mContext) {
        CommentsAdapter.commentsList = commentsList;
        CommentsAdapter.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comments_list, parent, false);

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final CommentsModel commentsModel = commentsList.get(position);

        if (position == 0) {
            postlinlay.setVisibility(View.VISIBLE);
            linlaycomment.setVisibility(View.GONE);
            comment_created_at.setVisibility(View.GONE);
            comment_text.setVisibility(View.GONE);
            holder.commentdivider.setVisibility(View.GONE);
            holder.card_view.setVisibility(View.GONE);

            post_id = commentsModel.getPost_id();

            if (commentsModel.getCreated_at() != null)
                holder.post_created_at.setText(commentsModel.getCreated_at());

            if (commentsModel.getTitle() != null)
                holder.post_title.setText(commentsModel.getTitle());

            if (commentsModel.getText() != null)
                holder.webview.loadDataWithBaseURL(null, commentsModel.getText(), "text/html", "utf-8", null);

                holder.post_description.setText(Html.fromHtml(commentsModel.getText()), TextView.BufferType.SPANNABLE);

            if (commentsModel.getLike_count() != null)
                txtlike_count.setText(commentsModel.getLike_count());

            if (commentsModel.getIs_bookmarked() != null) {
                if (commentsModel.getIs_bookmarked().contentEquals("1")) {
                    imgbookmark.setImageDrawable(mContext.getDrawable(R.drawable.btn_bookmark_filled));
                } else {
                    imgbookmark.setImageDrawable(mContext.getDrawable(R.drawable.btn_bookmark_unfilled));
                }
            }

            if (commentsModel.getIs_liked() != null) {
                if (commentsModel.getIs_liked().contentEquals("1")) {
                    imglike.setImageDrawable(mContext.getDrawable(R.drawable.btn_like_filled));
                } else {
                    imglike.setImageDrawable(mContext.getDrawable(R.drawable.btn_like_unfilled));
                }
            }

            if (commentsModel.getComment_count() != null)
                txtcommentscount.setText(commentsModel.getComment_count());

            if (commentsModel.getHas_video().equals("True")) {

                post_image.setVisibility(View.GONE);
                relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);

                videoId = getYoutubeVideoId(commentsModel.getVideo_url());

                Log.e("VideoId is->", "" + videoId);

                String img_url = "http://img.youtube.com/vi/" + videoId + "/0.jpg"; // this is link which will give u thumnail image of that video

                // picasso jar file download image for u and set image in imagview

                Picasso.with(mContext)
                        .load(img_url)
                        .placeholder(R.color.black)
                        .into(img_thumbnail);

            } else if (commentsModel.getHas_img().equals("True")) {

                Picasso.with(mContext).cancelRequest(post_image);
                Picasso.with(mContext)
                        .load(R2Values.Web.BASE_URL + commentsModel.getImg_url())
                        .placeholder(R.drawable.default_post_image) // optional
                        .error(R.drawable.default_post_image)         // optional
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                //Set it in the ImageView
                                post_image.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }
                        });
            } else {
                post_image.setVisibility(View.GONE);
            }

            post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Intent i = new Intent(mContext, FullImageActivity.class);
                    i.putExtra("imageName", commentsModel.getImg_url());
                    mContext.startActivity(i);
                }
            });


        } else {

            if (commentsModel.getStudent_name() != null)
                txtstudent_name.setText(commentsModel.getStudent_name());

            if (commentsModel.getComment_text() != null)
                comment_text.setText(commentsModel.getComment_text());

            if (commentsModel.getComment_time() != null)
                comment_created_at.setText(commentsModel.getComment_time());

            if (commentsModel.getStudent_image() != null) {

                Picasso.with(mContext).cancelRequest(comment_user_img);
                Picasso.with(mContext)
                        .load(R2Values.Web.BASE_URL + commentsModel.getStudent_image())
                        .placeholder(R.drawable.default_image) // optional
                        .error(R.drawable.default_image)         // optional
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                //Set it in the ImageView
                                comment_user_img.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                            }
                        });
            }
        }

        imglike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imglike.setEnabled(false);
                LikePost(position);
            }
        });

        imgbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookmarkPost(position);
            }
        });

        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    //    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = R2Values.Web.BASE_URL + "share_post/" + commentsModel.getPost_id();
                    //    sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    mContext.startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }

            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //       mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=Fm2S4fvL8J0")));
                try {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, "AIzaSyASo9rv7zaO2cRpz0l37rQIQeAzrBloj8w", videoId);
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Something is wrong with the youtube url.", Toast.LENGTH_SHORT).show();
                        throw new ActivityNotFoundException();
                    }
                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(mContext, "YouTube app is not installed. Opening in browser...", Toast.LENGTH_SHORT).show();
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                    mContext.startActivity(webIntent);
                }
            }
        });

        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public void BookmarkPost(final int position) {

        LikeModel likeModel = new LikeModel();
        likeModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        likeModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        likeModel.setPost_id(post_id);

        String dataString = gson.toJson(likeModel, LikeModel.class);
        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;

        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.ToggleBookmark.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "Bookmark Post response  ---------------" + response);
                try {

                    JSONObject jsonobj = response.getJSONObject("toggle_bookmark");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        if (jsonobj.getString("message").contentEquals(mContext.getResources().getString(R.string.bookmark_added))) {
                            commentsList.get(position).setIs_bookmarked("1");
                        } else {
                            commentsList.get(position).setIs_bookmarked("0");
                        }

                        notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        App.getInstance().addToRequestQueue(request, "BookmarkPost");

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    public void LikePost(final int position) {

        StringRequest request = new StringRequest(Request.Method.POST, R2Values.Web.LikePost.SERVICE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Nonstop", "Like Post response  ---------------" + response);
                try {

                    JSONObject obj = new JSONObject(response);

                    JSONObject jsonobj = obj.getJSONObject("like_post");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        imglike.setEnabled(true);

                        int flag_count = 0;

                        if (jsonobj.getString("message").contentEquals(mContext.getResources().getString(R.string.post_liked))) {

                            flag_count = Integer.parseInt(commentsList.get(position).getLike_count().toString()) + 1;
                            commentsList.get(position).setLike_count(String.valueOf(flag_count));
                            commentsList.get(position).setIs_liked("1");
                            like_count = flag_count;

                        } else {
                            flag_count = Integer.parseInt(commentsList.get(position).getLike_count().toString()) - 1;
                            commentsList.get(position).setLike_count(String.valueOf(flag_count));
                            commentsList.get(position).setIs_liked("0");
                        }

                        notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                LikeModel likeModel = new LikeModel();
                likeModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
                likeModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
                likeModel.setPost_id(post_id);

                String dataString = gson.toJson(likeModel, LikeModel.class);

                Log.d("Nonstopandroid", dataString);

                params.put(R2Values.Web.LikePost.DATA, dataString);

                return params;
            }
        };

        App.getInstance().addToRequestQueue(request, "LikePost");

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

    }
}

