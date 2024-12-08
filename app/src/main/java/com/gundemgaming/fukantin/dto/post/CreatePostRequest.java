package com.gundemgaming.fukantin.dto.post;

public class CreatePostRequest {

    private Long userId;
    private Long categoryId;
    private String title;
    private String post;

    public CreatePostRequest() {
    }

    public CreatePostRequest(Long user_id, Long category_id, String title, String post) {
        this.userId = user_id;
        this.categoryId = category_id;
        this.title = title;
        this.post = post;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
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

}
