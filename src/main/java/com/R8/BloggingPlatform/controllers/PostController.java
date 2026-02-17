package com.R8.BloggingPlatform.controllers;

import com.R8.BloggingPlatform.domain.CreatePostRequest;
import com.R8.BloggingPlatform.domain.UpdatePostRequest;
import com.R8.BloggingPlatform.domain.dtos.CreatePostRequestDto;
import com.R8.BloggingPlatform.domain.dtos.PostDto;
import com.R8.BloggingPlatform.domain.dtos.UpdatePostRequestDto;
import com.R8.BloggingPlatform.domain.entites.Post;
import com.R8.BloggingPlatform.domain.entites.User;
import com.R8.BloggingPlatform.mappers.PostMapper;
import com.R8.BloggingPlatform.services.PostService;
import com.R8.BloggingPlatform.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) UUID categoryId,@RequestParam(required = false) UUID tagId){
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto, @RequestAttribute UUID userId){

        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPost(loggedInUser,createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable UUID id, @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto){
        //System.out.println("New Body:"+updatePostRequestDto);
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        //System.out.println("New Body Sent To Service:"+updatePostRequest);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto updatedPostDto = postMapper.toDto(updatedPost);
        //System.out.println("Final Result:"+updatedPostDto);
        return ResponseEntity.ok(updatedPostDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id){
        Post post = postService.getPost(id);
        //System.out.println("In Controller : post "+post.getStatus());
        PostDto postDto = postMapper.toDto(post);
        //postDto.setStatus(post.getStatus());
        //System.out.println("In Controller : postDto "+postDto);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }



}
