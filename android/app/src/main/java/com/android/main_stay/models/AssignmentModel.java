package com.android.main_stay.models;

/**
 * Created by nonstop on 25/1/18.
 */

public class AssignmentModel {

    private String assign_id;
    private String score;
    private String title;
    private String status;
    private String created_at;

    public String getAssign_id() {
        return assign_id;
    }

    public void setAssign_id(String assign_id) {
        this.assign_id = assign_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
