package com.elumenapp.elumenapp.quiz.com;

/**
 * Created by IvanGudiček on 7/25/2016.
 */
public class Explanation {
    private String explanation;

    public Explanation(String explanation){
        this.explanation = explanation;
    }

    public void setExplanation(String explanation){
        this.explanation = explanation;
    }
    public String getExplanation(){
        return explanation;
    }

    @Override
    public String toString(){
        return explanation;
    }
}
