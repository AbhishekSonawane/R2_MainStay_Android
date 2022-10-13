package com.android.main_stay.models;

/**
 * Created by nonstop on 2/1/18.
 */

public class Quiz {

    private String quiz_created_at;
    private String quiz_desc;
    private String quiz_status;
    private String score;
    private String quiz_id;
    private String quiz_rating;
    private String email;
    private String password;

    public String getQuiz_created_at() {
        return quiz_created_at;
    }

    public void setQuiz_created_at(String quiz_created_at) {
        this.quiz_created_at = quiz_created_at;
    }

    public String getQuiz_desc() {
        return quiz_desc;
    }

    public void setQuiz_desc(String quiz_desc) {
        this.quiz_desc = quiz_desc;
    }

    public String getQuiz_status() {
        return quiz_status;
    }

    public void setQuiz_status(String quiz_status) {
        this.quiz_status = quiz_status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_rating() {
        return quiz_rating;
    }

    public void setQuiz_rating(String quiz_rating) {
        this.quiz_rating = quiz_rating;
    }

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
}
