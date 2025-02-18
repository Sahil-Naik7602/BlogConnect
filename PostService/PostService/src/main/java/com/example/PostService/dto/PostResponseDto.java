package com.example.PostService.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;

}
