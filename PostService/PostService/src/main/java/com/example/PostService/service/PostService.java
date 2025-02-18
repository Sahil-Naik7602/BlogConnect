package com.example.PostService.service;

import com.example.PostService.clients.ConnectionClient;
import com.example.PostService.context.UserContextHolder;
import com.example.PostService.dto.PersonDto;
import com.example.PostService.dto.PostRequestDto;
import com.example.PostService.dto.PostResponseDto;
import com.example.PostService.entity.Post;
import com.example.PostService.event.PostCreatedEvent;
import com.example.PostService.exception.ResourceNotFoundException;
import com.example.PostService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionClient connectionClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostResponseDto createPost(PostRequestDto postRequestDto, long userId) {
        Post post = new Post();
        post.setContent(postRequestDto.getContent());
        post.setUserId(userId);
        Post savedPost  = postRepository.save(post);

        //Producing the data to Kafka into topic post-created-topic
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .content(postRequestDto.getContent())
                .userId(userId)
                .postId(savedPost.getId())
                .build();

        kafkaTemplate.send("post-created-topic",postCreatedEvent);

        return modelMapper.map(savedPost, PostResponseDto.class);
    }

    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("PostId: "+postId+ " doesn't exist"));
        return modelMapper.map(post, PostResponseDto.class);
    }

    public List<PostResponseDto> getAllPostsOfUser(Long userId) {

        List<Post> posts = postRepository.findByUserId(userId);

        return posts
                .stream()
                .map((element) -> modelMapper.map(element, PostResponseDto.class))
                .collect(Collectors.toList());

    }
}
