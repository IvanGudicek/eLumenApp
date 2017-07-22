package com.elumenapp.elumenapp.quiz.com;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanGudiček on 7/25/2016.
 */
public class Entirety {
    private Question question;
    private List<Answer> correctAnswers = new ArrayList<>();
    private List<Answer> incorrectAnswers = new ArrayList<>();
    private Explanation explanation;


    public Entirety(Question question, Explanation explanation, List<Answer> correctAnswers, List<Answer> incorrectAnswers) {
        this.question = question;
        this.explanation = explanation;
        this.incorrectAnswers = incorrectAnswers;
        this.correctAnswers = correctAnswers;
    }

    public Entirety copyEntirety(Entirety entirety) {
        return new Entirety(entirety.question, entirety.explanation, entirety.correctAnswers, entirety.incorrectAnswers);
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Explanation getExplanation() {
        return explanation;
    }

    public void setExplanation(Explanation explanation) {
        this.explanation = explanation;
    }

    public List<Answer> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<Answer> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public List<Answer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Answer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public String toString() {
        return "Entirety: \nQuestion:\n" + question + "\n Correct answers:\n" + correctAnswers
                + "\n Incorrect answers: \n" + incorrectAnswers + "\n Explanation:\n" + explanation + "\n";
    }

}











