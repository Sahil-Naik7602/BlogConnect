package com.example.NotificationService.consumer;

import com.example.NotificationService.clients.ConnectionClient;
import com.example.NotificationService.dto.PersonDto;
import com.example.NotificationService.entity.Notification;
import com.example.NotificationService.repository.NotificationRepository;
import com.example.PostService.event.PostCreatedEvent;
import com.example.PostService.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {
    private final ConnectionClient connectionClient;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        List<PersonDto> connections = connectionClient.getFirstConnections(postCreatedEvent.getUserId());
        for (PersonDto connection: connections){
            String msg = String.format("User %d has posted Check Out",postCreatedEvent.getUserId());
            sendNotification(msg, connection.getUserId());
        }

    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent){
        String msg = String.format("%d you post has been liked by %d",postLikedEvent.getCreatorId(),postLikedEvent.getLikedByUserId());
        sendNotification(msg, postLikedEvent.getCreatorId());
    }

    private void sendNotification(String msg,Long userId){
        Notification notificaton = new Notification();
        notificaton.setUserId(userId);
        notificaton.setMessage(msg);
        notificationRepository.save(notificaton);


    }
}
