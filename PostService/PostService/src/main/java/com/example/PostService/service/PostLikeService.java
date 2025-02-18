package com.example.PostService.service;

import com.example.PostService.context.UserContextHolder;
import com.example.PostService.entity.Post;
import com.example.PostService.entity.PostLike;
import com.example.PostService.event.PostLikedEvent;
import com.example.PostService.exception.BadRequestException;
import com.example.PostService.exception.ResourceNotFoundException;
import com.example.PostService.repository.PostLikeRepository;
import com.example.PostService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId) {
        Long userId = UserContextHolder.getUserId();
        log.info("User {} liked post: {}",userId,postId);
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post not found with id: "+postId));
        if (postLikeRepository.existsByPostIdAndUserId(postId,userId)){
            throw new BadRequestException("User "+userId+" already liked post "+postId);
        }
        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();
        kafkaTemplate.send("post-liked-topic",postId,postLikedEvent);
    }

    @Transactional
    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getUserId();
        log.info("User {} disliked post: {}",userId,postId);
        if (!postLikeRepository.existsByPostIdAndUserId(postId,userId)){
            throw new BadRequestException("User "+userId+" never liked post "+postId);
        }
        postLikeRepository.deleteByPostIdAndUserId(postId,userId);
    }
}
