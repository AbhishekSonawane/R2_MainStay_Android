package com.erudito.mainstaypeople.models;

/**
 * Created by nonstop on 27/1/18.
 */

public class SubmitAssignment {

    private String email;
    private String password;
    private String ans_text;
    private String assign_id;
    private String answer_seen;
    private String answer_attempt;

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

    public String getAns_text() {
        return ans_text;
    }

    public void setAns_text(String ans_text) {
        this.ans_text = ans_text;
    }

    public String getAssign_id() {
        return assign_id;
    }

    public void setAssign_id(String assign_id) {
        this.assign_id = assign_id;
    }

    public String getAnswer_seen() {
        return answer_seen;
    }

    public void setAnswer_seen(String answer_seen) {
        this.answer_seen = answer_seen;
    }

    public String getAnswer_attempt() {
        return answer_attempt;
    }

    public void setAnswer_attempt(String answer_attempt) {
        this.answer_attempt = answer_attempt;
    }
}
