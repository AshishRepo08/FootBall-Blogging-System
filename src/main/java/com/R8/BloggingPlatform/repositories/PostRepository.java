package com.R8.BloggingPlatform.repositories;

import com.R8.BloggingPlatform.domain.PostStatus;
import com.R8.BloggingPlatform.domain.entites.Category;
import com.R8.BloggingPlatform.domain.entites.Post;
import com.R8.BloggingPlatform.domain.entites.Tag;
import com.R8.BloggingPlatform.domain.entites.User;
import com.R8.BloggingPlatform.services.PostService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);
    List<Post> findAllByStatus(PostStatus status);
    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
}
