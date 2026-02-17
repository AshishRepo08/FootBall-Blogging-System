package com.R8.BloggingPlatform.services;

import com.R8.BloggingPlatform.domain.CreatePostRequest;
import com.R8.BloggingPlatform.domain.UpdatePostRequest;
import com.R8.BloggingPlatform.domain.entites.Post;
import com.R8.BloggingPlatform.domain.entites.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);

    Post getPost(UUID id);
    void deletePost(UUID id);
}
