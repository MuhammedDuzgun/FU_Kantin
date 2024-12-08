package com.gundemgaming.fukantin.dto.post;

import com.gundemgaming.fukantin.dto.reply.ReplyResponse;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {

    private Long id;
    private String title;
    private String post;
    private LocalDateTime createdAt;
    private Long userId;
    private List<ReplyResponse> replies;
    private Long categoryId;

    public PostResponse() {
    }

    public PostResponse(Long id, String title, String post, LocalDateTime createdAt, Long userId, List<ReplyResponse> replies, Long categoryId) {
        this.id = id;
        this.title = title;
        this.post = post;
        this.createdAt = createdAt;
        this.userId = userId;
        this.replies = replies;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ReplyResponse> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyResponse> replies) {
        this.replies = replies;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
