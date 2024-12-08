package com.gundemgaming.fukantin.dto.user;

import com.gundemgaming.fukantin.dto.post.PostResponse;
import com.gundemgaming.fukantin.dto.reply.ReplyResponse;

import java.util.List;

public class UserResponse {
    private Long id;
    private String username;
    private String department;
    private String instagramAddress;
    private String biography;
    private List<PostResponse> posts;
    private List<ReplyResponse> replies;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String department, String instagramAddress, String biography, List<PostResponse> posts, List<ReplyResponse> replies) {
        this.id = id;
        this.username = username;
        this.department = department;
        this.instagramAddress = instagramAddress;
        this.biography = biography;
        this.posts = posts;
        this.replies = replies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInstagramAddress() {
        return instagramAddress;
    }

    public void setInstagramAddress(String instagramAddress) {
        this.instagramAddress = instagramAddress;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }

    public List<ReplyResponse> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyResponse> replies) {
        this.replies = replies;
    }
}
