package com.erudito.main_stay.Classes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.android.main_stay.R;
import com.erudito.main_stay.models.LoginModel;
import com.erudito.main_stay.utils.InputValidation;
import com.erudito.main_stay.utils.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import es.dmoral.toasty.Toasty;
import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class Login extends AppCompatActivity {

    private Button sign_in_button;
    private Gson gson;
    private PreferenceHelper mPreferenceHelper;
    private EditText txtemail, txtpassword;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog pd;
    private TextView txttandc, txtforgotpassword;
    private String uGoogRegId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String PACKAGE_NAME = getApplicationContext().getPackageName();
        Log.e("packaqe name", PACKAGE_NAME);


        init();

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (true) {
                    pd.show();
                    Login();

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                }
            }
        });
    }

    private void init() {

        gson = new Gson();
        mPreferenceHelper = new PreferenceHelper(Login.this);
        sign_in_button = findViewById(R.id.sign_in_button);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);
        txttandc = findViewById(R.id.txttandc);
        txtforgotpassword = findViewById(R.id.txtforgotpassword);

        pd = new ProgressDialog(Login.this, R.style.MyTheme);
        //  pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        txtforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uriText =
                        "mailto:rajiv@rsquareconsult.in" +
                                "?subject=" + Uri.encode("Regarding Main Stay App - forgot password") +
                                "&body=" + Uri.encode("Hi, I want to use the Main Stay app, but I have forgotten my password. Kindly send me password.");

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(uri);
                startActivity(sendIntent);

            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseAnalytics.setCurrentScreen(this, "Loginscreen", null /* class override */);

        txttandc.setText(Html.fromHtml("By signing in,you agree to our <b> Terms and Conditions </b>"));

    }

    public void Login() {

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(txtemail.getText().toString());
        loginModel.setPassword(txtpassword.getText().toString());
        loginModel.setDevice_type("Android");
        loginModel.setOs_version(Build.VERSION.RELEASE);
        loginModel.setApp_version(getCurrentVersion());
        loginModel.setDevice_token(mPreferenceHelper.getString(R2Values.Commons.TOKEN));

        String dataString = gson.toJson(loginModel);

        JSONObject jsonObject = new JSONObject();
        JSONObject dataStringnew = null;
        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data", dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  final String requestBody  = jsonObject.toString();

//        final String requestBody = "{\n" +
//                "    \"data\": {\n" +
//                "        \"email\": \"abhishek@iarianatech.com\",\n" +
//                "        \"password\": \"Abhi@new123\",\n" +
//                "        \"device_type\": \"iOS\",\n" +
//                "        \"app_version\": \"1\",\n" +
//                "        \"os_version\": \"15\",\n" +
//                "        \"device_token\": \"csaade3eweewweqfewrerre34424234423\"\n" +
//                "    }\n" +
//                "}";
//
//        JSONObject jsonObject1 = null;
//        try {
//             jsonObject1 = new JSONObject(requestBody);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.LoginService.SERVICE_URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", "" + response);

                if (pd != null && pd.isShowing())
                    if (pd != null && pd.isShowing())
                        try {
                            pd.dismiss();
                        } catch (Exception e) {

                        }
                try {

                    JSONObject jsonobj = response.getJSONObject("login");

                    if (jsonobj.getString("status").equals("fail")) {
                        Toasty.error(Login.this, jsonobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        JSONObject dataobj = jsonobj.getJSONObject("data");

                        mPreferenceHelper.addBoolean(R2Values.Commons.ISUSER_LOGGEDIN, true);
                        mPreferenceHelper.addString(R2Values.Commons.EMAIL, txtemail.getText().toString().trim());
                        mPreferenceHelper.addString(R2Values.Commons.PASSWORD, txtpassword.getText().toString().trim());
                        mPreferenceHelper.addString(R2Values.Commons.STUDENT_ID, dataobj.getString("student_id"));

                        Intent intent = new Intent(Login.this, TabActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    Log.e("Error", volleyError.getMessage());
                }catch (Exception e){

                }


                if (pd != null && pd.isShowing());
                   pd.dismiss();

                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof ServerError) {
                   message = "The server could not be found. Please try again after some time!!";
                }else if (volleyError instanceof AuthFailureError) {
                   message = getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = getResources().getString(R.string.internet_connection_error);
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toasty.warning(Login.this, message, Toast.LENGTH_SHORT).show();


            }
        });

        App.getInstance().addToRequestQueue(request, "Login");
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    public boolean isValid() {

        if (!InputValidation.hasText(txtemail)) {
            return false;
        }

        if (!InputValidation.hasText(txtpassword)) {
            return false;
        }

        return InputValidation.isEmailAddress(txtemail, false);
    }

    private String getCurrentVersion() {
        PackageManager pm = Login.this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(Login.this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;

        return currentVersion;
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {
                // Register the device for notifications
                String deviceToken = Pushy.register(getApplicationContext());
                uGoogRegId = deviceToken;
                Log.d("MyApp", "Pushy device token: " + deviceToken);
                // Send the token to your backend server via an HTTP GET request
                new URL("https://52.206.254.98/register/device?token=" + deviceToken).openConnection();
            } catch (PushyException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("NonStop","Device registration successful");
            // Success
            return null;
        }
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mPreferenceHelper.addString(R2Values.Commons.TOKEN, uGoogRegId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NonStop", "In Login onResume");

        new RegisterForPushNotificationsAsync().execute();
    }


}
