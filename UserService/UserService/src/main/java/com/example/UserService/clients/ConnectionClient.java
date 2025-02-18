package com.example.UserService.clients;



import com.example.UserService.dto.NodeCreationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ConnectionService",path = "/connections")
public interface ConnectionClient {

//    @GetMapping("/core/first-degree")
//    List<PersonDto> getFirstConnections();

    @PostMapping("/core/createNode")
    String createNode(NodeCreationDTO nodeCreationDTO);

}
