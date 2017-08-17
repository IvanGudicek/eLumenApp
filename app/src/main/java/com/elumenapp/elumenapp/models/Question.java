package com.elumenapp.elumenapp.models;


import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private String text;
    private String explanation;
    private Subject subject;
    private List<Answer> answerList;


    public Question() {
    }

    public Question(String text, String explanation, Subject subject, List<Answer> answerList) {
        this.text = text;
        this.explanation = explanation;
        this.subject = subject;
        this.answerList = answerList;
    }

    public Question(int id, String text, String explanation, Subject subject, List<Answer> answerList) {
        this.id = id;
        this.text = text;
        this.explanation = explanation;
        this.subject = subject;
        this.answerList = answerList;
    }

    public Question copyQuestion(Question question) {
        return new Question(question.getId(), question.getText(), question.getExplanation(), question.getSubject(), question.getAnswerList());
    }

    public List<Answer> getCorrectAnswers(List<Answer> answers) {
        List<Answer> correctAnswers = new ArrayList<>();
        for (Answer answer : answers) {
            if (answer.isCorrect()) {
                correctAnswers.add(answer);
            }
        }
        return correctAnswers;
    }

    public List<Answer> getInCorrectAnswers(List<Answer> answers) {
        List<Answer> inCorrectAnswers = new ArrayList<>();
        for (Answer answer : answers) {
            if (!answer.isCorrect()) {
                inCorrectAnswers.add(answer);
            }
        }
        return inCorrectAnswers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", explanation='" + explanation + '\'' +
                ", subject=" + subject +
                ", answerList=" + answerList +
                '}';
    }
}

