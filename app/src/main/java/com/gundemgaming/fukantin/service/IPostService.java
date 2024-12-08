package com.gundemgaming.fukantin.service;

import com.gundemgaming.fukantin.dto.post.CreatePostRequest;
import com.gundemgaming.fukantin.dto.post.PostResponse;

import java.util.List;

public interface IPostService {

    PostResponse addPost(CreatePostRequest createPostRequest);

    List<PostResponse> getAllPosts();

    List<PostResponse> getUsersPost(Long userId);

    List<PostResponse> getPostsByCategory(Long categoryId);

    void deletePost(Long postId);

}
