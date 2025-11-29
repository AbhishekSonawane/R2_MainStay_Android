package com.erudito.mainstaypeople.models;

/**
 * Created by nonstop on 27/11/17.
 */

public class NotificationsModel {

    private String notif_img;
    private String date;
    private String text;
    private String redirect_id;
    private String read;
    private String redirect_to;
    private String notif_id;
    private String email;
    private String password;

    public String getNotif_img() {
        return notif_img;
    }

    public void setNotif_img(String notif_img) {
        this.notif_img = notif_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRedirect_id() {
        return redirect_id;
    }

    public void setRedirect_id(String redirect_id) {
        this.redirect_id = redirect_id;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getRedirect_to() {
        return redirect_to;
    }

    public void setRedirect_to(String redirect_to) {
        this.redirect_to = redirect_to;
    }

    public String getNotif_id() {
        return notif_id;
    }

    public void setNotif_id(String notif_id) {
        this.notif_id = notif_id;
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
