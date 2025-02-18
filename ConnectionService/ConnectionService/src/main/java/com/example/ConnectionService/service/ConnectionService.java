package com.example.ConnectionService.service;

import com.example.ConnectionService.context.UserContextHolder;
import com.example.ConnectionService.dto.NodeCreationDTO;
import com.example.ConnectionService.entity.Person;
import com.example.ConnectionService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {

    private final PersonRepository personRepository;

    public List<Person> getFirstDegreeConnections(Long userId){
        log.info("Getting first Degree connections for userID: "+userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    public void connect(Long toId) {
        Long fromId = UserContextHolder.getUserId();
        Optional<Person> personA = personRepository.findByUserId(fromId);
        Optional<Person> personB = personRepository.findByUserId(toId);

        if (personA.isPresent() && personB.isPresent()) {
            Person a = personA.get();
            Person b = personB.get();

            a.addConnection(b); // Establish relationship
            personRepository.save(a); // Save changes
        } else {
            throw new RuntimeException("One or both persons not found!");
        }

    }

    public String createNode(NodeCreationDTO nodeCreationDTO) {
        log.info("Creating Node for user: {} with id: {}",nodeCreationDTO.getName(),nodeCreationDTO.getUserId());
        if (personRepository.findByUserId(nodeCreationDTO.getUserId()).isPresent()){
            throw new RuntimeException("Person already exists!");
        }
        Person person = new Person();
        person.setUserId(nodeCreationDTO.getUserId());
        person.setName(nodeCreationDTO.getName());
        personRepository.save(person);
        return "Created";
    }
}
