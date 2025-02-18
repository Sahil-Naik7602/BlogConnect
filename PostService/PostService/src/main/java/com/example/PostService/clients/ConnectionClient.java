package com.example.PostService.clients;

import com.example.PostService.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "ConnectionService",path = "/connections")
public interface ConnectionClient {

    @GetMapping("/core/first-degree")
    List<PersonDto> getFirstConnections();

}
