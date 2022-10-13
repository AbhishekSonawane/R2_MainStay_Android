package com.android.main_stay.Classes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.android.main_stay.R;
import com.android.main_stay.models.LoginModel;
import com.android.main_stay.utils.InputValidation;
import com.android.main_stay.utils.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ResetPassword extends AppCompatActivity {

    private PreferenceHelper mPreferenceHelper;
    private Gson gson;
    private Button btnsubmit;
    private EditText txtcurrentpwd, txtnewpassword, txtconfirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPreferenceHelper = new PreferenceHelper(this);
        gson = new Gson();
        btnsubmit = findViewById(R.id.btnsubmit);
        txtconfirmpassword = findViewById(R.id.txtconfirmpassword);
        txtcurrentpwd = findViewById(R.id.txtcurrentpwd);
        txtnewpassword = findViewById(R.id.txtnewpassword);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    if (txtnewpassword.getText().toString().equals(txtconfirmpassword.getText().toString())) {
                        ResetPassword();
                    } else {
                        Toasty.error(ResetPassword.this, getResources().getString(R.string.password_error), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void ResetPassword() {

        LoginModel loginModel = new LoginModel();
        loginModel.setEmail(mPreferenceHelper.getString(R2Values.Commons.EMAIL));
        loginModel.setPassword(txtcurrentpwd.getText().toString());
        loginModel.setNew_password(txtnewpassword.getText().toString());

        String dataString = gson.toJson(loginModel,LoginModel.class);
        JSONObject  jsonObject = new JSONObject();
        JSONObject dataStringnew = null;

        try {
            dataStringnew = new JSONObject(dataString);
            jsonObject.put("data",dataStringnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, R2Values.Web.ChangePassword.SERVICE_URL,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Nonstop", "Change Password response  ---------------" + response);
                try {

                    JSONObject jsonobj = response.getJSONObject("change_password");

                    if (jsonobj.getString("status").equals("fail")) {
                        Toasty.error(ResetPassword.this, getResources().getString(R.string.reset_password_error), Toast.LENGTH_LONG).show();
                    }

                    if (jsonobj.getString("status").equals("success")) {

                        mPreferenceHelper.addString(R2Values.Commons.PASSWORD, txtnewpassword.getText().toString());
                        Toasty.success(ResetPassword.this, jsonobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toasty.warning(ResetPassword.this, message, Toast.LENGTH_SHORT).show();
            }
        });

       App.getInstance().addToRequestQueue(request, "ResetPassword");

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
    }

    public boolean isValid() {

        if (!InputValidation.hasText(txtcurrentpwd)) {
            return false;
        }

        if (!InputValidation.hasText(txtnewpassword)) {
            return false;
        }

        return InputValidation.hasText(txtconfirmpassword);
    }
}
