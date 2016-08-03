package com.elumenapp.elumenapp.person.com;

import android.graphics.drawable.Drawable;

import java.math.BigDecimal;

/**
 * Created by IvanGudiček on 7/29/2016.
 */
public class Person {
    private String username;
    private String password;
    private String description;
    private String name;
    private String lastname;
    private String email;
    private Drawable drawable;
    private BigDecimal totalScore = new BigDecimal(0.00);


    public void setCurrentPerson(Person person){
        this.username = person.username;
        this.drawable = person.drawable;
        this.totalScore = person.totalScore;
        this.password = person.password;
        this.description = person.description;
        this.name = person.name;
        this.lastname = person.lastname;
        this.email = person.email;
    }

    public Person(String username, Drawable drawable, BigDecimal totalScore, String password, String description, String name, String lastname, String email) {
        this.username = username;
        this.drawable = drawable;
        this.totalScore = totalScore;
        this.password = password;
        this.description = description;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }
}
