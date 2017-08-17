package com.elumenapp.elumenapp.models;

import java.math.BigDecimal;

public class Score {
    private int id;
    private BigDecimal score;
    private int roundNumber;
    private User user;
    private Subject subject;

    public Score() {
    }

    public Score(BigDecimal score, int roundNumber, User user, Subject subject) {
        this.score = score;
        this.roundNumber = roundNumber;
        this.user = user;
        this.subject = subject;
    }

    public Score(int id, BigDecimal score, int roundNumber, User user, Subject subject) {
        this.id = id;
        this.score = score;
        this.roundNumber = roundNumber;
        this.user = user;
        this.subject = subject;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", score=" + score +
                ", roundNumber=" + roundNumber +
                ", user=" + user +
                ", subject=" + subject +
                '}';
    }
}
