package com.elumenapp.elumenapp.quiz.com;

/**
 * Created by IvanGudiček on 7/25/2016.
 */
public class Question {
    private String question;
    private boolean check;

    public Question(String question, boolean check) {
        this.question = question;
        this.check = check;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return question;
    }
}

