package com.android.main_stay.models;

/**
 * Created by nonstop on 4/11/17.
 */

public class AddPostModel {
    private String email;
    private String password;
    private String image_url;
    private Boolean has_img;
    private String video_url;
    private Boolean has_video;
    private String text;
    private String posted_by;

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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public Boolean getHas_img() {
        return has_img;
    }

    public void setHas_img(Boolean has_img) {
        this.has_img = has_img;
    }

    public Boolean getHas_video() {
        return has_video;
    }

    public void setHas_video(Boolean has_video) {
        this.has_video = has_video;
    }
}
