package com.example.ConnectionService.controller;

import com.example.ConnectionService.dto.NodeCreationDTO;
import com.example.ConnectionService.entity.Person;
import com.example.ConnectionService.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping("/connect/{toId}")
    public ResponseEntity<Void> connect( @PathVariable Long toId){
        connectionService.connect(toId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createNode")
    public ResponseEntity<String> createNode(@RequestBody NodeCreationDTO nodeCreationDTO){
        String label = connectionService.createNode(nodeCreationDTO);
        return ResponseEntity.ok("Node create Successfully with label: "+label);
    }

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstDegreeConnections(@RequestHeader("X-User-Id") Long userId){

        return ResponseEntity.ok(connectionService.getFirstDegreeConnections(userId));
    }

}
