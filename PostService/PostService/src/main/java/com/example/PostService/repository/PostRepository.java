package com.example.PostService.repository;

import com.example.PostService.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    public List<Post> findByUserId(Long userId);
}
