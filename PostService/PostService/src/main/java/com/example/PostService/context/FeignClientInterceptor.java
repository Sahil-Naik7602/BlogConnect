package com.example.PostService.context;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = UserContextHolder.getUserId();
        if(userId != null) {
            requestTemplate.header("X-User-Id", userId.toString());
        }
    }
}
