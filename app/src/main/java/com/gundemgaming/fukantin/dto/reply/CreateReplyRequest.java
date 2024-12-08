package com.gundemgaming.fukantin.dto.reply;

public class CreateReplyRequest {

    private String reply;
    private Long userId;
    private Long postId;

    public CreateReplyRequest() {
    }

    public CreateReplyRequest(String reply, Long userId, Long postId) {
        this.reply = reply;
        this.userId = userId;
        this.postId = postId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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
