package com.example.UserService.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private String name;

    @Relationship(type = "CONNECTED_TO",direction = Relationship.Direction.OUTGOING)
    private Set<Person> connections;

    public void addConnection(Person person) {
        this.connections.add(person);
    }
}
