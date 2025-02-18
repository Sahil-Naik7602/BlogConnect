package com.example.UserService.repository;

import com.example.UserService.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends Neo4jRepository<Person,Long> {
    @Query("CREATE (p:Person:`+ $name +` {userId: $userId, name: $name}) RETURN p")
    void createPersonWithLabel(Long userId, String name);
}
