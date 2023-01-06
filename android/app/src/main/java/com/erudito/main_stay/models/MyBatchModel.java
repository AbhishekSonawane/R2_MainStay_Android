package com.erudito.main_stay.models;

/**
 * Created by nonstop on 13/12/17.
 */

public class MyBatchModel {

    private String student_picture_url;
    private String student_first_name;
    private String student_last_name;

    public String getStudent_picture_url() {
        return student_picture_url;
    }

    public void setStudent_picture_url(String student_picture_url) {
        this.student_picture_url = student_picture_url;
    }

    public String getStudent_first_name() {
        return student_first_name;
    }

    public void setStudent_first_name(String student_first_name) {
        this.student_first_name = student_first_name;
    }

    public String getStudent_last_name() {
        return student_last_name;
    }

    public void setStudent_last_name(String student_last_name) {
        this.student_last_name = student_last_name;
    }
}
