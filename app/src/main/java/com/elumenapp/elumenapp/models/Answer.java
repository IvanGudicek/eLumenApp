package com.elumenapp.elumenapp.models;

/**
 * Created by IvanGudiček on 7/25/2016.
 */
public class Answer {
    private String textOfAnswer;



    public Answer(String textOfAnswer) {
        this.textOfAnswer = textOfAnswer;
    }

    public String getTextOfAnswer() {
        return textOfAnswer;
    }

    public void setTextOfAnswer(String textOfAnswer) {
        this.textOfAnswer = textOfAnswer;
    }

    @Override
    public String toString() {
        return textOfAnswer;
    }
}



