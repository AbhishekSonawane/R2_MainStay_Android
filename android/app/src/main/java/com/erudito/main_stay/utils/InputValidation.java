package com.erudito.main_stay.utils;

/**
 * Created by nonstop on 7/7/16.
 */

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class InputValidation {

   /* private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z" +
            "0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";*/
   private static final String EMAIL_REGEX = "";

    private static final String PHONE_REGEX = "\\d{10}";
    private static final String NUMBER_REGEX = "\\d+";

    private static final String REQUIRED_MSG = "Required";
    private static final String NUMBER_MSG = "Required";
    private static final String EMAIL_MSG = "Invalid email";
    private static final String PHONE_MSG = "Enter 10 digit Phone Number";


    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    // call this method when you need to check  number validation
    public static boolean isNumber(EditText editText, boolean required) {
        return isValid(editText, NUMBER_REGEX, NUMBER_MSG, required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            editText.setBackgroundColor(Color.RED);
            return false;
        }

        if (!Pattern.matches(regex, text)) {
            if (text.contentEquals("")) {
                return true;
            } else {
                editText.setError(errMsg);
//                editText.setBackgroundColor(Color.RED);
                return false;
            }
        }

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
//            editText.setBackgroundColor(Color.RED);
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    public static boolean TxtviewhasText(TextView textView) {

        String text = textView.getText().toString().trim();
        textView.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
//            editText.setBackgroundColor(Color.RED);
            textView.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }


    public static boolean spnHasText(Spinner spner, String str) {

        if (spner != null) {
            if (spner.getSelectedItem() != null) {
                String text = spner.getSelectedItem().toString().trim();
//                TextView err = (TextView) spner.getSelectedView();
//                err.setError(null);
                // length 0 means there is no text
                Log.d("NonStop", "Spinner Text: " + text);
                if ((text.length() == 0 || text.contentEquals("Select")) && (spner.getVisibility() == View.VISIBLE)) {
                    Log.d("NonStop", "Returning False for " + str);
                    return false;
                } else {
                    return true;
                }
            } else {
                Log.d("NonStop", "Returning False since null for " + str);
                Toast.makeText(spner.getContext(), "Please select a value for "
                        + str, Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

}
