package com.erudito.main_stay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.erudito.main_stay.Classes.App;
import com.erudito.main_stay.Classes.Comments;
import com.erudito.main_stay.Classes.R2Values;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.gson.Gson;
import com.android.main_stay.R;
import com.erudito.main_stay.models.FeedsModel;
import com.erudito.main_stay.models.LikeModel;
import com.erudito.main_stay.utils.PreferenceHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.erudito.main_stay.utils.Utils.getYoutubeVideoId;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by nonstop on 11/12/17.
 */

public class BookmarksListAdapter extends RecyclerView.Adapter<BookmarksListAdapter.MyViewHolder> {

    private ArrayList<FeedsModel> FeedsList;
    BookmarksListAdapter.MyViewHolder.RecyclerViewClickListener listener;
    public static ImageView imglike, imgbookmark, imgshare, imgcomments;
    public static Gson gson;
    public static PreferenceHelper mPreferenceHelper;
    public static Context mContext;
    public static CardView cardview;
    public String post_id;
    static WebView webview;
    static TextView txtlike_count;
    static int like_count;
    static RelativeLayout relativeLayoutOverYouTubeThumbnailView;
    String videoId;
    static ImageView img_thumbnail, playButton;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView post_title, post_created_at, post_description, txtcommentscount, button_toggle;
        BookmarksListAdapter.MyViewHolder.RecyclerViewClickListener listener;
        ImageView post_image;

        public MyViewHolder(View view, BookmarksListAdapter.MyViewHolder.RecyclerViewClickListener listener) {
            super(view);
            post_title = view.findViewById(R.id.txttitle);
            post_created_at = view.findViewById(R.id.txtdate);
            post_description = view.findViewById(R.id.txtpostdescription);
            imglike = view.findViewById(R.id.imglike);
            imgbookmark = view.findViewById(R.id.imgbookmark);
            imgshare = view.findViewById(R.id.imgshare);
            gson = new Gson();
            mPreferenceHelper = new PreferenceHelper(mContext);
            post_image = view.findViewById(R.id.post_image);
            //   circularImageView = (ImageView) view.findViewById(R.id.circularImageView);
            txtcommentscount = view.findViewById(R.id.txtcommentscount);
            //  webview = (WebView) view.findViewById(R.id.webview);
            button_toggle = view.findViewById(R.id.button_toggle);
            txtlike_count = view.findViewById(R.id.txtlike_count);
            imgcomments = view.findViewById(R.id.imgcomments);
            relativeLayoutOverYouTubeThumbnailView = itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            img_thumbnail = view.findViewById(R.id.img_thumbnail);
            playButton = view.findViewById(R.id.btnYoutube_player);

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


    public BookmarksListAdapter(ArrayList<FeedsModel> FeedsList, Context mContext) {
        this.FeedsList = FeedsList;
        BookmarksListAdapter.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bookmarks_list, parent, false);

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        FeedsModel FeedsModel = FeedsList.get(position);

        post_id = FeedsModel.getPost_id();

        final String ImageUrl = R2Values.Web.BASE_URL + FeedsModel.getImg_url();

        if (FeedsModel.getTitle() != null)
            holder.post_title.setText(FeedsModel.getTitle());

        if (FeedsModel.getCreated_at() != null)
            holder.post_created_at.setText(FeedsModel.getCreated_at());

        if (FeedsModel.getText() != null) {
            holder.post_description.setText(Html.fromHtml(FeedsModel.getText()), TextView.BufferType.SPANNABLE);
            //  webview.loadDataWithBaseURL(null, FeedsModel.getText(), "text/html", "utf-8", null);
        }

        if (FeedsModel.getText().length() < 150) {
            holder.button_toggle.setVisibility(View.GONE);
        }

        if (FeedsModel.getComment_count() != null)
            holder.txtcommentscount.setText(FeedsModel.getComment_count());

        if (FeedsModel.getLike_count() != null)
            txtlike_count.setText(FeedsModel.getLike_count());

        if (FeedsModel.getIs_bookmarked() != null) {
            if (FeedsModel.getIs_bookmarked().contentEquals("1")) {
                imgbookmark.setImageDrawable(mContext.getDrawable(R.drawable.btn_bookmark_filled));
            } else {
                imgbookmark.setImageDrawable(mContext.getDrawable(R.drawable.btn_bookmark_unfilled));
            }
        }

        if (FeedsModel.getIs_liked() != null) {
            if (FeedsModel.getIs_liked().contentEquals("1")) {
                imglike.setImageDrawable(mContext.getDrawable(R.drawable.btn_like_filled));
            } else {
                imglike.setImageDrawable(mContext.getDrawable(R.drawable.btn_like_unfilled));
            }
        }

        if (FeedsModel.getHas_video().equals("True")) {
            holder.post_image.setVisibility(View.GONE);
            relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);

            videoId = getYoutubeVideoId(FeedsModel.getVideo_url());

            Log.e("VideoId is->", "" + videoId);

            String img_url = "http://img.youtube.com/vi/" + videoId + "/0.jpg"; // this is link which will give u thumnail image of that video

            // picasso jar file download image for u and set image in imagview

            Picasso.with(mContext)
                    .load(img_url)
                    .placeholder(R.color.black)
                    .into(img_thumbnail);
        } else if (FeedsModel.getHas_img().equals("True")) {

            Picasso.with(mContext).cancelRequest(holder.post_image);
            Picasso.with(mContext)
                    .load(ImageUrl)
                    .placeholder(R.drawable.default_post_image) // optional
                    .error(R.drawable.default_post_image)         // optional
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            //Set it in the ImageView
                            holder.post_image.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }
                    });
        } else {
            holder.post_image.setVisibility(View.GONE);
        }

        imglike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    String sAux = R2Values.Web.BASE_URL + "share_post/" + FeedsList.get(position).getPost_id();
                    //    sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    mContext.startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Comments.class);
                intent.putExtra("post_id", FeedsList.get(position).getPost_id());
                mContext.startActivity(intent);
            }
        });

        holder.button_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Comments.class);
                intent.putExtra("post_id", FeedsList.get(position).getPost_id());
                mContext.startActivity(intent);
            }
        });

        holder.post_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Comments.class);
                intent.putExtra("post_id", FeedsList.get(position).getPost_id());
                mContext.startActivity(intent);
            }
        });

        holder.post_created_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Comments.class);
                intent.putExtra("post_id", FeedsList.get(position).getPost_id());
                mContext.startActivity(intent);
            }
        });

        imgcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Comments.class);
                intent.putExtra("post_id", FeedsList.get(position).getPost_id());
                mContext.startActivity(intent);
            }
        });

        holder.txtcommentscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Comments.class);
                intent.putExtra("post_id", FeedsList.get(position).getPost_id());
                mContext.startActivity(intent);
            }
        });


        holder.post_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Comments.class);
                intent.putExtra("post_id", FeedsList.get(position).getPost_id());
                mContext.startActivity(intent);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //       mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=Fm2S4fvL8J0")));
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, "AIzaSyASo9rv7zaO2cRpz0l37rQIQeAzrBloj8w", videoId);
                mContext.startActivity(intent);
            }
        });

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return FeedsList.size();
    }

    public void LikePost(final int position) {

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.LikePost.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "Like Post response  ---------------" + response);
                try {

                    JSONObject jsonobj = response.getJSONObject("like_post");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        int flag_count = 0;

                        if (jsonobj.getString("message").contentEquals(mContext.getResources().getString(R.string.post_liked))) {

                            flag_count = Integer.parseInt(FeedsList.get(position).getLike_count().toString()) + 1;
                            FeedsList.get(position).setLike_count(String.valueOf(flag_count));
                            FeedsList.get(position).setIs_liked("1");
                            like_count = flag_count;

                        } else {
                            flag_count = Integer.parseInt(FeedsList.get(position).getLike_count().toString()) - 1;
                            FeedsList.get(position).setLike_count(String.valueOf(flag_count));
                            FeedsList.get(position).setIs_liked("0");
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

        App.getInstance().addToRequestQueue(request, "LikePost");

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));

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

                    Toasty.success(mContext, jsonobj.getString("message"), Toast.LENGTH_SHORT).show();

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        if (jsonobj.getString("message").contentEquals(mContext.getResources().getString(R.string.bookmark_added))) {
                            FeedsList.get(position).setIs_bookmarked("1");
                        } else {
                            FeedsList.get(position).setIs_bookmarked("0");
                            FeedsList.remove(position);
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
    /*public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
*/

}
