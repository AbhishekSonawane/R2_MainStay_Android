package com.erudito.mainstaypeople.models;

/**
 * Created by nonstop on 19/1/18.
 */

public class GetQuestionsModel {

    private String op_a;
    private String op_b;
    private String question_id;
    private String question_text;
    private String question_type;
    private String op_d;
    private String op_c, student_ans, correct_ans;

    public String getOp_a() {
        return op_a;
    }

    public void setOp_a(String op_a) {
        this.op_a = op_a;
    }

    public String getOp_b() {
        return op_b;
    }

    public void setOp_b(String op_b) {
        this.op_b = op_b;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getOp_d() {
        return op_d;
    }

    public void setOp_d(String op_d) {
        this.op_d = op_d;
    }

    public String getOp_c() {
        return op_c;
    }

    public void setOp_c(String op_c) {
        this.op_c = op_c;
    }

    public String getStudent_ans() {
        return student_ans;
    }

    public void setStudent_ans(String student_ans) {
        this.student_ans = student_ans;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }
}
