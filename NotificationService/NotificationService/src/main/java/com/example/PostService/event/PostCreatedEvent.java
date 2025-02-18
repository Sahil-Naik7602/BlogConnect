package com.example.PostService.event;

import lombok.Builder;
import lombok.Data;

@Data
public class PostCreatedEvent {
    private Long userId;
    private Long postId;
    private String content;
}
