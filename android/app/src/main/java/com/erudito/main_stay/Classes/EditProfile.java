package com.erudito.main_stay.Classes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.android.main_stay.R;
import com.erudito.main_stay.models.LoginModel;
import com.erudito.main_stay.models.UpdateUserProfileModel;
import com.erudito.main_stay.utils.PreferenceHelper;
import com.erudito.main_stay.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class EditProfile extends AppCompatActivity {

    private TextView student_name, txtemail;
    private ImageView imgprofilepic, img_background;
    private static final int PICK_FROM_CAMERA = 1888;
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean fromImageSel = false;
    static Bitmap src;
    static byte[] byteArray;
    private String encodedImage;
    public static byte[] imageBA;
    ProgressDialog pd;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private Button btnsubmit;
    private EditText txtname, txtlastname;
    private String new_password;
    private TextView txtpassword;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPreferenceHelper = new PreferenceHelper(EditProfile.this);
        gson = new Gson();
        imgprofilepic = (CircularImageView) findViewById(R.id.uploadprofilepic);
        student_name = findViewById(R.id.student_name);
        img_background = findViewById(R.id.img_background);
        btnsubmit = findViewById(R.id.btnsubmit);
        txtemail = findViewById(R.id.txtemail);
        txtname = findViewById(R.id.txtname);
        txtpassword = findViewById(R.id.txtpassword);
        txtlastname = findViewById(R.id.txtlastname);

        fromImageSel = false;

        pd = new ProgressDialog(EditProfile.this, R.style.MyTheme);
        //  pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        GetProfileInfo();

        txtemail.setText(mPreferenceHelper.getString(R2Values.Commons.EMAIL));

        imgprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromImageSel = true;
                verifyStoragePermissions(EditProfile.this);
                Log.d("NS", "OnClick");
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                UpdateUserProfileService();
            }
        });

        txtpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, ResetPassword.class);
                startActivity(intent);

            }
        });

        BitmapDrawable drawable = (BitmapDrawable) imgprofilepic.getDrawable();
        src = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
        imageBA = stream.toByteArray();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (fromImageSel) {
            Log.d("NonStop", "In from Image Selection");
            fromImageSel = false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = Utils.getPath(getApplicationContext(), selectedImageUri);
                //   String selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                Bitmap bmp = decodeUri(selectedImageUri);
                if (bmp != null) {
                    src = getResizedBitmap(bmp);
                    imgprofilepic.setImageBitmap(src);
                    try {
                        encodedImage = encodeTobase64(src);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (requestCode == PICK_FROM_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                src = getResizedBitmap(bitmap);
                imgprofilepic.setImageBitmap(src);
                try {
                    encodedImage = encodeTobase64(src);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new
                            Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                            SELECT_PICTURE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private Bitmap decodeUri(Uri selectedImage) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 400;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeTobase64_2(Bitmap image) throws IOException {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.NO_WRAP);
        baos.close();
        System.out.println("LOOK" + imageEncoded);
        //return imageEncoded;
        return b;
    }

    public static String encodeTobase64(Bitmap image) throws IOException {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        imageBA = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        baos.close();
        System.out.println("Encoded image string: " + imageEncoded);
        Log.d("NonStop", "Encoded image string: " + imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static Bitmap getResizedBitmap(Bitmap bm) {
        int newWidth = 350;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float aspectRatio = width / (float) height;
        int scaleHeight = Math.round(newWidth / aspectRatio);
        System.out.println("Width=" + newWidth + "height=" + scaleHeight);
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(newWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, scaleHeight, false);
        bm.recycle();
        return resizedBitmap;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    //permission method.
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UpdateUserProfileService() {

        UpdateUserProfileModel updateUserProfileModel = new UpdateUserProfileModel();
        updateUserProfileModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        updateUserProfileModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));
        updateUserProfileModel.setFirst_name(txtname.getText().toString());
        updateUserProfileModel.setLast_name(txtlastname.getText().toString());
        updateUserProfileModel.setPicture_url(Base64.encodeToString(imageBA, Base64.DEFAULT));//encodedImage);


        String dataString = gson.toJson(updateUserProfileModel, UpdateUserProfileModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.POST, R2Values.Web.UpdateProfile.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("Nonstop", "response  ---------------" + response);

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    JSONObject updateobj = response.getJSONObject("update_profile");

                    if (updateobj.getString("status").equals("fail")) {
                        Toasty.error(EditProfile.this, updateobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    if (updateobj.getString("status").equals("success")) {
                        Toasty.success(EditProfile.this, updateobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toasty.warning(EditProfile.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "updateprofile");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    public void GetProfileInfo() {

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        loginModel.setPassword(mPreferenceHelper.getString(R2Values.Commons.PASSWORD));

        String dataString = gson.toJson(loginModel, LoginModel.class);

        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.GetProfileInfo.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "response  ---------------" + response);
                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {


                    JSONObject jsonobj = response.getJSONObject("get_profile_info");

                    if (jsonobj.getString("status").equals("fail")) {
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        JSONObject dataobj = jsonobj.getJSONObject("data");

                        Log.d("Pooja profile data", dataobj.toString());

                        if (dataobj.getString("student_first_name") != null)
                            student_name.setText(dataobj.getString("student_first_name") + " " + dataobj.getString("student_last_name"));
                        txtname.setText(dataobj.getString("student_first_name"));

                        if (dataobj.getString("student_last_name") != null)
                            txtlastname.setText(dataobj.getString("student_last_name"));

                        if (dataobj.getString("student_picture_url") != null) {

                            Picasso.with(getApplicationContext()).cancelRequest(img_background);
                            Picasso.with(getApplicationContext())
                                    .load(R2Values.Web.BASE_URL + dataobj.getString("company_image"))
                                    //*.placeholder(R.drawable.default_post_image) // optional
                                    //  .error(R.drawable.default_post_image)         // optional*//*
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                            //Set it in the ImageView
                                            img_background.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {

                                        }



                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }


                                    });
                        }

                        if (dataobj.getString("student_picture_url") != null) {
                            Picasso.with(EditProfile.this).cancelRequest(imgprofilepic);
                            Picasso.with(EditProfile.this)
                                    .load(R2Values.Web.BASE_URL + dataobj.getString("student_picture_url"))
                                    .placeholder(R.drawable.default_image) // optional
                                    .error(R.drawable.default_image)         // optional
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                            src = bitmap;
                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            src.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                            byteArray = stream.toByteArray();
                                            imageBA = stream.toByteArray();
                                            //Set it in the ImageView
                                            imgprofilepic.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {

                                        }



                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }


                                    }); //imgprofilepic);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();

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
                Toasty.warning(EditProfile.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        App.getInstance().addToRequestQueue(request, "GetProfileInfo");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }
}
