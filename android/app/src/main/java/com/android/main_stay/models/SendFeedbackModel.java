package com.android.main_stay.models;

/**
 * Created by nonstop on 7/11/17.
 */

public class SendFeedbackModel {

    private String email;
    private String password;
    private String text;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
