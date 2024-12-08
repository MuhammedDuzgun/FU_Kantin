package com.gundemgaming.fukantin.dto.reply;

import java.time.LocalDateTime;

public class ReplyResponse {

    private Long id;
    private String reply;
    private LocalDateTime createdAt;
    private Long userId;
    private Long postId;

    public ReplyResponse() {
    }

    public ReplyResponse(Long id, String response, LocalDateTime createdAt, Long userId, Long postId) {
        this.id = id;
        this.reply = response;
        this.createdAt = createdAt;
        this.userId = userId;
        this.postId = postId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

}
