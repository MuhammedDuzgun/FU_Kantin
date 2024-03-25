package com.gundemgaming.fukantin.models;

public class User {

    private int userId;
    private String username;
    private String password;
    private String biography;
    private int userDetailId;

    public User(int userId, String username, String password, String biography) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.biography = biography;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int userId, int userDetailId) {
        this.userId = userId;
        this.userDetailId = userDetailId;
    }

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(int userDetailId) {
        this.userDetailId = userDetailId;
    }
}
