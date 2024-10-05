package com.erudito.main_stay.models;

/**
 * Created by nonstop on 9/2/18.
 */

public class ApplicationModel {
    private String topic_id;
    private String score;
    private String status;
    private String title;
    private String created_at;
    private String topic_description;

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTopic_desc() {
        return topic_description;
    }

    public void setTopic_desc(String topic_desc) {
        this.topic_description = topic_desc;
    }
}


