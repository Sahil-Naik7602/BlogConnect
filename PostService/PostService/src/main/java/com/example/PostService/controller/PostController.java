package com.example.PostService.controller;

import com.example.PostService.context.UserContextHolder;
import com.example.PostService.dto.PostRequestDto;
import com.example.PostService.dto.PostResponseDto;
import com.example.PostService.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto){
        Long userId = UserContextHolder.getUserId();
        PostResponseDto postResponseDto = postService.createPost(postRequestDto,userId);
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId){
        PostResponseDto postResponseDto = postService.getPost(postId);
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostResponseDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostResponseDto> posts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }


}
