package com.erudito.mainstaypeople.models;

/**
 * Created by nonstop on 23/1/18.
 */

public class QuizAnswers {
    private String question_id;
    private String answer_text;
    private String seen;
    private String attempt;

    public QuizAnswers(String question_id, String answer_text, String seen, String attempt) {
        this.question_id = question_id;
        this.answer_text = answer_text;
        this.seen = seen;
        this.attempt = attempt;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }
}
