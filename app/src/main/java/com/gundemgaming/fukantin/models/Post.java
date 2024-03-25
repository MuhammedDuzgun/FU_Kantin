package com.gundemgaming.fukantin.models;

public class Post {

    private int postId;
    private int categoryId;
    private String title;
    private String post;
    private int userId;
    private String username;
    private String date;
    private String numberOfReplies;

    public Post(int postId, int categoryId, String title, String post, int userId, String username, String date, String numberOfReplies) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.title = title;
        this.post = post;
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.numberOfReplies = numberOfReplies;
    }

    public Post(int postId, int categoryId, String title, String post, int userId, String username, String date) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.title = title;
        this.post = post;
        this.userId = userId;
        this.username = username;
        this.date = date;
    }

    public Post(int postId, int categoryId, String title, String post, int userId, String username) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.title = title;
        this.post = post;
        this.userId = userId;
        this.username = username;
    }

    public Post(int postId, String title, String post, int userId, String username) {
        this.postId = postId;
        this.title = title;
        this.post = post;
        this.userId = userId;
        this.username = username;
    }

    public Post(int postId, String title, String post, String username, String date, String numberOfReplies) {
        this.postId = postId;
        this.title = title;
        this.post = post;
        this.username = username;
        this.date = date;
        this.numberOfReplies = numberOfReplies;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumberOfReplies() {
        return numberOfReplies;
    }

    public void setNumberOfReplies(String numberOfReplies) {
        this.numberOfReplies = numberOfReplies;
    }
}
