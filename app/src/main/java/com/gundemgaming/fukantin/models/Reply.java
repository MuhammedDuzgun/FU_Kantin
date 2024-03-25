package com.gundemgaming.fukantin.models;

public class Reply {

    private int replyId;
    private int postId;
    private int userId;
    private String reply;
    private String username;
    private String date;

    public Reply(int replyId, int postId, int userId, String reply, String username, String date) {
        this.replyId = replyId;
        this.postId = postId;
        this.userId = userId;
        this.reply = reply;
        this.username = username;
        this.date = date;
    }

    public Reply(int replyId, int postId, int userId, String reply, String username) {
        this.replyId = replyId;
        this.postId = postId;
        this.userId = userId;
        this.reply = reply;
        this.username = username;
    }

    public Reply(int replyId, int userId, String reply, String username) {
        this.replyId = replyId;
        this.userId = userId;
        this.reply = reply;
        this.username = username;
    }

    public Reply(int replyId, int userId, String reply, String username, String date) {
        this.replyId = replyId;
        this.userId = userId;
        this.reply = reply;
        this.username = username;
        this.date = date;
    }

    public Reply(int replyId, String reply, String username, String date) {
        this.replyId = replyId;
        this.reply = reply;
        this.username = username;
        this.date = date;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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
}
