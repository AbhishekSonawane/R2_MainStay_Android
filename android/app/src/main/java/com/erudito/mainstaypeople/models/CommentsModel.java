package com.erudito.mainstaypeople.models;

/**
 * Created by nonstop on 1/11/17.
 */

public class CommentsModel {

    private String comment_id;
    private String student_name;
    private String student_image;
    private String comment_text;
    private String post_id;
    private String video_url;
    private String like_count;
    private String text;
    private String creator_name;
    private String is_liked;
    private String creator_img;
    private String has_video;
    private String img_url;
    private String created_at;
    private String has_img;
    private String comment_count;
    private String is_bookmarked;
    private String comment_time;
    private String title;

/*   public  CommentsModel(String post_has_img,String post_creator_img,String post_like_count,String post_text,String  post_created_at,String post_img_url, String post_video_url,String post_has_video){

        this.post_has_img=post_has_img;
        this.post_creator_img=post_creator_img;
        this.post_like_count = post_like_count;
        this.post_text = post_text;
        this.post_created_at=post_created_at;
        this.post_img_url=post_img_url;
        this.post_video_url = post_video_url;
        this.post_has_video = post_has_video;
    }*/

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_image() {
        return student_image;
    }

    public void setStudent_image(String student_image) {
        this.student_image = student_image;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String getCreator_img() {
        return creator_img;
    }

    public void setCreator_img(String creator_img) {
        this.creator_img = creator_img;
    }

    public String getHas_video() {
        return has_video;
    }

    public void setHas_video(String has_video) {
        this.has_video = has_video;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHas_img() {
        return has_img;
    }

    public void setHas_img(String has_img) {
        this.has_img = has_img;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(String is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
