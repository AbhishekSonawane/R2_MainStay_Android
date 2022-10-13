package com.android.main_stay.models;

/**
 * Created by nonstop on 3/11/17.
 */

public class BatchModel {

    private String email;
    private String password;
    private String batch_id;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
