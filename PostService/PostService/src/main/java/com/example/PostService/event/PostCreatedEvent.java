package com.example.PostService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreatedEvent {
    private Long userId;
    private Long postId;
    private String content;
}
