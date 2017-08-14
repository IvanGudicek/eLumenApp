package com.elumenapp.elumenapp.models;

/**
 * Created by igudicek on 14.8.2017..
 */

public class User {
    private int id;
    private String facebookId;
    private String firstName;
    private String lastName;
    private String fullName;

    public User() {
    }

    public User(String facebookId, String firstName, String lastName, String fullName) {
        this.facebookId = facebookId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
    }

    public User(int id, String facebookId, String firstName, String lastName, String fullName) {
        this.id = id;
        this.facebookId = facebookId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", facebookId='" + facebookId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
