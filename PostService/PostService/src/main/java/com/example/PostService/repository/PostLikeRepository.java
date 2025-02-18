package com.example.PostService.repository;

import com.example.PostService.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    public boolean existsByPostIdAndUserId(Long postId, Long userId);

    public void deleteByPostIdAndUserId(Long postId, Long userId);
}
